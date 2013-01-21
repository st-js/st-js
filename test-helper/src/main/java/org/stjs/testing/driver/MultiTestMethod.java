package org.stjs.testing.driver;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.stjs.testing.JSException;

/**
 * Coordinates the execution of one unit test across several browsers. The JUnit runner creates one instance of this
 * class for each unit test, specifying how many browsers are expected to execute this test. The JUnit runner then sends
 * this test to all browsers and waits for all browsers to return the execution results by calling
 * awaitExecutionresult(). When a browser has finished executing a test, notifiyExecutionResult() is called.
 * 
 * @author lordofthepigs
 */
public class MultiTestMethod {

	private final TestClass testClass;
	private final FrameworkMethod meth;
	private final CountDownLatch latch;
	private final TestResultCollection results;

	/**
	 * Creates a new MultiTestMethod that reprents the specified unit test of the specified class, executed on the
	 * specified number of browsers.
	 * 
	 * @param nBrowsers
	 */
	public MultiTestMethod(TestClass testClass, FrameworkMethod meth, int nBrowsers) {
		// Do some validation
		Class<? extends Throwable> expectedExceptionType = meth.getAnnotation(Test.class).expected();
		if (!expectedExceptionType.equals(Test.None.class) && !expectedExceptionType.equals(JSException.class)) {
			throw new IllegalStateException( //
					"The only valid value for the @Test.expected annotation field is JSException.class.\n" + /**/
					"This is caused by the fact that STJS cannot currently handle typed exceptions.\n" + /**/
					"Please replace the current value with JSException.class, or do your own error handling.");
		}
		this.latch = new CountDownLatch(nBrowsers);
		this.meth = meth;
		this.testClass = testClass;
		this.results = new TestResultCollection(testClass.getName(), meth.getName());
	}

	/**
	 * Called by the browsers when they have finished executing their unit test. This method is non-blocking.
	 */
	public void notifyExecutionResult(TestResult result) {
		results.addResult(result);
		latch.countDown();
	}

	/**
	 * Blocks until all browsers have called notifyExecutionResult() and returns a TestResultCollection containing all
	 * the results from all the browsers.
	 */
	public TestResultCollection awaitExecutionResult() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return this.results;
	}

	public FrameworkMethod getMethod() {
		return this.meth;
	}

	public TestClass getTestClass() {
		return this.testClass;
	}

	@Override
	public String toString() {
		return "MultiTestMethod [method=" + meth.getMethod() + "]";
	}
}
