package org.stjs.testing.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class STJSTestServer {
	protected static final String BROWSER_CHECK_URI = "/check";
	protected static final String BROWSER_RESULT_URI = "/result";
	protected static final String BROWSER_TEST_URI = "/test";
	private final int port = 8000;
	private final HttpServer httpServer;

	private final Map<Long, BrowserConnection> browserConnections = new HashMap<Long, BrowserConnection>();
	private long lastBrowserId = System.currentTimeMillis();
	private long lastTestId = System.currentTimeMillis();

	private File testFile = null;

	public STJSTestServer() throws IOException {
		// create the HttpServer
		InetSocketAddress address = new InetSocketAddress(port);
		httpServer = HttpServer.create(address, 0);
		// create and register our handler
		HttpHandler handler = new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {
				System.out.println("GET:" + exchange.getRequestURI());
				try {
					exchange.getResponseHeaders().add("CacheControl", "no-cache");
					exchange.getResponseHeaders().add("Pragma", "no-cache");
					exchange.getResponseHeaders().add("Expires", "-1");

					Map<String, String> params = parseQueryString(exchange.getRequestURI().getQuery());

					if (BROWSER_CHECK_URI.equals(exchange.getRequestURI().getPath())) {
						handleBrowser(params, exchange);
					} else if (BROWSER_RESULT_URI.equals(exchange.getRequestURI().getPath())) {
						handleBrowserResult(params, exchange);
					} else if (BROWSER_TEST_URI.equals(exchange.getRequestURI().getPath())) {
						handleBrowserTest(params, exchange);
					} else {
						handleResource(exchange.getRequestURI().getPath(), exchange);
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

	private synchronized void handleBrowserResult(Map<String, String> params, HttpExchange exchange) {
		long id = parseLong(params.get("id"), -1);
		long testId = parseLong(params.get("testId"), -1);

		if (id < 0 || testId < 0) {
			System.out.println("Test id or browser id missing");
			return;
		}

		BrowserConnection b = browserConnections.get(id);
		if (b != null) {
			if (testId == lastTestId) {
				b.setResult(params.get("result"));
				b.setLastTestId(testId);
				notify();
			}
		}
	}

	private synchronized void handleResource(String path, HttpExchange exchange) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path.substring(1));
		if (is != null) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			byte[] buffer = new byte[100000];
			int length = is.read(buffer);
			exchange.getResponseBody().write(buffer, 0, length);
			is.close();
		} else {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
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
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		long id = parseLong(params.get("id"), -1);

		if (id < 0) {
			id = ++lastBrowserId;
		}

		BrowserConnection b = browserConnections.get(id);
		if (b == null) {
			b = new BrowserConnection();
			browserConnections.put(id, b);
		}
		StringBuilder jsonResponse = new StringBuilder();
		jsonResponse.append("{");
		jsonResponse.append("id:").append(id);
		if (testFile != null && b.getLastTestId() != lastTestId) {
			jsonResponse.append(",src:").append("'").append(BROWSER_TEST_URI).append("?test=").append(lastTestId)
					.append("'");
			jsonResponse.append(",testId:").append(lastTestId);
		}
		jsonResponse.append("}");

		exchange.getResponseBody().write(jsonResponse.toString().getBytes());
	}

	private synchronized void handleBrowserTest(Map<String, String> params, HttpExchange exchange) throws IOException {

		if (testFile == null || !testFile.exists()) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
			return;
		}
		InputStream is = new FileInputStream(testFile);
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		byte[] buffer = new byte[100000];
		int length = is.read(buffer);
		exchange.getResponseBody().write(buffer, 0, length);
		is.close();

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

	public synchronized String test(File srcFile, int timeout) throws InterruptedException {
		lastTestId++;
		testFile = srcFile;
		System.out.println("--> testing :" + lastTestId + ", file:" + testFile);
		int testBrowsers = 0;
		long endTime = System.currentTimeMillis() + timeout;
		StringBuilder result = new StringBuilder();
		while (true) {
			for (BrowserConnection b : browserConnections.values()) {
				if (b.getLastTestId() == lastTestId) {
					System.out.println("FOUND result:" + b);
					if (result.toString().equals("OK") && b.getResult().equals("OK")) {
						// keep one ok only
					} else {
						result.append(b.getResult());
						result.append("\n");
					}
					testBrowsers++;
				}
			}
			if (testBrowsers == browserConnections.size() || System.currentTimeMillis() >= endTime) {
				break;
			}
			wait(500);
		}
		System.out.println("<-- testing :" + lastTestId + ", file:" + testFile);

		testFile = null;
		return result.toString().trim();
	}

	public URL getHostURL() {
		try {
			return new URL("http", "localhost", port, "/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
