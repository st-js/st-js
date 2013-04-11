package org.stjs.testing.driver.browser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.runners.model.InitializationError;
import org.openqa.selenium.browserlaunchers.locators.BrowserInstallation;
import org.openqa.selenium.browserlaunchers.locators.SingleBrowserLocator;
import org.stjs.testing.driver.AsyncProcess;
import org.stjs.testing.driver.DriverConfiguration;
import org.stjs.testing.driver.HttpLongPollingServer;
import org.stjs.testing.driver.TestResult;

import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings({ "restriction" /* for HttpExchange */, "deprecation" /* for @Scripts */})
public class PhantomjsBrowser extends LongPollingBrowser {

	public static final String PROP_PHANTOMJS_BIN = "phantomjs.bin";

	private File tempBootstrapJs;

	public PhantomjsBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void doStart() throws InitializationError {
		this.registerWithLongPollingServer();
		try {
			// We first need to extract phantomjs-bootstrap.js to the temp directory, because phantomjs
			// can only be started with a file on the local filesystem as argument
			tempBootstrapJs = unpackBootstrap();

			String executableName = getConfig().getProperty(PROP_PHANTOMJS_BIN);
			if (executableName == null) {
				BrowserInstallation installation = new Locator().findBrowserLocation();
				if (installation == null) {
					throw new InitializationError( //
							"phantomjs could not be found in the path!\n"
									+ "Please add the directory containing 'phantomjs' or 'phantomjs.exe' to your PATH environment\n"
									+ "variable, or explicitly specify a path to phantomjs in stjs-test.properties like this:\n"
									+ PROP_PHANTOMJS_BIN + "=/blah/blah/phantomjs");
				}
				executableName = installation.launcherFilePath();
			}
			new ProcessBuilder( //
					executableName, //
					"--web-security=no", //
					tempBootstrapJs.getAbsolutePath(), //
					Long.toString(getId()), //
					getConfig().getServerURL().toString()).start();

			if (getConfig().isDebugEnabled()) {
				System.out.println("Started phantomjs");
			}
		} catch (IOException e) {
			throw new InitializationError(e);
		}
	}

	@Override
	public void sendNoMoreTestFixture(HttpExchange exchange) throws IOException {
		byte[] response = "<html><head><script language='javascript'>parent.phantom.exit()</script></head></html>"
				.getBytes("UTF-8");
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);

		OutputStream output = exchange.getResponseBody();
		output.write(response);
		output.flush();
	}

	@Override
	public void stop() {
		// phantomJS automatically stops when the noMoreTests fixture is sent
		tempBootstrapJs.delete();
	}

	private File unpackBootstrap() throws IOException {
		File tmp = File.createTempFile("phantomjs", null);
		InputStream in = this.getClass().getResourceAsStream("/phantomjs-bootstrap.js");
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmp));
		byte[] buffer = new byte[8192];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) > 0) {
			out.write(buffer, 0, bytesRead);
		}
		in.close();
		out.close();
		return tmp;
	}

	@Override
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
	public Set<Class<? extends AsyncProcess>> getSharedDependencies() {
		Set<Class<? extends AsyncProcess>> dep = new HashSet<Class<? extends AsyncProcess>>();
		dep.add(HttpLongPollingServer.class);
		return dep;
	}

	private static class Locator extends SingleBrowserLocator {

		@Override
		protected String[] standardlauncherFilenames() {
			return new String[] { "phantomjs", "phantomjs.exe" };
		}

		@Override
		protected String[] usualLauncherLocations() {
			// phantomjs doesn't have a proper installer, so there really isn't any usual
			// location where it would be. Except maybe on linux versions that use package
			// managers
			return new String[] { "/usr/bin" };
		}

		@Override
		protected String seleniumBrowserName() {
			// not useful in stjs, but required by selenium
			return "phantomjs";
		}

		@Override
		protected String browserPathOverridePropertyName() {
			// not useful in stjs, but required by selenium
			return "phantomjs";
		}

		@Override
		protected String browserName() {
			// not useful in stjs, but required by selenium
			return "phantomjs";
		}
	}
}
