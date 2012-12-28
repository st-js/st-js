package org.stjs.testing.driver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.stjs.generator.BridgeClass;
import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollection;
import org.stjs.generator.Generator;
import org.stjs.testing.annotation.HTMLFixture;
import org.stjs.testing.annotation.Scripts;
import org.stjs.testing.annotation.ScriptsAfter;
import org.stjs.testing.annotation.ScriptsBefore;

import com.google.common.base.Strings;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class PhantomjsBrowser implements Browser {

	private final DriverConfiguration config;

	public PhantomjsBrowser(DriverConfiguration config) {
		this.config = config;
	}

	@Override
	public void start(long browserId) {
		//		ProcessBuilder pb =
		//				new ProcessBuilder("/home/npiguet/softwares/phantomjs/bin/phantomjs",
		//						"/home/npiguet/workspace/st-js/test-helper/src/main/js/phantomjs-bootstrap.js", Long.toString(browserId));
		//		pb.redirectErrorStream();
		//		try {
		//			Process p = pb.start();
		//			if (config.isDebugEnabled()) {
		//				System.out.println("Started phantomjs");
		//			}
		//		}
		//		catch (IOException e) {
		//			throw new RuntimeException(e);
		//		}

		//		~/softwares/phantomjs/bin/phantomjs --web-security=no phantomjs-bootstrap.js 0 "http://localhost:8055"

	}

	@Override
	public DriverConfiguration getConfig() {
		return config;
	}

	@Override
	public void sendTestFixture(AsyncMethod meth, AsyncBrowserSession browser, HttpExchange exchange) throws IOException, URISyntaxException {

		Class<?> testClass = meth.getTestClass().getJavaClass();
		Method method = meth.getMethod().getMethod();
		ClassWithJavascript stjsClass = new Generator().getExistingStjsClass(config.getClassLoader(), testClass);

		final HTMLFixture htmlFixture = testClass.getAnnotation(HTMLFixture.class);

		final Scripts addedScripts = testClass.getAnnotation(Scripts.class);
		final ScriptsBefore addedScriptsBefore = testClass.getAnnotation(ScriptsBefore.class);
		final ScriptsAfter addedScriptsAfter = testClass.getAnnotation(ScriptsAfter.class);

		StringBuilder resp = new StringBuilder(8192);
		resp.append("<html>\n");
		resp.append("<head>\n");
		addScript(resp, "/stjs.js");
		addScript(resp, "/junit.js");

		resp.append("<script language='javascript'>stjs.mainCallDisabled=true;</script>\n");

		// scripts added explicitly
		if (addedScripts != null) {
			for (String script : addedScripts.value()) {
				addScript(resp, script);
			}
		}
		// scripts before - new style
		if (addedScriptsBefore != null) {
			for (String script : addedScriptsBefore.value()) {
				addScript(resp, script);
			}
		}

		Set<URI> jsFiles = new LinkedHashSet<URI>();
		for (ClassWithJavascript dep : new DependencyCollection(stjsClass).orderAllDependencies(config.getClassLoader())) {

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
			addScript(resp, file.toString());
		}

		// scripts after - new style
		if (addedScriptsAfter != null) {
			for (String script : addedScriptsAfter.value()) {
				addScript(resp, script);
			}
		}
		resp.append("<script language='javascript'>\n");
		resp.append("  onload=function(){\n");
		resp.append("    console.error(document.getElementsByTagName('html')[0].innerHTML);\n");

		// Adapter between generated assert (not global) and JS-test-driver assert (which is a
		// set of global methods)
		resp.append("    Assert=window;\n");

		String testedClassName = testClass.getSimpleName();

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
				StreamUtils.copy(config.getClassLoader(), htmlFixture.url(), writer);
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

	@Override
	public void sendNoMoreTestFixture(AsyncBrowserSession browser, HttpExchange exchange) throws IOException, URISyntaxException {
		byte[] response = "<html><head><script language='javascript'>parent.phantom.exit()</script></head></html>".getBytes("UTF-8");
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);

		OutputStream output = exchange.getResponseBody();
		output.write(response);
		output.flush();
	}

	private void addScript(StringBuilder builder, String script) throws IOException {
		// remove wrong leading classpath://
		String cleanScript = script.replace("classpath://", "/");
		// add a slash to prevent the browser to interpret the scheme
		builder.append("<script src='" + cleanScript + "'></script>\n");
	}

	@Override
	public void stop() {
		// for phantomJS stop() does nothing.
		// phantomJS automatically stops when the noMoreTests fixture is sent
	}
}
