package org.stjs.testing.driver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class STJSTestServer {
	protected static final String BROWSER_CHECK_URI = "/check";
	protected static final String BROWSER_RESULT_URI = "/result";
	protected static final String BROWSER_TEST_URI = "/test";
	protected static final String BROWSER_GET_JS_URI = "/js";

	private final int port;
	private final int testTimeout;
	private final HttpServer httpServer;

	private final Map<Long, BrowserConnection> browserConnections = new HashMap<Long, BrowserConnection>();
	private long lastBrowserId = System.currentTimeMillis();
	private long lastTestId = System.currentTimeMillis();
	private boolean debug;

	private File testFile = null;
	private TestResultCollection results;
	private Set<Long> browserWithCurrentTest;

	public STJSTestServer(int port, int testTimeout, boolean debugParam) throws IOException {
		this.port = port;
		this.testTimeout = testTimeout;
		this.debug = debugParam;
		// create the HttpServer
		InetSocketAddress address = new InetSocketAddress(port);
		httpServer = HttpServer.create(address, 0);
		// create and register our handler
		HttpHandler handler = new HttpHandler() {

			@Override
			public void handle(HttpExchange exchange) throws IOException {
				if (debug) {
					System.out.println(exchange.getRequestMethod() + ": " + exchange.getRequestURI());
				}
				try {
					Map<String, String> params = parseQueryString(exchange.getRequestURI().getQuery());
					exchange.getResponseHeaders().add("Date", formatDateHeader(new Date()));
					exchange.getResponseHeaders().add("Last-Modified", formatDateHeader(new Date()));
					exchange.getResponseHeaders().add("Connection", "Keep-Alive");
					if ("/".equals(exchange.getRequestURI().getPath())) {
						handleResource("start.html", exchange);
					} else if (BROWSER_CHECK_URI.equals(exchange.getRequestURI().getPath())) {
						handleBrowser(params, exchange);
					} else if (BROWSER_RESULT_URI.equals(exchange.getRequestURI().getPath())) {
						handleBrowserResult(params, exchange);
					} else if (BROWSER_TEST_URI.equals(exchange.getRequestURI().getPath())) {
						handleBrowserTest(params, exchange);
					} else if (BROWSER_GET_JS_URI.equals(exchange.getRequestURI().getPath())) {
						handleBrowserGetJs(exchange.getRequestURI().getQuery(), exchange);
					} else {
						handleResource(exchange.getRequestURI().getPath().substring(1), exchange);
					}

				} catch (Exception ex) {
					System.err.println("Error processing request:" + ex);
					ex.printStackTrace();
				} finally {
					exchange.close();
				}
			}

		};
		httpServer.createContext("/", handler);
	}

	private String formatDateHeader(Date date) {
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.format(date);
	}

	private void addNoCache(HttpExchange exchange) {
		exchange.getResponseHeaders().add("CacheControl", "no-cache");
		exchange.getResponseHeaders().add("Pragma", "no-cache");
		exchange.getResponseHeaders().add("Expires", "-1");

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

	private synchronized void handleBrowserResult(Map<String, String> params, HttpExchange exchange) throws IOException {
		addNoCache(exchange);
		long id = parseLong(params.get("id"), -1);
		long testId = parseLong(params.get("testId"), -1);

		if ((id < 0) || (testId < 0)) {
			System.err.println("Test id or browser id missing");
			return;
		}

		BrowserConnection b = browserConnections.get(id);
		if (b != null) {
			if (testId == lastTestId && results != null) {
				results.addResult(b.buildResult(params.get("result"), params.get("location")));
			}
		}
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		OutputStream output = exchange.getResponseBody();
		output.flush();
	}

	private synchronized void handleResource(String path, HttpExchange exchange) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		try {
			if (is != null) {
				// TODO - add a proper mime type here
				if (path.endsWith(".js")) {
					exchange.getResponseHeaders().add("Content-type", "text/javascript");
				}
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				OutputStream output = exchange.getResponseBody();
				ByteStreams.copy(is, output);
				output.flush();
			} else {
				System.err.println(path + " was not found in classpath");
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
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

	private synchronized void handleBrowser(Map<String, String> params, HttpExchange exchange) throws IOException {
		addNoCache(exchange);
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		long id = parseLong(params.get("id"), -1);

		if (id < 0) {
			id = ++lastBrowserId;
		}

		BrowserConnection b = browserConnections.get(id);
		if (b == null) {
			b = new BrowserConnection(id, exchange.getRequestHeaders().getFirst("User-Agent"));
			browserConnections.put(id, b);
		}
		StringBuilder jsonResponse = new StringBuilder();
		jsonResponse.append("{");
		jsonResponse.append("id:").append(id);
		if ((testFile != null) && !browserWithCurrentTest.contains(id)) {
			jsonResponse.append(",src:").append("'").append(BROWSER_TEST_URI).append("?test=").append(lastTestId)
					.append("'");
			jsonResponse.append(",testId:").append(lastTestId);
			browserWithCurrentTest.add(id);
		}
		jsonResponse.append("}");

		OutputStream output = exchange.getResponseBody();
		output.write(jsonResponse.toString().getBytes());
		output.flush();
	}

	private synchronized void handleBrowserTest(Map<String, String> params, HttpExchange exchange) throws IOException {
		addNoCache(exchange);
		if ((testFile == null) || !testFile.exists()) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
			return;
		}
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		OutputStream output = exchange.getResponseBody();
		Files.copy(testFile, output);
		output.flush();
	}

	private synchronized void handleBrowserGetJs(String fileName, HttpExchange exchange) throws IOException,
			URISyntaxException {
		// Object modified = exchange.getRequestHeaders().get("If-Modified-Since");
		// System.out.println(fileName + ":" + exchange.getRequestHeaders().keySet());
		exchange.getResponseHeaders().add("Content-type", "text/javascript");
		URI uri = new URI(fileName);
		if ("classpath".equals(uri.getScheme())) {
			handleResource(uri.getPath(), exchange);
		} else {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStream output = exchange.getResponseBody();
			Files.copy(new File(uri.getPath().substring(1)), output);
			output.flush();
		}
	}

	public synchronized void start() {
		// start the server
		httpServer.start();
	}

	public synchronized void stop() {
		// start the server
		httpServer.stop(0);
	}

	public synchronized int getBrowserCount() {
		return browserConnections.size();
	}

	public TestResultCollection test(File srcFile) throws InterruptedException {
		int testBrowsers;
		synchronized (this) {
			lastTestId++;
			testFile = srcFile;
			results = new TestResultCollection();
			browserWithCurrentTest = new HashSet<Long>();
			testBrowsers = browserConnections.size();
		}
		long endTime = System.currentTimeMillis() + (testTimeout * 1000);

		synchronized (results) {
			while (results.size() != testBrowsers) {
				long stillToWait = endTime - System.currentTimeMillis();
				if (stillToWait <= 0) {
					break;
				}
				results.wait(stillToWait);
			}
		}

		testFile = null;
		if (results.size() == 0) {
			results.addResult(new TestResult("none", "No test responded back in " + testTimeout + " seconds", "none"));
		}
		return results;
	}

	public URL getHostURL() {
		try {
			return new URL("http", "localhost", port, "/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
