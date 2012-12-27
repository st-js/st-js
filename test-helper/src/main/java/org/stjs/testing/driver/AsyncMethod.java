package org.stjs.testing.driver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public class AsyncMethod {

	private final TestClass testClass;
	private final FrameworkMethod meth;
	private final CountDownLatch latch;
	private final AtomicReference<Throwable> firstError = new AtomicReference<Throwable>();

	public AsyncMethod(TestClass testClass, FrameworkMethod meth, int nBrowsers){
		latch = new CountDownLatch(nBrowsers);
		this.meth = meth;
		this.testClass = testClass;
	}

	public void notifyExecutionResult(Throwable throwable){
		firstError.compareAndSet(null, throwable);
		latch.countDown();
	}

	public void awaitExecutionResult(){
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		if(firstError.get() != null){
			throw new RuntimeException("One of the browsers failed", firstError.get());
		}
	}

	public FrameworkMethod getMethod(){
		return this.meth;
	}

	public TestClass getTestClass(){
		return this.testClass;
	}
}
