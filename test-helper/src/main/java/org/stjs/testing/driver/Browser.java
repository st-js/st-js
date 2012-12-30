package org.stjs.testing.driver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

/**
 * Handles all the details of starting, stopping and communicating with a specific browser through HTTP or other means.
 * @author lordofthepigs
 */
@SuppressWarnings("restriction")
public interface Browser {

	/**
	 * Opens a new browser and makes it ready to accept unit tests for execution.
	 * @param browserId the ID that the browser has to report to the HTTP server everytime it communicates with it.
	 */
	public void start(long browserId);

	public DriverConfiguration getConfig();

	/**
	 * Writes to the HTTP response the HTML and/or javascript code that is necessary for the browser to execute the specified test.
	 * @param meth The test to send to the browser
	 * @param browserSession The session to which the test is sent
	 * @param exchange contains the HTTP response that must be written to
	 */
	public void sendTestFixture(AsyncMethod meth, AsyncBrowserSession browserSession, HttpExchange exchange) throws IOException,
			URISyntaxException;

	/**
	 * Writes to the HTTP response the HTML and/or javascript code that is necessary for the browser understand that there will be no more tests.
	 * @param browserSession The session to be notified
	 * @param exchange contains the HTTP response that must be written to
	 */
	public void sendNoMoreTestFixture(AsyncBrowserSession browser, HttpExchange exchange) throws IOException, URISyntaxException;

	public TestResult buildResult(Map<String, String> queryStringParameters, HttpExchange exchange);

	/**
	 * Opens a new browser and makes it ready to accept unit tests for execution.
	 * @param browserId the ID that the browser has to report to the HTTP server everytime it communicates with it.
	 */
	public void stop();
}
