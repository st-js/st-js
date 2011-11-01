package org.stjs.testing.driver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
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
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.testing.annotation.HTMLFixture;
import org.stjs.testing.annotation.Scripts;

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

	public STJSTestDriverRunner(Class<?> klass) throws InitializationError {
		super(klass);

		DriverConfiguration config = new DriverConfiguration();
		skipIfNoBrowser = config.isSkipIfNoBrowser();
		try {
			serverSession = ServerSession.getInstance();
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
		if (script.startsWith("classpath:")) {
			writer.append("<script src='" + script.substring("classpath:/".length()) + "'></script>");
		} else if (script.startsWith("file:")) {
			writer.append("<script src='/js?" + URLEncoder.encode(script, Charset.defaultCharset().name())
					+ "'></script>");
		} else {
			writer.append("<script src='" + script + "'></script>");
		}
	}

	@Override
	protected Statement methodBlock(final FrameworkMethod method) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {

				ClassWithJavascript stjsClass = new Generator().getExistingStjsClass(getTestClass().getJavaClass());

				final HTMLFixture htmlFixture = getTestClass().getJavaClass().getAnnotation(HTMLFixture.class);
				final Scripts addedScripts = getTestClass().getJavaClass().getAnnotation(Scripts.class);
				try {
					File htmlFile = File.createTempFile(getTestClass().getJavaClass().getName()
							+ "_js_test_driver_adapter", ".html");
					FileWriter writer = new FileWriter(htmlFile);

					writer.append("<html>");
					writer.append("<head>");
					writer.append("<script src='/stjs.js'></script>");
					writer.append("<script src='/junit.js'></script>");

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
					// String methodName = "test" + method.getName();
					// String jsObjectName = testedClassName + "Adapter";
					// writer.append(jsObjectName + " = TestCase('" + jsObjectName + "');\n" + jsObjectName
					// + ".prototype." + methodName + "= function() {");

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
						writer.append(htmlFixture.value());
					}
					writer.append("</body>");
					writer.append("</html>");

					writer.flush();
					writer.close();

					System.out.println("Added source file");
					Files.copy(htmlFile, System.out);
					System.out.flush();
					TestResultCollection response = serverSession.getServer().test(htmlFile);
					if (!response.isOk()) {
						// take the first wrong result
						throw response.getResult(0).buildException(getTestClass().getJavaClass().getName(),
								method.getName());
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}
}
