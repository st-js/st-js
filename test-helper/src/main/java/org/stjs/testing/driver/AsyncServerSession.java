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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class AsyncServerSession {

	public static final String NEXT_TEST_URI = "/getNextTest";

	private static AsyncServerSession instance;

	private final DriverConfiguration config;
	private final HttpServer httpServer;
	private final Map<Long, AsyncBrowserSession> browsers = new ConcurrentHashMap<Long, AsyncBrowserSession>();

	private AsyncServerSession(DriverConfiguration config) throws IOException {
		this.config = config;
		// create the HttpServer
		InetSocketAddress address = new InetSocketAddress(config.getPort());
		httpServer = HttpServer.create(address, 0);

		// create and register our handler
		httpServer.createContext("/", new AsyncHttpHandler());

		if(config.isDebugEnabled()){
			System.out.println("Server session created");
		}
		this.start();
	}

	public static synchronized AsyncServerSession getInstance(DriverConfiguration config) throws IOException {
		if (instance == null) {
			instance = new AsyncServerSession(config);
		}
		return instance;
	}

	private void start(){
		httpServer.start();
		if(config.isDebugEnabled()){
			System.out.println("Server session started");
		}
	}

	public void addBrowserConnection(AsyncBrowserSession browser) {
		browsers.put(browser.getId(), browser);
	}

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
			if(config.isDebugEnabled()){
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
				if(NEXT_TEST_URI.equals(path)){
					handleNextTest(params, exchange);
				} else {
					handleResource(path, exchange);
				}
			} catch (Exception ex) {
				System.err.println("Error processing request:" + ex);
				ex.printStackTrace();
			} finally {
				exchange.close();
			}
			//
			//				//			try {
			//				Map<String, String> params = parseQueryString(exchange.getRequestURI().getQuery());
			//				exchange.getResponseHeaders().add("Date", formatDateHeader(new Date()));
			//				exchange.getResponseHeaders().add("Last-Modified", formatDateHeader(new Date()));
			//				exchange.getResponseHeaders().add("Connection", "Keep-Alive");
			//				exchange.getResponseHeaders().add("Server", "STJS");
			//				String path = exchange.getRequestURI().getPath();
			//				if ("/".equals(path) || "/start.html".equals(path)) {
			//					handleResource("/start.html", exchange);
			//				} else if (BROWSER_CHECK_URI.equals(path)) {
			//					handleBrowser(params, exchange);
			//				} else if (BROWSER_RESULT_URI.equals(path)) {
			//					handleBrowserResult(params, exchange);
			//				} else if (BROWSER_TEST_URI.equString path = exchange.getRequestURI().getPath();als(path)) {
			//					handleBrowserTest(params, exchange);
			//				} else {
			//					handleResource(path, exchange);
			//				}
			//
			//			} catch (Exception ex) {
			//				System.err.println("Error processing request:" + ex);
			//				ex.printStackTrace();
			//			} finally {
			//				exchange.close();
			//			}
		}

		private void handleNextTest(Map<String, String> params, HttpExchange exchange) {
			// Read the test results returned by the browser, if any
			long browserId = parseLong(params.get("browserId"), -1);
			AsyncBrowserSession browser = browsers.get(browserId);
			AsyncMethod completedMethod = browser.getMethodUnderExecution();
			if(completedMethod != null){
				if(config.isDebugEnabled()){
					System.out.println("Server received test results for method " + completedMethod.toString() +
							" from browser " + browserId);
				}
				// We only have a method under execution, if the HTTP request that is being
				// handled is not the first one the server has received

				// TODO: code a real implementation. for now, we just say it's successful
				// notify JUnit of the result of this test. When the last browser notifies
				// the AsyncMethod, the JUnit thread will become unblocked and the test result
				// will be reported
				completedMethod.notifyExecutionResult(null);
			} else {
				if(config.isDebugEnabled()){
					System.out.println("Server received request for the first test from browser " + browserId);
				}
			}

			// Wait for the JUnit thread to send us the next test. We block this thread
			// until we have a new test to send to the browser or the server is shutdown,
			// whichever comes first. Basically, we are not sending the HTTP response to the
			// browser until we have received a new test
			AsyncMethod nextMethod = browser.awaitNewTestReady();
			if(nextMethod != null){
				if(config.isDebugEnabled()){
					System.out.println("Server is sending test for method " + nextMethod.toString() + " to browser " + browserId);
				}
				browser.sendTestFixture(nextMethod, exchange);
				// TODO: send the proper HTTP response, with the instructions to run the next test
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
			if(config.isDebugEnabled()){
				System.out.println("Sending resource from classpath://" + cleanPath);
			}
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
			} catch (Exception ex) {
				return defaultValue;
			}
		}
	}

	//	protected static final String BROWSER_CHECK_URI = "/check";
	//	protected static final String BROWSER_RESULT_URI = "/result";
	//	protected static final String BROWSER_TEST_URI = "/test";

	//	private final Map<Long, BrowserConnection> browserConnections = new HashMap<Long, BrowserConnection>();
	//	private long lastBrowserId = System.currentTimeMillis();
	//	private long lastTestId = System.currentTimeMillis();
	//	private boolean debug;
	//
	//	private File testFile = null;
	//	private TestResultCollection results;
	//	private Set<Long> browserWithCurrentTest;



	//
	//	private void addNoCache(HttpExchange exchange) {
	//		exchange.getResponseHeaders().add("CacheControl", "no-cache");
	//		exchange.getResponseHeaders().add("Pragma", "no-cache");
	//		exchange.getResponseHeaders().add("Expires", "-1");
	//	}



	//	private synchronized void handleBrowserResult(Map<String, String> params, HttpExchange exchange) throws IOException {
	//		addNoCache(exchange);
	//		long id = parseLong(params.get("id"), -1);
	//		long testId = parseLong(params.get("testId"), -1);
	//
	//		if (id < 0 || testId < 0) {
	//			System.err.println("Test id or browser id missing");
	//			return;
	//		}
	//
	//		BrowserConnection b = browserConnections.get(id);
	//		if (b != null) {
	//			if (testId == lastTestId && results != null) {
	//				results.addResult(b.buildResult(params.get("result"), params.get("location")));
	//			}
	//		}
	//		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
	//		OutputStream output = exchange.getResponseBody();
	//		output.flush();
	//	}
	//
	//	private synchronized void handleResource(String path, HttpExchange exchange) throws IOException, URISyntaxException {
	//		if (path.endsWith(".js")) {
	//			exchange.getResponseHeaders().add("Content-Type", "text/javascript");
	//		} else if (path.endsWith(".html")) {
	//			exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
	//		}
	//		// XXX: legacy fix
	//		String cleanPath = path.replaceFirst("file:/+target", "target");
	//		if (!StreamUtils.copy(classLoader, cleanPath, exchange)) {
	//			System.err.println(cleanPath + " was not found in classpath");
	//		}
	//	}
	//
	//	private long parseLong(String s, long defaultValue) {
	//		if (s == null) {
	//			return defaultValue;
	//		}
	//		try {
	//			return Long.parseLong(s);
	//		} catch (Exception ex) {
	//			return defaultValue;
	//		}
	//	}
	//
	//	private synchronized void handleBrowser(Map<String, String> params, HttpExchange exchange) throws IOException {
	//		addNoCache(exchange);
	//		long id = parseLong(params.get("id"), -1);
	//
	//		if (id < 0) {
	//			id = ++lastBrowserId;
	//		}
	//
	//		BrowserConnection b = browserConnections.get(id);
	//		if (b == null) {
	//			b = new BrowserConnection(id, exchange.getRequestHeaders().getFirst("User-Agent"));
	//			browserConnections.put(id, b);
	//		}
	//		StringBuilder jsonResponse = new StringBuilder();
	//		jsonResponse.append("{");
	//		jsonResponse.append("id:").append(id);
	//		if (testFile != null && !browserWithCurrentTest.contains(id)) {
	//			jsonResponse.append(",src:").append("'").append(BROWSER_TEST_URI).append("?test=").append(lastTestId)
	//			.append("'");
	//			jsonResponse.append(",testId:").append(lastTestId);
	//			jsonResponse.append(",className:'").append(results.getTestClassName()).append("'");
	//			jsonResponse.append(",methodName:'").append(results.getTestMethodName()).append("'");
	//			browserWithCurrentTest.add(id);
	//		}
	//		jsonResponse.append("}");
	//
	//		byte[] response = jsonResponse.toString().getBytes();
	//		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
	//
	//		OutputStream output = exchange.getResponseBody();
	//		output.write(response);
	//		output.flush();
	//	}
	//
	//	private synchronized void handleBrowserTest(Map<String, String> params, HttpExchange exchange) throws IOException,
	//	URISyntaxException {
	//		addNoCache(exchange);
	//		if (testFile == null) {
	//			exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
	//			return;
	//		}
	//		if (!StreamUtils.copy(classLoader, "/" + testFile.getName(), exchange)) {
	//			System.err.println("/" + testFile.getName() + " was not found in classpath");
	//		}
	//	}

	//	public TestResultCollection test(File srcFile, String testClassName, String testMethodName)
	//			throws InterruptedException {
	//		int testBrowsers;
	//		synchronized (this) {
	//			lastTestId++;
	//			testFile = srcFile;
	//			results = new TestResultCollection(testClassName, testMethodName);
	//			browserWithCurrentTest = new HashSet<Long>();
	//			testBrowsers = browserConnections.size();
	//		}
	//		long endTime = System.currentTimeMillis() + testTimeout * 1000;
	//
	//		synchronized (results) {
	//			while (results.size() != testBrowsers) {
	//				long stillToWait = endTime - System.currentTimeMillis();
	//				if (stillToWait <= 0) {
	//					break;
	//				}
	//				results.wait(stillToWait);
	//			}
	//		}
	//
	//		testFile = null;
	//		if (results.size() == 0) {
	//			results.addResult(new TestResult("none", "No test responded back in " + testTimeout + " seconds", "none"));
	//		}
	//		return results;
	//	}
	//
	//	public URL getHostURL() {
	//		try {
	//			return new URL("http", "localhost", port, "/");
	//		} catch (MalformedURLException e) {
	//			throw new RuntimeException(e);
	//		}
	//	}
}
