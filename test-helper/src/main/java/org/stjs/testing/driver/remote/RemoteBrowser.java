package org.stjs.testing.driver.remote;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.runners.model.FrameworkMethod;
import org.stjs.generator.BridgeClass;
import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollection;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConstants;
import org.stjs.testing.annotation.HTMLFixture;
import org.stjs.testing.annotation.Scripts;
import org.stjs.testing.annotation.ScriptsAfter;
import org.stjs.testing.annotation.ScriptsBefore;
import org.stjs.testing.driver.AsyncBrowserSession;
import org.stjs.testing.driver.AsyncMethod;
import org.stjs.testing.driver.Browser;
import org.stjs.testing.driver.DriverConfiguration;
import org.stjs.testing.driver.StreamUtils;
import org.stjs.testing.driver.TestResult;

import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.sun.net.httpserver.HttpExchange;

/**
 *
 * @author acraciun
 *
 */
@SuppressWarnings({ "restriction" /* for HttpExchange */, "deprecation" /* for @Scripts */})
public class RemoteBrowser implements Browser {
	public final static File targetDirectory = new File("target", GeneratorConstants.STJS_TEST_TEMP_FOLDER);

	private final DriverConfiguration config;
	/**
	 * timeout to wait for all the connected clients
	 */
	private int timeout;
	private boolean hasBrowsers;
	private final int browserCount;
	private final boolean startBrowser;

	public RemoteBrowser(DriverConfiguration config) {
		this.config = config;
		timeout = config.getWaitForBrowser() * 1000;
		startBrowser = config.isStartBrowser();
		browserCount = config.getBrowserCount();
	}

	@Override
	public void start(long browserId) {
		hasBrowsers = checkBrowsers();
	}

	private boolean checkBrowsers() {
		/*
		// wait for any previously open browser to connect
		final int initialTimeout = 2000;
		final int stepTimeout = 500;

		System.out.println("Waiting for " + browserCount + " browser(s)");
		try {
			Thread.sleep(Math.min(initialTimeout, timeout));
			if (startBrowser && Desktop.isDesktopSupported() && (server.getBrowserCount() == 0)) {
				System.out.println("Starting the default browser ...");
				Desktop.getDesktop().browse(new URL(server.getHostURL(), "/start.html").toURI());
			}

			for (int i = initialTimeout; i < timeout; i += stepTimeout) {
				Thread.sleep(stepTimeout);
				if (server.getBrowserCount() >= browserCount) {
					System.out.println("Captured browsers");
					return true;
				}
			}
			System.err.println("Unable to capture at least " + browserCount + " browser(s)");
			return false;
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		System.out.println("Have a browsers connected");
		*/
		return true;
	}

	@Override
	public DriverConfiguration getConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendTestFixture(AsyncMethod meth, AsyncBrowserSession browserSession, HttpExchange exchange)
			throws IOException, URISyntaxException {
		Class<?> testClass = meth.getTestClass().getJavaClass();
		Method method = meth.getMethod().getMethod();
		ClassWithJavascript stjsClass = new Generator().getExistingStjsClass(config.getClassLoader(), testClass);

		List<FrameworkMethod> beforeMethods = meth.getTestClass().getAnnotatedMethods(Before.class);
		List<FrameworkMethod> afterMethods = meth.getTestClass().getAnnotatedMethods(After.class);

		final HTMLFixture htmlFixture = testClass.getAnnotation(HTMLFixture.class);

		final Scripts addedScripts = testClass.getAnnotation(Scripts.class);
		final ScriptsBefore addedScriptsBefore = testClass.getAnnotation(ScriptsBefore.class);
		final ScriptsAfter addedScriptsAfter = testClass.getAnnotation(ScriptsAfter.class);
		File htmlFile = null;
		FileWriter writer = null;
		try {
			targetDirectory.mkdirs();
			htmlFile = new File(targetDirectory, testClass.getName() + "-" + method.getName() + ".html");
			writer = new FileWriter(htmlFile);

			writer.append("<html>");
			writer.append("<head>");
			addScript(writer, "/stjs.js");
			addScript(writer, "/junit.js");

			writer.append("<script language='javascript'>stjs.mainCallDisabled=true;</script>\n");

			// scripts added explicitly
			if (addedScripts != null) {
				for (String script : addedScripts.value()) {
					addScript(writer, script);
				}
			}
			// scripts before - new style
			if (addedScriptsBefore != null) {
				for (String script : addedScriptsBefore.value()) {
					addScript(writer, script);
				}
			}

			Set<URI> jsFiles = new LinkedHashSet<URI>();
			for (ClassWithJavascript dep : new DependencyCollection(stjsClass).orderAllDependencies(config
					.getClassLoader())) {

				if (addedScripts != null && dep instanceof BridgeClass) {
					// bridge dependencies are not added when using @Scripts
					System.out
							.println("WARNING: You're using @Scripts deprecated annotation that disables the automatic inclusion of the Javascript files of the bridges you're using! "
									+ "Please consider using @ScriptsBefore and/or @ScriptsAfter instead.");
					continue;
				}
				for (URI file : dep.getJavascriptFiles()) {
					jsFiles.add(file);
				}
			}

			for (URI file : jsFiles) {
				addScript(writer, file.toString());
			}

			// scripts after - new style
			if (addedScriptsAfter != null) {
				for (String script : addedScriptsAfter.value()) {
					addScript(writer, script);
				}
			}
			writer.append("<script language='javascript'>");
			writer.append("onload=function(){");

			// Adapter between generated assert (not global) and JS-test-driver assert (which is a
			// set of global methods)
			writer.append("Assert=window;\n");

			String testedClassName = testClass.getSimpleName();

			writer.append("var stjsTest = new " + testedClassName + "();\n");
			writer.append("try{\n");
			// call before methods
			for (FrameworkMethod beforeMethod : beforeMethods) {
				writer.append("stjsTest." + beforeMethod.getName() + "();\n");
			}
			// call the test's method
			writer.append("stjsTest." + method.getName() + "();\n");
			writer.append("parent.sendOK('" + testedClassName + "." + method.getName() + "');\n");
			writer.append("}catch(ex){\n");
			writer.append("parent.sendError('" + testedClassName + "." + method.getName() + "', ex);\n");
			writer.append("} finally{\n");
			// call after methods
			for (FrameworkMethod afterMethod : afterMethods) {
				writer.append("stjsTest." + afterMethod.getName() + "();\n");
			}
			writer.append("}\n");
			writer.append("}\n");
			writer.append("</script>");
			writer.append("</head>");
			writer.append("<body>");
			if (htmlFixture != null) {
				if (!Strings.isNullOrEmpty(htmlFixture.value())) {
					writer.append(htmlFixture.value());
				} else if (!Strings.isNullOrEmpty(htmlFixture.url())) {
					StreamUtils.copy(config.getClassLoader(), htmlFixture.url(), writer);
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
			/*
			 * TestResultCollection response = serverSession.getServer().test(htmlFile, testedClassName,
			 * method.getName()); if (!response.isOk()) { // take the first wrong result throw
			 * response.buildException(); }
			 */
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Closeables.closeQuietly(writer);
			if (htmlFile != null) {
				htmlFile.delete();
			}
		}
	}

	private void addScript(Writer writer, String script) throws IOException {
		// remove wrong leading classpath://
		String cleanScript = script.replace("classpath://", "/");
		// add a slash to prevent the browser to interpret the scheme
		writer.append("<script src='" + cleanScript + "'></script>\n");
	}

	@Override
	public void sendNoMoreTestFixture(AsyncBrowserSession browser, HttpExchange exchange) throws IOException,
			URISyntaxException {
		// TODO Auto-generated method stub

	}

	@Override
	public TestResult buildResult(Map<String, String> queryStringParameters, HttpExchange exchange) {
		String userAgent = exchange.getRequestHeaders().getFirst("User-Agent");
		String result = queryStringParameters.get("result");
		String location = queryStringParameters.get("location");

		if (config.isDebugEnabled()) {
			System.out.println("Result was: " + result + ", at " + location + ", from " + userAgent);
		}

		return new TestResult(userAgent, result, location);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
