package org.stjs.testing.driver.browser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.stjs.generator.BridgeClass;
import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollection;
import org.stjs.generator.Generator;
import org.stjs.testing.annotation.HTMLFixture;
import org.stjs.testing.annotation.Scripts;
import org.stjs.testing.annotation.ScriptsAfter;
import org.stjs.testing.annotation.ScriptsBefore;
import org.stjs.testing.driver.AsyncBrowserSession;
import org.stjs.testing.driver.AsyncMethod;
import org.stjs.testing.driver.AsyncProcess;
import org.stjs.testing.driver.AsyncServerSession;
import org.stjs.testing.driver.DriverConfiguration;
import org.stjs.testing.driver.JUnitSession;
import org.stjs.testing.driver.StreamUtils;
import org.stjs.testing.driver.TestResult;

import com.google.common.base.Strings;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings({"restriction", "deprecation"})
public abstract class AbstractBrowser implements Browser {

	private DriverConfiguration config;

	public AbstractBrowser(DriverConfiguration config) {
		this.config = config;
	}

	protected void registerWithLongPollingServer(AsyncBrowserSession bs) {
		JUnitSession.getInstance().getDependency(AsyncServerSession.class).registerBrowserSession(bs);
	}

	protected void startProcess(String defaultBinaryName, String binPropertyName, String url) {
		startProcess(buildProcess(defaultBinaryName, binPropertyName, url));
	}

	protected void startProcess(ProcessBuilder builder) {
		try {
			builder.start();

			if (config.isDebugEnabled()) {
				System.out.println("Started " + builder.command().get(0));
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected ProcessBuilder buildProcess(String defaultBinaryName, String binPropertyName, String url) {
		String executableName = config.getProperty(binPropertyName, defaultBinaryName);
		return new ProcessBuilder(executableName, url);
	}

	protected String getStartPageUri(long browserId) {
		return "start-nopoll.html?browserId=" + browserId;
	}

	protected String getStartPageUrl(long browserId) {
		return getConfig().getServerURL() + getStartPageUri(browserId);
	}

	@Override
	public DriverConfiguration getConfig() {
		return config;
	}

	@Override
	public void sendTestFixture(AsyncMethod meth, AsyncBrowserSession browserSession, HttpExchange exchange) throws IOException,
			URISyntaxException {
		Class<?> testClass = meth.getTestClass().getJavaClass();
		Method method = meth.getMethod().getMethod();
		ClassWithJavascript stjsClass = new Generator().getExistingStjsClass(getConfig().getClassLoader(), testClass);

		final HTMLFixture htmlFixture = testClass.getAnnotation(HTMLFixture.class);

		final Scripts addedScripts = testClass.getAnnotation(Scripts.class);
		final ScriptsBefore addedScriptsBefore = testClass.getAnnotation(ScriptsBefore.class);
		final ScriptsAfter addedScriptsAfter = testClass.getAnnotation(ScriptsAfter.class);

		StringBuilder resp = new StringBuilder(8192);
		resp.append("<html>\n");
		resp.append("<head>\n");
		appendScriptTag(resp, "/stjs.js");
		appendScriptTag(resp, "/junit.js");

		resp.append("<script language='javascript'>stjs.mainCallDisabled=true;</script>\n");

		// scripts added explicitly
		if (addedScripts != null) {
			for (String script : addedScripts.value()) {
				appendScriptTag(resp, script);
			}
		}
		// scripts before - new style
		if (addedScriptsBefore != null) {
			for (String script : addedScriptsBefore.value()) {
				appendScriptTag(resp, script);
			}
		}

		Set<URI> jsFiles = new LinkedHashSet<URI>();
		for (ClassWithJavascript dep : new DependencyCollection(stjsClass).orderAllDependencies(getConfig().getClassLoader())) {

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
			appendScriptTag(resp, file.toString());
		}

		// scripts after - new style
		if (addedScriptsAfter != null) {
			for (String script : addedScriptsAfter.value()) {
				appendScriptTag(resp, script);
			}
		}
		resp.append("<script language='javascript'>\n");
		resp.append("  onload=function(){\n");
		resp.append("    console.error(document.getElementsByTagName('html')[0].innerHTML);\n");

		// Adapter between generated assert (not global) and JS-test-driver assert (which is a
		// set of global methods)
		resp.append("    Assert=window;\n");

		String testedClassName = testClass.getSimpleName();
		resp.append("    parent.log('<b>" + testedClassName + "</b>." + method.getName() + "');");
		resp.append("    try{\n");
		resp.append("      new " + testedClassName + "()." + method.getName() + "();\n");
		resp.append("      parent.reportResultAndRunNextTest('OK');\n");
		resp.append("    }catch(ex){\n");
		resp.append("      parent.reportResultAndRunNextTest(ex, ex.location);\n");
		resp.append("    }\n");
		resp.append("  }\n");
		resp.append("</script>\n");
		resp.append("</head>\n");
		resp.append("<body>\n");
		if (htmlFixture != null) {
			if (!Strings.isNullOrEmpty(htmlFixture.value())) {
				resp.append(htmlFixture.value());

			} else if (!Strings.isNullOrEmpty(htmlFixture.url())) {
				StringWriter writer = new StringWriter();
				StreamUtils.copy(getConfig().getClassLoader(), htmlFixture.url(), writer);
				resp.append(writer.toString());
			}
		}
		resp.append("</body>\n");
		resp.append("</html>\n");

		byte[] response = resp.toString().getBytes("UTF-8");
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);

		OutputStream output = exchange.getResponseBody();
		output.write(response);
		output.flush();
	}

	protected boolean isRunningOnWindows() {
		return System.getProperty("os.name").contains("Windows");
	}

	/**
	 * The default implementation sends a pure HTML page with a title saying "Tests completed".
	 */
	@Override
	public void sendNoMoreTestFixture(AsyncBrowserSession browser, HttpExchange exchange) throws IOException, URISyntaxException {
		sendResponse("<html><body><h1>Tests completed!</h1></body></html>", exchange);
	}

	protected void appendScriptTag(StringBuilder builder, String script) throws IOException {
		// remove wrong leading classpath://
		String cleanScript = script.replace("classpath://", "/");
		// add a slash to prevent the browser to interpret the scheme
		builder.append("<script src='" + cleanScript + "'></script>\n");
	}

	protected void sendResponse(String content, HttpExchange exchange) throws IOException {
		byte[] response;
		try {
			response = content.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			// Cannot happen. UTF-8 is part of the character sets that must be supported by any implementation of java
			throw new RuntimeException(e);
		}
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);

		OutputStream output = exchange.getResponseBody();
		output.write(response);
		output.flush();
	}

	/**
	 * The default implementation builds the test result by reading the "result" and "location" query string parameters
	 */
	@Override
	public TestResult buildResult(Map<String, String> queryStringParameters, HttpExchange exchange) {
		String userAgent = exchange.getRequestHeaders().getFirst("User-Agent");
		String result = queryStringParameters.get("result");
		String location = queryStringParameters.get("location");

		if (getConfig().isDebugEnabled()) {
			System.out.println("Result was: " + result + ", at " + location + ", from " + userAgent);
		}

		return new TestResult(userAgent, result, location);
	}

	@Override
	public void stop() {
		// default implementation does nothing
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<Class<? extends AsyncProcess>> getSharedDependencies() {
		return processSet(AsyncServerSession.class);
	}

	protected static Set<Class<? extends AsyncProcess>> processSet(Class<? extends AsyncProcess>... clazz) {
		Set<Class<? extends AsyncProcess>> deps = new HashSet<Class<? extends AsyncProcess>>();
		for (Class<? extends AsyncProcess> c : clazz) {
			deps.add(c);
		}
		return deps;
	}
}
