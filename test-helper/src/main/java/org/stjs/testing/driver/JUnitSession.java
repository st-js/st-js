package org.stjs.testing.driver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class JUnitSession {

	private static JUnitSession instance = null;

	private DriverConfiguration config;

	private List<AsyncBrowserSession> browsers;
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

		browsers = new CopyOnWriteArrayList<AsyncBrowserSession>();
		for (int i = 0; i < config.getBrowserCount(); i++) {
			final AsyncBrowserSession browser = new AsyncBrowserSession(new PhantomjsBrowser(config), i);
			browsers.add(browser);
			serverSession.addBrowserConnection(browser);

			// let's start the browsers asynchronously. Synchronization will
			// be done later when the browser GET's and URL from the server
			new Thread(new Runnable() {
				@Override
				public void run() {
					browser.start();
				}
			}, "browser-" + i).start();
		}
	}

	private void reset() {
		config = null;
		serverSession.stop();
		serverSession = null;
		for (AsyncBrowserSession browser : browsers) {
			browser.stop();
		}
		browsers.clear();
		remainingRunners.clear();
	}

	public void runnerStarting(STJSAsyncTestDriverRunner runner) throws InitializationError {
		if (this.config == null) {
			// session not initialized yet
			init(runner.getTestClass().getJavaClass());
		}

		if (config.isDebugEnabled()) {
			System.out.println("Runner for class " + runner.getTestClass().getJavaClass().getName() + " is starting");
		}

		remainingRunners.add(runner);
	}

	public void testStarting(STJSAsyncTestDriverRunner runner, FrameworkMethod method) {
		if (config.isDebugEnabled()) {
			System.out.println("test " + method.getMethod() + " is starting");
		}
	}

	public void testCompleted(STJSAsyncTestDriverRunner runner, FrameworkMethod method) {
		if (config.isDebugEnabled()) {
			System.out.println("test " + method.getMethod() + " is completed");
		}
	}

	public void runnerCompleted(STJSAsyncTestDriverRunner runner) {
		if (config.isDebugEnabled()) {
			System.out.println("Runner for class " + runner.getTestClass().getJavaClass().getName() + " has completed");
		}
		//		this.remainingRunners.remove(runner);
		//		if (this.remainingRunners.isEmpty()) {
		//			reset();
		//		}
	}

	public DriverConfiguration getConfig() {
		return this.config;
	}

	public List<AsyncBrowserSession> getBrowsers() {
		return this.browsers;
	}

	public AsyncServerSession getServer() {
		return serverSession;
	}
}
