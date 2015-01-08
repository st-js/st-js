package org.stjs.testing.driver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.runners.model.InitializationError;
import org.stjs.testing.driver.browser.LongPollingBrowser;

import com.google.common.base.Charsets;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Manages the HTTP server that is used to send tests to the browsers.
 * @author lordofthepigs
 */
@SuppressWarnings("restriction")
public class HttpLongPollingServer implements AsyncProcess {
	private static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
	public static final String NEXT_TEST_URI = "/getNextTest";
	public static final String BLANK_URI = "/about:blank";

	private final DriverConfiguration config;
	private final HttpServer httpServer;
	private final Map<Long, LongPollingBrowser> browsers = new ConcurrentHashMap<Long, LongPollingBrowser>();
	private final Map<Long, Long> selfAssignedBrowserIds = new ConcurrentHashMap<Long, Long>();
	private final Set<String> notFound = new HashSet<String>();

	/**
	 * Configures and starts the HTTP server
	 */
	public HttpLongPollingServer(DriverConfiguration config) throws InitializationError {
		this.config = config;
		// create the HttpServer
		InetSocketAddress address = new InetSocketAddress(config.getPort());
		try {
			httpServer = HttpServer.create(address, 0);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
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
	}

	@Override
	public void start() throws InitializationError {
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
	public long registerBrowserSession(LongPollingBrowser browser) {
		long id = browsers.size();
		browsers.put(id, browser);
		return id;
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
				exchange.getResponseHeaders().add("Connection", "Keep-Alive");
				exchange.getResponseHeaders().add("Server", "STJS");
				boolean dryRun = false;

				if(exchange.getRequestMethod().equals("HEAD") || exchange.getRequestMethod().equals("OPTIONS")){
					exchange = new NoBodyHttpExchange(exchange);
					dryRun = true;
				}

				// now really handle the request
				Map<String, String> params = parseQueryString(exchange.getRequestURI().getRawQuery());
				String path = exchange.getRequestURI().getPath();
				if (NEXT_TEST_URI.equals(path)) {
					handleNextTest(params, exchange, dryRun);

				} else if (BLANK_URI.equals(path)) {
					handleAboutBlank(exchange);

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

		private void handleAboutBlank(HttpExchange exchange) throws IOException {
			byte[] response = "<html></html>".getBytes("UTF-8");
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
			exchange.getResponseBody().write(response);
			exchange.getResponseBody().flush();
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
		private void handleNextTest(Map<String, String> params, HttpExchange exchange, boolean dryRun) {
			// Read the test results returned by the browser, if any
			long browserId = parseLong(params.get("browserId"), -1);
			LongPollingBrowser browser = browsers.get(browserId);
			if (browser == null) {
				browser = selfAssignedBrowser(browserId);
			}
			MultiTestMethod completedMethod = browser.getMethodUnderExecution();
			if (completedMethod != null) {
				// We only have a method under execution, if the HTTP request that is being
				// handled is not the first one the server has received
				if (config.isDebugEnabled()) {
					System.out.println("Server received test results for method " + completedMethod.toString() + " from browser " + browserId);
				}

				// notify JUnit of the result of this test. When the last browser notifies
				// the MultiTestMethod, the JUnit thread will become unblocked and the test result
				// will be reported
				if(!dryRun) {
					TestResult result = browser.buildResult(params, exchange);
					completedMethod.notifyExecutionResult(result);
				}
			} else {
				if (config.isDebugEnabled()) {
					System.out.println("Server received request for the first test from browser " + browserId);
				}
			}

			// Wait for the JUnit thread to send us the next test. We block this thread
			// until we have a new test to send to the browser or the server is shutdown,
			// whichever comes first. Basically, we are not sending the HTTP response to the
			// browser until we have received a new test
			MultiTestMethod nextMethod = null;
			if(!dryRun){
				nextMethod = browser.awaitNextTest();
			}
			if (nextMethod != null) {
				if (config.isDebugEnabled()) {
					System.out.println("Server is sending test for method " + nextMethod.toString() + " to browser " + browserId);
				}
				try {
					browser.sendTestFixture(nextMethod, exchange);
				}
				catch (Exception e) {
					// we failed to send the fixture. This means that the browser will not request the next test,
					// it is therefore essentially dead.
					browser.markAsDead(e, exchange.getRequestHeaders().getFirst("User-Agent"));
					throw new RuntimeException(e);
				}

			} else {
				try {
					// Note: This case will always be triggered for a dry run (ie: in response to HEAD or
					// OPTIONS requests). However, it really doesn't matter because in those cases, the HttpExchange
					// is always an instance of NoBodyHttpExchange which never really sends the response body anyway.
					browser.sendNoMoreTestFixture(exchange);
				}
				catch (IOException ioe) {
					// sending a 500 error has basically the same effect as sending a proper response. The browser may
					// not cleanup properly, but hey, this is disaster recovery
					throw new RuntimeException(ioe);
				}
			}
		}

		/**
		 * this method returns an id of a registered browser when the id received from the client does not correspond to an existing one - i.e.
		 * it's randomly generated on the client.
		 * @param browserId
		 * @return
		 */
		private synchronized LongPollingBrowser selfAssignedBrowser(long browserId) {
			Long correspondingId = selfAssignedBrowserIds.get(browserId);
			if (correspondingId != null) {
				return browsers.get(correspondingId);
			}
			// find a browser that does not have yet an ID assigned
			for (LongPollingBrowser browser : browsers.values()) {
				if (!selfAssignedBrowserIds.containsValue(browser.getId())) {
					selfAssignedBrowserIds.put(browserId, browser.getId());
					return browser;
				}
			}
			throw new RuntimeException("More browser connections than configured browsers");
		}

		private synchronized void handleResource(String path, HttpExchange exchange) throws IOException, URISyntaxException {
			if (notFound.contains(path)) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, -1);
			return;
			}
			if (path.endsWith(".js")) {
				exchange.getResponseHeaders().add("Content-Type", "text/javascript");
			} else if (path.endsWith(".html")) {
				exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
			}

			String ifModifiedSinceHeader = exchange.getRequestHeaders().getFirst("If-Modified-Since");
			Date ifModifiedSince = parseDateHeader(ifModifiedSinceHeader);

			// XXX: legacy fix
			String cleanPath = path.replaceFirst("file:/+target", "target");

			Date lastModified = StreamUtils.getResourceModifiedDate(config.getClassLoader(), cleanPath);
			exchange.getResponseHeaders().add("Last-Modified", formatDateHeader(lastModified));

			if (ifModifiedSince != null && !lastModified.after(ifModifiedSince)) {
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_MODIFIED, -1);
				return;
			}
			if (!StreamUtils.copy(config.getClassLoader(), cleanPath, exchange)) {
				notFound.add(path);
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
					try {
						params.put(x[0], URLDecoder.decode(x[1], Charsets.UTF_8.name()));
					}
					catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				}
			}
			return params;
		}

		private Date parseDateHeader(String header) {
			if (header == null) {
				return null;
			}
			DateFormat df = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.ENGLISH);
			try {
				return df.parse(header);
			}
			catch (ParseException e) {
				System.err.println("Cannot parse date header:" + e);
				return null;
			}
		}

		private String formatDateHeader(Date date) {
			DateFormat df = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.ENGLISH);
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

	@Override
	public void stop() {
		this.httpServer.stop(5);
	}
}
