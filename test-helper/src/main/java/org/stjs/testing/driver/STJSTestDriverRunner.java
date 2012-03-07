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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollection;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.utils.ClassUtils;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.testing.annotation.HTMLFixture;
import org.stjs.testing.annotation.Scripts;

import com.google.common.base.Strings;
import com.google.common.io.Files;

/**
 * add the STJSBridge annotation only to allow it to be present in the junit annotation
 * 
 * @author acraciun
 * 
 */
@STJSBridge
public class STJSTestDriverRunner extends BlockJUnit4ClassRunner {
	private boolean skipIfNoBrowser = false;
	private final ServerSession serverSession;
	private final DriverConfiguration config;
	public final static File targetDirectory = new File("target", GeneratorConstants.STJS_TEST_TEMP_FOLDER);

	public STJSTestDriverRunner(Class<?> klass) throws InitializationError {
		super(klass);

		config = new DriverConfiguration(klass);
		skipIfNoBrowser = config.isSkipIfNoBrowser();
		try {
			serverSession = ServerSession.getInstance(config);
		} catch (Exception e) {
			throw new InitializationError(e);
		}
	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		if (!serverSession.hasBrowsers()) {
			if (skipIfNoBrowser) {
				EachTestNotifier eachNotifier = makeNotifier(method, notifier);
				eachNotifier.fireTestIgnored();
			} else {
				notifier.fireTestFailure(new Failure(describeChild(method), new IllegalStateException(
						"No connected browser")));
			}
			return;
		}
		super.runChild(method, notifier);
	}

	private EachTestNotifier makeNotifier(FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		return new EachTestNotifier(notifier, description);
	}

	private void addScript(Writer writer, String script) throws IOException {
		// remove wrong leading classpath://
		String cleanScript = script.replace("classpath://", "/");
		// add a slash to prevent the browser to interpret the scheme
		writer.append("<script src='" + cleanScript + "'></script>\n");
	}

	@Override
	protected Statement methodBlock(final FrameworkMethod method) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {

				System.out.println("STREAM:"
						+ config.getClassLoader().getResourceAsStream(
								ClassUtils.getPropertiesFileName(getTestClass().getJavaClass().getName())));
				ClassWithJavascript stjsClass = new Generator().getExistingStjsClass(config.getClassLoader(),
						getTestClass().getJavaClass());

				final HTMLFixture htmlFixture = getTestClass().getJavaClass().getAnnotation(HTMLFixture.class);
				final Scripts addedScripts = getTestClass().getJavaClass().getAnnotation(Scripts.class);
				File htmlFile = null;
				try {
					targetDirectory.mkdirs();
					htmlFile = new File(targetDirectory, getTestClass().getJavaClass().getName() + "-"
							+ method.getName() + ".html");
					FileWriter writer = new FileWriter(htmlFile);

					writer.append("<html>");
					writer.append("<head>");
					addScript(writer, "/stjs.js");
					addScript(writer, "/junit.js");

					writer.append("<script language='javascript'>stjs.mainCallDisabled=true;</script>");
					if (addedScripts != null) {
						for (String script : addedScripts.value()) {
							addScript(writer, script);
						}
					}

					Set<URI> jsFiles = new LinkedHashSet<URI>();
					for (ClassWithJavascript dep : new DependencyCollection(stjsClass).orderAllDependencies()) {
						for (URI file : dep.getJavascriptFiles()) {
							jsFiles.add(file);
						}
					}

					for (URI file : jsFiles) {
						addScript(writer, file.toString());
					}
					writer.append("<script language='javascript'>");
					writer.append("onload=function(){");

					// Adapter between generated assert (not global) and JS-test-driver assert (which is a
					// set of global methods)
					writer.append("Assert=window;");

					String testedClassName = getTestClass().getJavaClass().getSimpleName();

					writer.append("try{");
					writer.append("new " + testedClassName + "()." + method.getName() + "();");
					writer.append("parent.sendOK('" + testedClassName + "." + method.getName() + "');");
					writer.append("}catch(ex){");
					writer.append("parent.sendError('" + testedClassName + "." + method.getName() + "', ex);");
					writer.append("}");
					writer.append("}");
					writer.append("</script>");
					writer.append("</head>");
					writer.append("<body>");
					if (htmlFixture != null) {
						if (!Strings.isNullOrEmpty(htmlFixture.value())) {
							writer.append(htmlFixture.value());
						} else if (!Strings.isNullOrEmpty(htmlFixture.url())) {
							StreamUtils.copy(serverSession.getServer().getClassLoader(), htmlFixture.url(), writer);
						}
					}
					writer.append("</body>");
					writer.append("</html>");

					writer.flush();
					writer.close();

					if (config.isDebugEnabled()) {
						System.out.println("Added source file");
						Files.copy(htmlFile, System.out);
						System.out.flush();
					}
					TestResultCollection response = serverSession.getServer().test(htmlFile, testedClassName,
							method.getName());
					if (!response.isOk()) {
						// take the first wrong result
						throw response.buildException(getTestClass().getJavaClass().getName(), method.getName());
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				} finally {
					// if (htmlFile != null) {
					// htmlFile.delete();
					// }
				}
			}
		};
	}
}
