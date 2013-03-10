/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.testing.driver;

import java.io.File;
import java.io.IOException;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.stjs.generator.GeneratorConstants;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.testing.driver.browser.Browser;

/**
 * add the STJSBridge annotation only to allow it to be present in the junit annotation
 * 
 * @author acraciun,lordofthepigs,ekaspi
 */
@STJSBridge
public class STJSTestDriverRunner extends BlockJUnit4ClassRunner {
	public final static File targetDirectory = new File("target", GeneratorConstants.STJS_TEST_TEMP_FOLDER);

	public STJSTestDriverRunner(Class<?> klass) throws InitializationError, IOException {
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
				session.testStarting(STJSTestDriverRunner.this, method);

				if (session.getConfig().isDebugEnabled()) {
					System.out.println("Executing Statement for " + method.getMethod().toString());
				}

				MultiTestMethod aMethod = new MultiTestMethod(getTestClass(), method, session.getConfig()
						.getBrowserCount());

				for (Browser browser : session.getBrowsers()) {
					browser.executeTest(aMethod);
				}

				TestResultCollection results = aMethod.awaitExecutionResult();
				session.testCompleted(STJSTestDriverRunner.this, method, results);

				if (!results.isOk()) {
					// take the first wrong result
					throw results.buildException(session.getConfig().getClassLoader());
				}
			}
		};
	}
}
