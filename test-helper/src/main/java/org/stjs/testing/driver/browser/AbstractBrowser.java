package org.stjs.testing.driver.browser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.runners.model.InitializationError;
import org.openqa.selenium.browserlaunchers.locators.BrowserLocator;
import org.stjs.generator.ClassWithJavascriptResolver;
import org.stjs.testing.driver.AsyncProcess;
import org.stjs.testing.driver.DriverConfiguration;
import org.stjs.testing.driver.TestResult;

import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings({"restriction"})
public abstract class AbstractBrowser implements Browser {

	private DriverConfiguration config;

	public AbstractBrowser(DriverConfiguration config) {
		this.config = config;
	}

	protected void startProcess(BrowserLocator locator, String binPropertyName, String url) throws InitializationError {
		startProcess(buildProcess(locator, binPropertyName, url));
	}

	protected Process startProcess(ProcessBuilder builder) throws InitializationError {
		try {
			Process p = builder.start();

			if (config.isDebugEnabled()) {
				System.out.println("Started " + builder.command().get(0));
			}
			if (config.isDebugEnabled()) {
				StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream());
				outputGobbler.start();
			}
			return p;
		}
		catch (IOException e) {
			throw new InitializationError(e);
		}
	}

	protected ProcessBuilder buildProcess(BrowserLocator locator, String binPropertyName, String url) {
		String executableName = config.getProperty(binPropertyName);
		if (executableName == null) {
			executableName = locator.findBrowserLocationOrFail().launcherFilePath();
		}
		ProcessBuilder builder = new ProcessBuilder(executableName, url);
		builder.redirectErrorStream(true);
		return builder;
	}

	@Override
	public DriverConfiguration getConfig() {
		return config;
	}

	protected boolean isRunningOnWindows() {
		return System.getProperty("os.name").contains("Windows");
	}

	protected void appendScriptTag(StringBuilder builder, String script) throws IOException {
		// remove wrong leading classpath://
		String cleanScript = script.replace("classpath://", "/");
		// add a slash to prevent the browser to interpret the scheme
		builder.append("<script src='" + cleanScript + "'></script>\n");
	}

	protected void sendResponse(String content, HttpExchange exchange) throws IOException {
		byte[] response;
		try {
			response = content.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			// Cannot happen. UTF-8 is part of the character sets that must be supported by any implementation of java
			throw new RuntimeException(e);
		}
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);

		OutputStream output = exchange.getResponseBody();
		output.write(response);
		output.flush();
	}

	/**
	 * Reads the result of the last unit test from the specified HTTP request. The default implementation builds the test result by reading the
	 * "result" and "location" query string parameters
	 */
	public TestResult buildResult(Map<String, String> queryStringParameters, HttpExchange exchange) {
		String userAgent = exchange.getRequestHeaders().getFirst("User-Agent");
		String result = queryStringParameters.get("result");
		String location = queryStringParameters.get("location");
		String isAssert = queryStringParameters.get("isAssert");

		if (getConfig().isDebugEnabled()) {
			System.out.println("Result was: " + result + ", at " + location + ", from " + userAgent);
		}

		return new TestResult(userAgent, result, location, "true".equals(isAssert));
	}

	@Override
	public void stop() {
		// default implementation does nothing
	}

	protected static Set<Class<? extends AsyncProcess>> processSet(Class<? extends AsyncProcess>... clazz) {
		Set<Class<? extends AsyncProcess>> deps = new HashSet<Class<? extends AsyncProcess>>();
		for (Class<? extends AsyncProcess> c : clazz) {
			deps.add(c);
		}
		return deps;
	}

	class StreamGobbler extends Thread {
		InputStream is;

		// reads everything from is until empty.
		StreamGobbler(InputStream is) {
			this.is = is;
		}

		@Override
		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					System.out.println("<<" + line + ">>");
				}
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

}
