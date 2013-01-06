package org.stjs.testing.driver;

import java.util.Map;
import java.util.concurrent.Exchanger;

import org.junit.runners.model.InitializationError;
import org.stjs.testing.driver.browser.Browser;

import com.sun.net.httpserver.HttpExchange;

/**
 * Represents a testing session opened with one instance of some browser. AsyncBrowserSession is responsible for the communication between JUnit
 * (via STJSAsyncTestDriverRunner) and the HTTP server (via AsyncBrowserSession). JUnit notifies each browser that it needs a new test method to
 * be executed by calling notifyNewTestReady(AsyncMethod), or that it has finished executing all the tests by calling notifyNoMoreTests(). The
 * HTTP server waits for a new test to send to the browser by calling awaitNewTestReady(). <br>
 * <br>
 * On top of that, AsyncBrowserSession delegates the details of starting, stopping and communicating with the actual browser instance to an
 * implementation of the Browser interface.
 * @author lordofthepigs
 */
public class AsyncBrowserSession implements AsyncProcess {

	private final long id;
	private final Exchanger<AsyncMethod> exchanger = new Exchanger<AsyncMethod>();
	private final Browser browser;
	private volatile AsyncMethod methodUnderExecution = null;

	/**
	 * Creates a new browser session using the specified browser and id.
	 */
	public AsyncBrowserSession(Browser browser, long id) {
		this.id = id;
		this.browser = browser;
	}

	/**
	 * Returns the id of this browser session. This id is sent by the actual browser with each HTTP request, and used by the HTTP server to
	 * identify which browser he is communicating with.
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Starts the browser session. This will open a browser and navigate it to some page where the unit testing procedure can be started. The
	 * decision about exactly which browser binary is started, how it is started and which page is opened is delegated to the Browser
	 * implementation that this AsynBrowserSession was constructed with.
	 */
	@Override
	public void start() throws InitializationError {
		if (browser.getConfig().isDebugEnabled()) {
			System.out.println("Browser " + this.id + " starting (" + browser.getClass().getSimpleName() + ")");
		}
		// non-blocking, gets the browser to send the initial request to the
		// server
		this.browser.start(this);
	}

	/**
	 * Blocks until JUnit notifies this browser session that either a new test must be executed, or there are no more tests. If there is a new
	 * test to execute, then this method returns it. If there are no more tests, this method returns null.
	 * @return The next test to execute, or null if there isn't any
	 */
	public AsyncMethod awaitNewTestReady() {
		try {
			if (browser.getConfig().isDebugEnabled()) {
				System.out.println("Browser " + this.id + " is waiting for a new test");
			}
			methodUnderExecution = exchanger.exchange(null);
			if (browser.getConfig().isDebugEnabled()) {
				if (methodUnderExecution != null) {
					System.out.println("Browser " + this.id + " has picked up the test " + methodUnderExecution.getMethod().getMethod());
				} else {
					System.out.println("Browser " + this.id + " has no more tests");
				}
			}
			return methodUnderExecution;
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Executes the specified test method, possibly asynchronously. // TODO: this is the description of the previous method. Needs to be moved
	 * somewhere elseNotifies this browser that the specified test must be executed. This method blocks until this browser picks up the test by
	 * calling awaitNewTestReady().
	 * @param method The test to execute.
	 */
	public void executeTest(AsyncMethod method) {
		try {
			if (browser.getConfig().isDebugEnabled()) {
				System.out.println("Test " + method.getMethod().getMethod() + " is available for browser " + this.id);
			}
			exchanger.exchange(method);
			if (browser.getConfig().isDebugEnabled()) {
				System.out.println("Browser " + this.id + " has picked up the new test");
			}
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Notifies this browser that there are no more tests to execute. This method blocks until this browser attempts to pick up a new test by
	 * calling awaitNewTestReady().
	 */
	public void notifyNoMoreTests() {
		try {
			if (browser.getConfig().isDebugEnabled()) {
				System.out.println("Browser " + this.id + " has been notified that no more tests are coming");
			}
			exchanger.exchange(null);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the test is currently being executed by this browser.
	 */
	public AsyncMethod getMethodUnderExecution() {
		return methodUnderExecution;
	}

	/**
	 * @see Browser.sendTestFixture(AsyncMethod, HttpExchange)
	 */
	@SuppressWarnings("restriction")
	public void sendTestFixture(AsyncMethod nextMethod, HttpExchange exchange) {
		try {
			this.browser.sendTestFixture(nextMethod, this, exchange);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see Browser.sendNoMoreTestFixture(AsyncBroserSession, HttpExchange)
	 */
	@SuppressWarnings("restriction")
	public void sendNoMoreTestFixture(AsyncBrowserSession browser, HttpExchange exchange) {
		try {
			this.browser.sendNoMoreTestFixture(this, exchange);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Stops this browser session and performs cleanup operations. The details of whether or not the opened browser is actually closed is
	 * delegated to the implementation of the Browser interface that this AsyncBrowserSession was created with.
	 */
	@Override
	public void stop() {
		browser.stop();
		if (browser.getConfig().isDebugEnabled()) {
			System.out.println("Browser " + this.id + " stopped");
		}
	}

	@SuppressWarnings("restriction")
	public TestResult buildResult(Map<String, String> queryStringParameters, HttpExchange exchange) {
		return browser.buildResult(queryStringParameters, exchange);
	}

}
