package org.stjs.testing.driver;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

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
	private Set<SharedExternalProcess> dependentProcesses = new HashSet<SharedExternalProcess>();

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
		if (config != null) {
			// init has already been called, nothing to do
			return;
		}
		System.out.println("initializing config");
		config = new DriverConfiguration(testClassSample);

		// initialize the browser sessions
		initBrowserSessions();
		initBrowserDependencies();
		startBrowserDependencies();
		startBrowserSessions();
	}

	private void initBrowserSessions() {
		browserSessions = new CopyOnWriteArrayList<AsyncBrowserSession>();
		List<Browser> browsers = config.getBrowsers();
		for (int i = 0; i < browsers.size(); i++) {
			browserSessions.add(new AsyncBrowserSession(browsers.get(i), i));
		}
	}

	private void initBrowserDependencies() throws InitializationError {
		// Collect the dependencies of all browsers
		Set<Class<? extends SharedExternalProcess>> deps = new HashSet<Class<? extends SharedExternalProcess>>();
		for (Browser browser : config.getBrowsers()) {
			deps.addAll(browser.getExternalProcessDependencies());
		}

		// Init the HTTP server
		// TODO: this should be a dependency like any other
		serverSession = new AsyncServerSession(config);

		// Init all the dependencies
		for (Class<? extends SharedExternalProcess> dep : deps) {
			dependentProcesses.add(initBrowserDependency(dep));
		}
	}

	private SharedExternalProcess initBrowserDependency(Class<? extends SharedExternalProcess> dep) throws InitializationError {

		Constructor<? extends SharedExternalProcess> cons;
		try {
			cons = dep.getConstructor(DriverConfiguration.class);
			return cons.newInstance(config);
		}
		catch (NoSuchMethodException e) {
			// constructor with single DriverConfiguration argument cannot be found,
			// try with the no-args constructor
			try {
				cons = dep.getConstructor();
				return cons.newInstance();
			}
			catch (Exception e2) {
				// proper constructor not found (or something else happened), 
				// cannot instantiate dependency
				throw new InitializationError(e2);
			}
		}
		catch (Exception e) {
			throw new InitializationError(e);
		}
	}

	private void startBrowserDependencies() throws InitializationError {
		List<AsyncProcess> allDeps = new ArrayList<AsyncProcess>(this.dependentProcesses);
		allDeps.add(serverSession);
		startInParallel(allDeps);
	}

	private void startBrowserSessions() throws InitializationError {
		startInParallel(this.browserSessions);
	}

	private void startInParallel(Collection<? extends AsyncProcess> processes) throws InitializationError {
		List<Thread> threads = new ArrayList<Thread>();
		final AtomicReference<Throwable> firstError = new AtomicReference<Throwable>();

		// start all the dependencies in parallel
		for (final AsyncProcess proc : processes) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						proc.start();
						if (proc instanceof SharedExternalProcess) {
							for (AsyncBrowserSession browserSession : browserSessions) {
								((SharedExternalProcess) proc).addBrowserSession(browserSession);
							}
						}
					}
					catch (Exception e) {
						firstError.compareAndSet(null, e);
					}
				}
			});
			t.start();
			threads.add(t);
		}

		// wait until all the started threads have completed, and therefore that all of the
		// dependencies have started (or failed to start)
		for (Thread t : threads) {
			try {
				t.join();
			}
			catch (InterruptedException e) {
				throw new InitializationError(e);
			}
		}

		// check if any of the dependencies has failed to start. If some did, throw an exception
		if (firstError.get() != null) {
			throw new InitializationError(firstError.get());
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
		for (SharedExternalProcess p : dependentProcesses) {
			p.stop();
		}
		dependentProcesses.clear();
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
