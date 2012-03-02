/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.testing.driver;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class STJSTestServer {
	protected static final String BROWSER_CHECK_URI = "/check";
	protected static final String BROWSER_RESULT_URI = "/result";
	protected static final String BROWSER_TEST_URI = "/test";

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

	private final ClassLoader classLoader;

	public STJSTestServer(ClassLoader classLoader, int port, int testTimeout, boolean debugParam) throws IOException {
		this.port = port;
		this.testTimeout = testTimeout;
		this.debug = debugParam;
		this.classLoader = classLoader;
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
					exchange.getResponseHeaders().add("Server", "STJS");
					String path = exchange.getRequestURI().getPath();
					if ("/".equals(path) || "/start.html".equals(path)) {
						handleResource("/start.html", exchange);
					} else if (BROWSER_CHECK_URI.equals(path)) {
						handleBrowser(params, exchange);
					} else if (BROWSER_RESULT_URI.equals(path)) {
						handleBrowserResult(params, exchange);
					} else if (BROWSER_TEST_URI.equals(path)) {
						handleBrowserTest(params, exchange);
					} else {
						handleResource(path, exchange);
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

		if (id < 0 || testId < 0) {
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

	private synchronized void handleResource(String path, HttpExchange exchange) throws IOException, URISyntaxException {
		if (path.endsWith(".js")) {
			exchange.getResponseHeaders().add("Content-Type", "text/javascript");
		} else if (path.endsWith(".html")) {
			exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
		}
		// XXX: legacy fix
		String cleanPath = path.replaceFirst("file:/+target", "target");
		if (!StreamUtils.copy(classLoader, cleanPath, exchange)) {
			System.err.println(cleanPath + " was not found in classpath");
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
		if (testFile != null && !browserWithCurrentTest.contains(id)) {
			jsonResponse.append(",src:").append("'").append(BROWSER_TEST_URI).append("?test=").append(lastTestId)
					.append("'");
			jsonResponse.append(",testId:").append(lastTestId);
			jsonResponse.append(",className:'").append(results.getTestClassName()).append("'");
			jsonResponse.append(",methodName:'").append(results.getTestMethodName()).append("'");
			browserWithCurrentTest.add(id);
		}
		jsonResponse.append("}");

		byte[] response = jsonResponse.toString().getBytes();
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);

		OutputStream output = exchange.getResponseBody();
		output.write(response);
		output.flush();
	}

	private synchronized void handleBrowserTest(Map<String, String> params, HttpExchange exchange) throws IOException,
			URISyntaxException {
		addNoCache(exchange);
		if (!StreamUtils.copy(classLoader, "/" + testFile.getName(), exchange)) {
			System.err.println("/" + testFile.getName() + " was not found in classpath");
		}
	}

	public synchronized void start() {
		// start the server
		httpServer.start();
	}

	public synchronized void stop() {
		// stop the server
		httpServer.stop(0);
	}

	public synchronized int getBrowserCount() {
		return browserConnections.size();
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public TestResultCollection test(File srcFile, String testClassName, String testMethodName)
			throws InterruptedException {
		int testBrowsers;
		synchronized (this) {
			lastTestId++;
			testFile = srcFile;
			results = new TestResultCollection(testClassName, testMethodName);
			browserWithCurrentTest = new HashSet<Long>();
			testBrowsers = browserConnections.size();
		}
		long endTime = System.currentTimeMillis() + testTimeout * 1000;

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
