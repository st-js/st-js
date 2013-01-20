package org.stjs.testing.driver;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.stjs.testing.driver.browser.Browser;

/**
 * Represents one session of unit testing that may span multiple tests in multiple classes, and performs startup and
 * cleanup actions based on the JUnit lifecycle.<br>
 * <br>
 * Since JUnit itself does not provide any easy way to hook into its lifecycle, this class relies on
 * STJSMultiTestDriverRunner to gather hints about the lifecyle. The JUnit lifecycle has been experimentally determined
 * to be as follows:
 * <ol>
 * <li>The test classes are scanned, and one instance of Runner is created per class
 * <li>run() is called sequentially on each Runner instance. The unit tests are executed sequentially from within the
 * run() method
 * </ol>
 * 
 * @author lordofthepigs
 */
public class JUnitSession {

	private static JUnitSession instance = null;

	private DriverConfiguration config;
	private volatile boolean initFailed = false;

	private List<Browser> browsers;
	private Set<STJSMultiTestDriverRunner> remainingRunners = new HashSet<STJSMultiTestDriverRunner>();
	private HashMap<Class<? extends AsyncProcess>, AsyncProcess> sharedDependencies = new HashMap<Class<? extends AsyncProcess>, AsyncProcess>();

	public static JUnitSession getInstance() {
		if (instance == null) {
			instance = new JUnitSession();
		}
		return instance;
	}

	private JUnitSession() {
		super();
	}

	/**
	 * Starts all the browser and server sessions. If the sessions are already started, this method has no effect.
	 * 
	 * @throws InitializationError
	 */
	private void init(Class<?> testClassSample) throws InitializationError {
		if (config != null) {
			// init has already been called, nothing to do
			return;
		}

		if (this.initFailed) {
			throw new InitializationError("Session initialization failed previously. Not trying again.");
		}

		try {
			config = new DriverConfiguration(testClassSample);

			// initialize the browser sessions
			initBrowsers();
			initBrowserDependencies();
			startBrowserDependencies();
			startBrowsers();

		} catch (Throwable e) {
			printStackTrace(e);
			reset();
			this.initFailed = true;
			// sometimes, JUnit doesn't display the exception, so let's print it out
			throw new InitializationError(e);
		}
	}

	private void initBrowsers() {
		browsers = new ArrayList<Browser>(config.getBrowsers());
	}

	private void initBrowserDependencies() throws InitializationError {
		// Collect the dependencies of all browsers
		Set<Class<? extends AsyncProcess>> deps = new HashSet<Class<? extends AsyncProcess>>();
		for (Browser browser : config.getBrowsers()) {
			deps.addAll(browser.getSharedDependencies());
		}

		// Init all the dependencies
		for (Class<? extends AsyncProcess> dep : deps) {
			sharedDependencies.put(dep, initBrowserDependency(dep));
		}
	}

	private AsyncProcess initBrowserDependency(Class<? extends AsyncProcess> dep) throws InitializationError {

		Constructor<? extends AsyncProcess> cons;
		try {
			cons = dep.getConstructor(DriverConfiguration.class);
			return cons.newInstance(config);
		} catch (NoSuchMethodException e) {
			// constructor with single DriverConfiguration argument cannot be found,
			// try with the no-args constructor
			try {
				cons = dep.getConstructor();
				return cons.newInstance();
			} catch (Exception e2) {
				// proper constructor not found (or something else happened),
				// cannot instantiate dependency
				throw new InitializationError(e2);
			}
		} catch (Exception e) {
			throw new InitializationError(e);
		}
	}

	private void startBrowserDependencies() throws InitializationError {
		startInParallel(this.sharedDependencies.values());
	}

	private void startBrowsers() throws InitializationError {
		startInParallel(this.browsers);
	}

	private void startInParallel(Collection<? extends AsyncProcess> processes) throws InitializationError {
		List<Thread> threads = new ArrayList<Thread>();
		final List<Throwable> errors = new CopyOnWriteArrayList<Throwable>();

		// start all the dependencies in parallel
		for (final AsyncProcess proc : processes) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						proc.start();
					} catch (Exception e) {
						errors.add(e);
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
			} catch (InterruptedException e) {
				throw new InitializationError(e);
			}
		}

		// check if any of the dependencies has failed to start. If some did, throw an exception
		if (!errors.isEmpty()) {
			throw new InitializationError(errors);
		}
	}

	/**
	 * Stops the HTTP server and closes all the browsers.
	 */
	private void reset() {
		config = null;
		remainingRunners.clear();

		for (Browser browser : browsers) {
			try {
				browser.notifyNoMoreTests();
				browser.stop();
			} catch (Throwable e) {
				printStackTrace(e);
			}
		}
		browsers.clear();

		for (AsyncProcess dep : sharedDependencies.values()) {
			try {
				dep.stop();
			} catch (Throwable e) {
				printStackTrace(e);
			}
		}
		sharedDependencies.clear();
	}

	/**
	 * Called when a runner has been instantiated. This method is expected to be called many times in a row right after
	 * the session has started, once per STJSMultiTestDriverRunner. The first time this method is called, the HTTP
	 * server and the browsers are configured and started. This method counts the number of times it has been called.
	 */
	public void runnerInstantiated(STJSMultiTestDriverRunner runner) throws InitializationError {
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
	public void testStarting(STJSMultiTestDriverRunner runner, FrameworkMethod method) {
		if (config.isDebugEnabled()) {
			System.out.println("test " + method.getMethod() + " is starting");
		}
	}

	/**
	 * Called after a JUnit test has been executed by all the browsers
	 */
	public void testCompleted(STJSMultiTestDriverRunner runner, FrameworkMethod method, TestResultCollection result) {
		if (config.isDebugEnabled()) {
			System.out.println("test " + method.getMethod() + " is completed");
		}
	}

	/**
	 * Called when the specified runner has finished executing all of its tests. When this method is called for the last
	 * time, the HTTP server and all browsers are stopped and cleaned up. This method can figure out when it i being
	 * invoked for the last time by comparing the number of times it was invoked with the number of times
	 * runnerInstantiated() was invoked.
	 */
	public void runnerCompleted(STJSMultiTestDriverRunner runner) {
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

	public List<Browser> getBrowsers() {
		return this.browsers;
	}

	@SuppressWarnings("unchecked")
	public <T> T getDependency(Class<T> depencencyType) {
		return (T) this.sharedDependencies.get(depencencyType);
	}

	/**
	 * Prints the stack trace of the specified throwable. This method is required because JUnits InitializationError
	 * makes it pretty much impossible to print nice looking stack traces using the standard printStackTrace() method
	 * 
	 * @param t
	 */
	private static void printStackTrace(Throwable t) {
		printStackTrace(t, System.err);
	}

	private static void printStackTrace(Throwable t, PrintStream out) {
		if (t instanceof InitializationError) {
			out.println(t);

		} else {
			// "Normal" Throwable, code is pretty much equivalent to Throwable.printStackTrace();
			out.println(t);
			for (StackTraceElement elem : t.getStackTrace()) {
				out.println("\tat " + elem);
			}
		}
		printCauses(t, "", out);
	}

	private static void printCauses(Throwable caused, String prefix, PrintStream out) {
		if (caused instanceof InitializationError) {
			// Initialization error does not use the standard way to report causes, and therefore
			// printStackTrace() on InitializationError does not print causes. Let's try to fix that
			InitializationError ie = (InitializationError) caused;
			List<Throwable> causes = ie.getCauses();
			if (causes != null && !causes.isEmpty()) {
				System.err.println(prefix + "Caused by " + causes.size() + " exceptions:");
				for (Throwable cause : causes) {
					printCauseStackTrace(cause, caused.getStackTrace(), "\t" + prefix, out);
				}
			}

		} else if (caused.getCause() != null) {
			printCauseStackTrace(caused.getCause(), caused.getStackTrace(), prefix, out);
		}
	}

	private static void printCauseStackTrace(Throwable cause, StackTraceElement[] causedTrace, String prefix,
			PrintStream out) {
		// Compute number of frames in common between cause and caused
		StackTraceElement[] trace = cause.getStackTrace();
		int m = trace.length - 1, n = causedTrace.length - 1;
		while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
			m--;
			n--;
		}
		int framesInCommon = trace.length - 1 - m;

		out.println(prefix + "Caused by: " + cause);
		for (int i = 0; i <= m; i++) {
			out.println(prefix + "\tat " + trace[i]);
		}
		if (framesInCommon != 0) {
			out.println(prefix + "\t... " + framesInCommon + " more");
		}

		printCauses(cause, prefix, out);
	}

}
