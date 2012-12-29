package org.stjs.testing.driver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.runners.model.InitializationError;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Manages the HTTP server that is used to send tests to the browsers.
 * @author lordofthepigs
 */
@SuppressWarnings("restriction")
public class AsyncServerSession {

	public static final String NEXT_TEST_URI = "/getNextTest";

	private final DriverConfiguration config;
	private final HttpServer httpServer;
	private final Map<Long, AsyncBrowserSession> browsers = new ConcurrentHashMap<Long, AsyncBrowserSession>();

	/**
	 * Configures and starts the HTTP server
	 */
	public AsyncServerSession(DriverConfiguration config) throws InitializationError {
		this.config = config;
		// create the HttpServer
		InetSocketAddress address = new InetSocketAddress(config.getPort());
		try {
			httpServer = HttpServer.create(address, 0);
		}
		catch (IOException e) {
			throw new InitializationError(e);
		}

		// by default, the HttpServer uses a single thread to respond to all requests given that we block responses 
		// to tests requests while waiting for new tests, and that the new tests are only sent when all browsers have
		// responded, one thread is not enough to handle multiple browsers. 
		// Let's give him an executor that is a bit more flexible
		httpServer.setExecutor(Executors.newFixedThreadPool(config.getBrowserCount() * 2, new ThreadFactory() {
			private AtomicInteger i = new AtomicInteger(0);

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("httpServer-" + i.incrementAndGet());
				t.setDaemon(true);
				return t;
			}
		}));

		// create and register our handler
		httpServer.createContext("/", new AsyncHttpHandler());

		if (config.isDebugEnabled()) {
			System.out.println("Server session created");
		}
		this.start();
	}

	private void start() {
		httpServer.start();
		if (config.isDebugEnabled()) {
			System.out.println("Server session started");
		}
	}

	/**
	 * Registers the specified browser session with this HTTP server, so that this server knows how to respond to HTTP requests containing the
	 * specified session's id. This method is expected to be called many times in a row before any unit test is started, once per browser
	 * session.
	 */
	public void addBrowserConnection(AsyncBrowserSession browserSession) {
		browsers.put(browserSession.getId(), browserSession);
	}

	/**
	 * Instructs the HTTP server to respond with the specified test to the specified browser session the next time that session asks for test to
	 * run.
	 */
	public void queueTest(AsyncMethod method, AsyncBrowserSession browser) {
		// notify the browser that a new test is ready to be executed. This will actually cause this
		// server session to release the HTTP response to any previous request, thereby letting the
		// actual browser know that a new test is ready, and providing it with the necessary instructions
		// to run it.
		browser.notifyNewTestReady(method);
	}

	private final class AsyncHttpHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			if (config.isDebugEnabled()) {
				System.out.println(exchange.getRequestMethod() + ": " + exchange.getRequestURI());
			}

			try {

				// add some common response headers
				exchange.getResponseHeaders().add("Date", formatDateHeader(new Date()));
				exchange.getResponseHeaders().add("Last-Modified", formatDateHeader(new Date()));
				exchange.getResponseHeaders().add("Connection", "Keep-Alive");
				exchange.getResponseHeaders().add("Server", "STJS");

				// now really handle the request
				Map<String, String> params = parseQueryString(exchange.getRequestURI().getQuery());
				String path = exchange.getRequestURI().getPath();
				if (NEXT_TEST_URI.equals(path)) {
					handleNextTest(params, exchange);
				} else {
					handleResource(path, exchange);
				}
			}
			catch (Exception ex) {
				System.err.println("Error processing request:" + ex);
				ex.printStackTrace();
			}
			finally {
				exchange.close();
			}
		}

		/**
		 * Called when this HTTP server receives a request for the next test from a browser. This method blocks until one of these to conditions
		 * are met:<br>
		 * <ol>
		 * <li>This server receives a new test to send to the browser session that made the request (via the queueTest()) method.
		 * <li>This server is notified that no more tests are remaining (via AsyncBrowserSession.notifyNoMoreTest())
		 * </ol>
		 * Once one of these events has happened, this HTTP server sends the appropriate HTML/javascript response before returning from this
		 * method.
		 */
		private void handleNextTest(Map<String, String> params, HttpExchange exchange) {
			// Read the test results returned by the browser, if any
			long browserId = parseLong(params.get("browserId"), -1);
			AsyncBrowserSession browser = browsers.get(browserId);
			AsyncMethod completedMethod = browser.getMethodUnderExecution();
			if (completedMethod != null) {
				// We only have a method under execution, if the HTTP request that is being
				// handled is not the first one the server has received
				if (config.isDebugEnabled()) {
					System.out.println("Server received test results for method " + completedMethod.toString() + " from browser " + browserId);
				}

				// notify JUnit of the result of this test. When the last browser notifies
				// the AsyncMethod, the JUnit thread will become unblocked and the test result
				// will be reported
				TestResult result = browser.buildResult(params, exchange);
				completedMethod.notifyExecutionResult(result);
			} else {
				if (config.isDebugEnabled()) {
					System.out.println("Server received request for the first test from browser " + browserId);
				}
			}

			// Wait for the JUnit thread to send us the next test. We block this thread
			// until we have a new test to send to the browser or the server is shutdown,
			// whichever comes first. Basically, we are not sending the HTTP response to the
			// browser until we have received a new test
			AsyncMethod nextMethod = browser.awaitNewTestReady();
			if (nextMethod != null) {
				if (config.isDebugEnabled()) {
					System.out.println("Server is sending test for method " + nextMethod.toString() + " to browser " + browserId);
				}
				browser.sendTestFixture(nextMethod, exchange);
			} else {
				browser.sendNoMoreTestFixture(browser, exchange);
			}
		}

		private synchronized void handleResource(String path, HttpExchange exchange) throws IOException, URISyntaxException {
			if (path.endsWith(".js")) {
				exchange.getResponseHeaders().add("Content-Type", "text/javascript");
			} else if (path.endsWith(".html")) {
				exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
			}
			// XXX: legacy fix
			String cleanPath = path.replaceFirst("file:/+target", "target");
			if (!StreamUtils.copy(config.getClassLoader(), cleanPath, exchange)) {
				System.err.println(cleanPath + " was not found in classpath");
			}
		}

		private Map<String, String> parseQueryString(String query) {
			// TODO use a real query parser
			Map<String, String> params = new HashMap<String, String>();
			if (query == null) {
				return params;
			}
			String[] nameValues = query.split("&");
			for (String nv : nameValues) {
				String[] x = nv.split("=");
				if (x.length == 2) {
					params.put(x[0], x[1]);
				}
			}
			return params;
		}

		private String formatDateHeader(Date date) {
			DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH);
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			return df.format(date);
		}

		private long parseLong(String s, long defaultValue) {
			if (s == null) {
				return defaultValue;
			}
			try {
				return Long.parseLong(s);
			}
			catch (Exception ex) {
				return defaultValue;
			}
		}
	}

	public void stop() {
		this.httpServer.stop(5);
	}
}
