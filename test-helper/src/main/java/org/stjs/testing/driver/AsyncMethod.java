package org.stjs.testing.driver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * Coordinates the execution of one unit test across several browsers. The JUnit runner creates one instance of this class for each unit test,
 * specifying how many browsers are expected to execute this test. The JUnit runner then sends this test to all browsers and waits for all
 * browsers to return the execution results by calling awaitExecutionresult(). When a browser has finished executing a test,
 * notifiyExecutionResult() is called.
 * @author lordofthepigs
 */
public class AsyncMethod {

	private final TestClass testClass;
	private final FrameworkMethod meth;
	private final CountDownLatch latch;
	private final AtomicReference<Throwable> firstError = new AtomicReference<Throwable>();

	/**
	 * Creates a new AsyncMethod that reprents the specified unit test of the specified class, executed on the specified number of browsers.
	 * @param nBrowsers
	 */
	public AsyncMethod(TestClass testClass, FrameworkMethod meth, int nBrowsers) {
		latch = new CountDownLatch(nBrowsers);
		this.meth = meth;
		this.testClass = testClass;
	}

	/**
	 * Called by the browsers when they have finished executing their unit test. This method is non-blocking.
	 */
	public void notifyExecutionResult(Throwable throwable) {
		firstError.compareAndSet(null, throwable);
		latch.countDown();
	}

	/**
	 * Blocks until all browsers have called notifyExecutionResult().
	 */
	public void awaitExecutionResult() {
		try {
			latch.await();
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		if (firstError.get() != null) {
			throw new RuntimeException("One of the browsers failed", firstError.get());
		}
	}

	public FrameworkMethod getMethod() {
		return this.meth;
	}

	public TestClass getTestClass() {
		return this.testClass;
	}
}
