package org.stjs.testing.driver;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.stjs.javascript.annotation.STJSBridge;

@STJSBridge
public class STJSAsyncTestDriverRunner extends BlockJUnit4ClassRunner {

	private final DriverConfiguration config;
	private final List<AsyncBrowserSession> browsers;
	private final AsyncServerSession serverSession;

	public STJSAsyncTestDriverRunner(Class<?> klass) throws InitializationError, IOException {
		super(klass);

		config = new DriverConfiguration(klass);

		if (config.isDebugEnabled()) {
			System.out.println("Creating runner for " + klass);
		}

		serverSession = AsyncServerSession.getInstance(config);

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

	@Override
	protected Statement methodBlock(final FrameworkMethod method) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				if (config.isDebugEnabled()) {
					System.out.println("Executing Statement for " + method.getMethod().toString());
				}
				AsyncMethod aMethod = new AsyncMethod(getTestClass(), method, config.getBrowserCount());

				for (AsyncBrowserSession browser : browsers) {
					serverSession.queueTest(aMethod, browser);
				}

				aMethod.awaitExecutionResult();
			}
		};
	}
}
