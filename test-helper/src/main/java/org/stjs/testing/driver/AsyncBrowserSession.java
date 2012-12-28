package org.stjs.testing.driver;

import java.util.concurrent.Exchanger;

import com.sun.net.httpserver.HttpExchange;

public class AsyncBrowserSession {

	private final long id;
	private final Exchanger<AsyncMethod> exchanger = new Exchanger<AsyncMethod>();
	private final Browser browser;
	private volatile AsyncMethod methodUnderExecution = null;

	public AsyncBrowserSession(Browser browser, long id) {
		this.id = id;
		this.browser = browser;
	}

	public long getId() {
		return this.id;
	}

	public void start() {
		if (browser.getConfig().isDebugEnabled()) {
			System.out.println("Starting browser " + this.id);
		}
		// non-blocking, gets the browser to send the initial request to the server
		this.browser.start(this.id);
	}

	public AsyncMethod awaitNewTestReady() {
		try {
			if (browser.getConfig().isDebugEnabled()) {
				System.out.println("Browser " + this.id + " is waiting for a new test");
			}
			methodUnderExecution = exchanger.exchange(null);
			if (browser.getConfig().isDebugEnabled()) {
				System.out.println("Browser " + this.id + " has consumed the test " + methodUnderExecution.getMethod().getMethod());
			}
			return methodUnderExecution;
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void notifyNewTestReady(AsyncMethod method) {
		try {
			if (browser.getConfig().isDebugEnabled()) {
				System.out.println("Test " + method.getMethod().getMethod() + " is availble for browser " + this.id);
			}
			exchanger.exchange(method);
			if (browser.getConfig().isDebugEnabled()) {
				System.out.println("Browser " + this.id + " has returned the result of the previous test");
			}
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

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

	public AsyncMethod getMethodUnderExecution() {
		return methodUnderExecution;
	}

	@SuppressWarnings("restriction")
	public void sendTestFixture(AsyncMethod nextMethod, HttpExchange exchange) {
		try {
			this.browser.sendTestFixture(nextMethod, this, exchange);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void stop() {
		// TODO Auto-generated method stub

	}

}
