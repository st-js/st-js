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

	public long getId(){
		return this.id;
	}

	public void start(){
		if(browser.getConfig().isDebugEnabled()){
			System.out.println("Starting browser " + this.id);
		}
		// non-blocking, gets the browser to send the initial request to the server
		this.browser.start(this.id);
	}

	public AsyncMethod awaitNewTestReady(){
		try {
			if(browser.getConfig().isDebugEnabled()){
				System.out.println("Browser " + this.id + " is waiting for a new test");
			}
			methodUnderExecution = exchanger.exchange(null);
			if(browser.getConfig().isDebugEnabled()){
				System.out.println("Browser " + this.id + " now has his new test");
			}
			return methodUnderExecution;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void notifyNewTestReady(AsyncMethod method){
		try {
			if(browser.getConfig().isDebugEnabled()){
				System.out.println("Browser " + this.id + " has been notified that a new test is available");
			}
			exchanger.exchange(method);
			if(browser.getConfig().isDebugEnabled()){
				System.out.println("Browser " + this.id + " has consumed the new test");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void notifyNoMoreTests(){
		try {
			if(browser.getConfig().isDebugEnabled()){
				System.out.println("Browser " + this.id + " has been notified that no more tests are coming");
			}
			exchanger.exchange(null);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public AsyncMethod getMethodUnderExecution(){
		return methodUnderExecution;
	}

	@SuppressWarnings("restriction")
	public void sendTestFixture(AsyncMethod nextMethod, HttpExchange exchange) {
		try {
			this.browser.sendTestFixture(nextMethod, exchange);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//	public void executeMethod(FrameworkMethod method) {
	//		// blocks the current thread until the method under has finished executing
	//		// and the browser has returned the result of the execution
	//		try {
	//			doEnqueueMethod(method);
	//		} catch (InterruptedException e) {
	//			throw new RuntimeException(e);
	//		}
	//	}
	//
	//	private void doEnqueueMethod(FrameworkMethod method) throws InterruptedException{
	//		// Blocks the execution of the current thread until
	//		methods.put(method);
	//	}

	//	public void onTestRequested(){
	//		// blocks until a new test is available, or the server is stopped
	//	}
	//
	//	public void onClassStart(){
	//
	//	}

}
