package org.stjs.testing.driver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.stjs.testing.driver.browser.Browser;

/**
 * Represents one session of unit testing that may span multiple tests in multiple classes, and performs startup and cleanup actions based on the
 * JUnit lifecycle.<br>
 * <br>
 * Since JUnit itself does not provide any easy way to hook into its lifecycle, this class relies on STJSAsyncTestDriverRunner to gather hints
 * about the lifecyle. The JUnit lifecycle has been experimentally determined to be as follows:
 * <ol>
 * <li>The test classes are scanned, and one instance of Runner is created per class
 * <li>run() is called sequentially on each Runner instance. The unit tests are executed sequentially from within the run() method
 * </ol>
 * @author lordofthepigs
 */
public class JUnitSession {

	private static JUnitSession instance = null;

	private DriverConfiguration config;

	private List<AsyncBrowserSession> browserSessions;
	private AsyncServerSession serverSession;
	private Set<STJSAsyncTestDriverRunner> remainingRunners = new HashSet<STJSAsyncTestDriverRunner>();

	public static JUnitSession getInstance() {
		if (instance == null) {
			instance = new JUnitSession();
		}
		return instance;
	}

	private JUnitSession() {
		super();
		System.out.println("Creating JUnitSession");
	}

	/**
	 * Starts all the browser and server sessions. If the sessions are already started, this method has no effect.
	 * @throws InitializationError
	 */
	private void init(Class<?> testClassSample) throws InitializationError {
		System.out.println("initializing config");

		config = new DriverConfiguration(testClassSample);
		serverSession = new AsyncServerSession(config);

		browserSessions = new CopyOnWriteArrayList<AsyncBrowserSession>();
		List<Browser> browsers = config.getBrowsers();
		for (int i = 0; i < browsers.size(); i++) {
			final AsyncBrowserSession browser = new AsyncBrowserSession(browsers.get(i), i);
			browserSessions.add(browser);
			serverSession.addBrowserConnection(browser);

			// let's start the browserSessions asynchronously. Synchronization will
			// be done later when the browser GET's and URL from the server
			new Thread(new Runnable() {
				@Override
				public void run() {
					browser.start();
				}
			}, "browser-" + i).start();
		}
	}

	/**
	 * Stops the HTTP server and closes all the browsers.
	 */
	private void reset() {
		config = null;
		for (AsyncBrowserSession browser : browserSessions) {
			browser.notifyNoMoreTests();
			browser.stop();
		}
		browserSessions.clear();
		remainingRunners.clear();
		serverSession.stop();
		serverSession = null;
	}

	/**
	 * Called when a runner has been instantiated. This method is expected to be called many times in a row right after the session has started,
	 * once per STJSAsyncTestDriverRunner. The first time this method is called, the HTTP server and the browsers are configured and started.
	 * This method counts the number of times it has been called.
	 */
	public void runnerInstantiated(STJSAsyncTestDriverRunner runner) throws InitializationError {
		if (this.config == null) {
			// session not initialized yet
			init(runner.getTestClass().getJavaClass());
		}

		if (config.isDebugEnabled()) {
			System.out.println("Runner for class " + runner.getTestClass().getJavaClass().getName() + " is starting");
		}

		remainingRunners.add(runner);
	}

	/**
	 * Called before a JUnit test is dispatched to the browsers.
	 */
	public void testStarting(STJSAsyncTestDriverRunner runner, FrameworkMethod method) {
		if (config.isDebugEnabled()) {
			System.out.println("test " + method.getMethod() + " is starting");
		}
	}

	/**
	 * Called after a JUnit test has been executed by all the browsers
	 */
	public void testCompleted(STJSAsyncTestDriverRunner runner, FrameworkMethod method, TestResultCollection result) {
		if (config.isDebugEnabled()) {
			System.out.println("test " + method.getMethod() + " is completed");
		}
	}

	/**
	 * Called when the specified runner has finished executing all of its tests. When this method is called for the last time, the HTTP server
	 * and all browsers are stopped and cleaned up. This method can figure out when it i being invoked for the last time by comparing the number
	 * of times it was invoked with the number of times runnerInstantiated() was invoked.
	 */
	public void runnerCompleted(STJSAsyncTestDriverRunner runner) {
		if (config.isDebugEnabled()) {
			System.out.println("Runner for class " + runner.getTestClass().getJavaClass().getName() + " has completed");
		}
		this.remainingRunners.remove(runner);
		if (this.remainingRunners.isEmpty()) {
			reset();
		}
	}

	public DriverConfiguration getConfig() {
		return this.config;
	}

	public List<AsyncBrowserSession> getBrowsers() {
		return this.browserSessions;
	}

	public AsyncServerSession getServer() {
		return serverSession;
	}
}
