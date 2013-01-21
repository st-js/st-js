package org.stjs.testing.driver.browser;

import java.util.Set;

import org.junit.runners.model.InitializationError;
import org.stjs.testing.driver.AsyncProcess;
import org.stjs.testing.driver.DriverConfiguration;
import org.stjs.testing.driver.MultiTestMethod;

/**
 * Handles all the details of starting and stopping a testing session as well as executing individual tests for the
 * specific browser represented by the implementation of this interface. Implementations will typically start an
 * internal HTTP server and an external browsers, instruct that browser and instruct the browser how to communicate with
 * the HTTP server.
 * 
 * @author lordofthepigs
 */
public interface Browser extends AsyncProcess {

	/**
	 * Starts the browser session. This will open a browser and navigate it to some page where the unit testing
	 * procedure can be started. The decision about exactly which browser binary is started, how it is started and which
	 * page is opened is delegated to the Browser implementation that this AsynBrowserSession was constructed with. This
	 * method is non-blocking and returns as soon as possible.
	 */
	@Override
	public void start() throws InitializationError;

	public DriverConfiguration getConfig();

	/**
	 * Executes the specified test method on this browser, possibly asynchronously.
	 * 
	 * @param method
	 *            The test to execute.
	 */
	public void executeTest(MultiTestMethod method);

	/**
	 * Notifies this browser that there are no more tests to execute.
	 */
	public void notifyNoMoreTests();

	/**
	 * Stops this browsers and performs cleanup operations, if any.
	 */
	@Override
	public void stop();

	/**
	 * Returns a list of AsyncProcess that the JUnit session must start before attempting to start this browser. If any
	 * of the dependencies fails to start, the JUnit session fails.
	 */
	public Set<Class<? extends AsyncProcess>> getSharedDependencies();
}
