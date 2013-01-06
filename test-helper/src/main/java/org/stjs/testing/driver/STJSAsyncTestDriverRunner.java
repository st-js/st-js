package org.stjs.testing.driver;

import java.io.IOException;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.stjs.javascript.annotation.STJSBridge;

@STJSBridge
public class STJSAsyncTestDriverRunner extends BlockJUnit4ClassRunner {

	public STJSAsyncTestDriverRunner(Class<?> klass) throws InitializationError, IOException {
		super(klass);
		JUnitSession.getInstance().runnerInstantiated(this);
	}

	@Override
	public void run(RunNotifier notifier) {
		super.run(notifier);
		JUnitSession.getInstance().runnerCompleted(this);
	}

	@Override
	protected Statement methodBlock(final FrameworkMethod method) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				JUnitSession session = JUnitSession.getInstance();
				session.testStarting(STJSAsyncTestDriverRunner.this, method);

				if (session.getConfig().isDebugEnabled()) {
					System.out.println("Executing Statement for " + method.getMethod().toString());
				}

				AsyncMethod aMethod = new AsyncMethod(getTestClass(), method, session.getConfig().getBrowserCount());

				for (AsyncBrowserSession browserSession : session.getBrowserSessions()) {
					browserSession.executeTest(aMethod);
				}

				TestResultCollection results = aMethod.awaitExecutionResult();
				session.testCompleted(STJSAsyncTestDriverRunner.this, method, results);

				if (!results.isOk()) {
					// take the first wrong result
					throw results.buildException();
				}
			}
		};
	}
}
