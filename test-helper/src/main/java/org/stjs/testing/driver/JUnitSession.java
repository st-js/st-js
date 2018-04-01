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
 * STJSTestDriverRunner to gather hints about the lifecyle. The lifecycle of the JUnitSession is the following:<ol>
 * <li>It starts when the first STJSTestDriverRunner is instantiated</li>
 * <li>It ends when the JVM shuts down.</li>
 * </ol>
 * This lifecycle has been successfully tested again both Eclipses and maven-surefire-plugins test harnesses.
 * @author lordofthepigs
 */
public class JUnitSession {

	private static JUnitSession instance = null;

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
	 * Called when a runner has been instantiated. This method is expected to be called many times in a row right after
	 * the session has started, once per STJSTestDriverRunner. The first time this method is called, the HTTP
	 * server and the browsers are configured and started. This method counts the number of times it has been called.
	 */
	public void runnerInstantiated(STJSTestDriverRunner runner) throws InitializationError {
		System.out.println("Runner for class " + runner.getTestClass().getJavaClass().getName() + " is starting");
	}

	/**
	 * Called before a JUnit test is dispatched to the browsers.
	 */
	public void testStarting(STJSTestDriverRunner runner, FrameworkMethod method) {
		System.out.println("test " + method.getMethod() + " is starting");
	}

	/**
	 * Called after a JUnit test has been executed by all the browsers
	 */
	public void testCompleted(STJSTestDriverRunner runner, FrameworkMethod method, TestResultCollection result) {
		System.out.println("test " + method.getMethod() + " is completed");
	}

	/**
	 * Called when the specified runner has finished executing all of its tests.
	 */
	public void runnerCompleted(STJSTestDriverRunner runner) {
		System.out.println("Runner for class " + runner.getTestClass().getJavaClass().getName() + " has completed");
	}

	public DriverConfiguration getConfig() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getDependency(Class<T> depencencyType) {
		return (T) this.sharedDependencies.get(depencencyType);
	}

}
