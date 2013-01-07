package org.stjs.testing.driver.browser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Exchanger;

import org.junit.runners.model.InitializationError;
import org.stjs.generator.BridgeClass;
import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollection;
import org.stjs.generator.Generator;
import org.stjs.testing.annotation.HTMLFixture;
import org.stjs.testing.annotation.Scripts;
import org.stjs.testing.annotation.ScriptsAfter;
import org.stjs.testing.annotation.ScriptsBefore;
import org.stjs.testing.driver.DriverConfiguration;
import org.stjs.testing.driver.HttpLongPollingServer;
import org.stjs.testing.driver.JUnitSession;
import org.stjs.testing.driver.MultiTestMethod;
import org.stjs.testing.driver.StreamUtils;
import org.stjs.testing.driver.TestResult;

import com.google.common.base.Strings;
import com.sun.net.httpserver.HttpExchange;

/**
 * Represents a testing session opened with one instance of a browser that uses long-polling to fetch new tests to execute from the HTTP server.
 * LongPollingBrowser handles multithreading synchronization between the browser, the HTTP server and the JUnit runner. The JUnit runner notifies
 * this browser that a new test method must be executed by calling executeTest(MultiTestMethod), or that it has finished executing all the tests
 * by calling notifyNoMoreTests(). The HTTP server waits for a new test to send to the browser by calling awaitNewTestReady(). <br>
 * <br>
 * On top of that, LongPollinBrowser delegates the details of starting and stopping the browser itself to its concrete subclasses.
 * @author lordofthepigs
 */
@SuppressWarnings({"restriction", "deprecation"})
public abstract class LongPollingBrowser extends AbstractBrowser {

	private final Exchanger<MultiTestMethod> exchanger = new Exchanger<MultiTestMethod>();
	private volatile MultiTestMethod methodUnderExecution = null;
	private long id;
	private volatile boolean isDead = false;

	public LongPollingBrowser(DriverConfiguration config) {
		super(config);
	}

	protected void registerWithLongPollingServer() {
		this.id = JUnitSession.getInstance().getDependency(HttpLongPollingServer.class).registerBrowserSession(this);
		if (getConfig().isDebugEnabled()) {
			System.out.println("Browser " + id + " is " + this.getClass().getSimpleName());
		}
	}

	/**
	 * Starts the browser session. This will open a browser and navigate it to some page where the unit testing procedure can be started. The
	 * decision about exactly which browser binary is started, how it is started and which page is opened is delegated to the Browser
	 * implementation that this AsynBrowserSession was constructed with.
	 */
	@Override
	public abstract void start() throws InitializationError;

	/**
	 * Blocks until JUnit notifies this browser session that either a new test must be executed, or there are no more tests. If there is a new
	 * test to execute, then this method returns it. If there are no more tests, this method returns null.
	 * @return The next test to execute, or null if there isn't any
	 */
	public MultiTestMethod awaitNewTestReady() {
		try {
			if (getConfig().isDebugEnabled()) {
				System.out.println("Browser " + this.id + " is waiting for a new test");
			}
			methodUnderExecution = exchanger.exchange(null);
			if (getConfig().isDebugEnabled()) {
				if (methodUnderExecution != null) {
					System.out.println("Browser " + this.id + " has picked up the test " + methodUnderExecution.getMethod().getMethod());
				} else {
					System.out.println("Browser " + this.id + " has no more tests");
				}
			}
			return methodUnderExecution;
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Notifies this browser that the specified test must be executed. This method blocks until this browser picks up the test by calling
	 * awaitNewTestReady().
	 * @param method The test to execute.
	 */
	@Override
	public void executeTest(MultiTestMethod method) {
		if (this.isDead) {
			method.notifyExecutionResult(new TestResult(this.getClass().getSimpleName(), "Browser is dead", null));
			return;
		}
		try {
			if (getConfig().isDebugEnabled()) {
				System.out.println("Test " + method.getMethod().getMethod() + " is available for browser " + this.id);
			}
			exchanger.exchange(method);
			if (getConfig().isDebugEnabled()) {
				System.out.println("Browser " + this.id + " has picked up the new test");
			}
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Notifies this browser that there are no more tests to execute. This method blocks until this browser attempts to pick up a new test by
	 * calling awaitNewTestReady().
	 */
	@Override
	public void notifyNoMoreTests() {
		try {
			if (getConfig().isDebugEnabled()) {
				System.out.println("Browser " + this.id + " has been notified that no more tests are coming");
			}
			exchanger.exchange(null);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the test is currently being executed by this browser.
	 */
	public MultiTestMethod getMethodUnderExecution() {
		return methodUnderExecution;
	}

	/**
	 * Writes to the HTTP response the HTML and/or javascript code that is necessary for the browser to execute the specified test.
	 * @param meth The test to send to the browser
	 * @param browserSession The session to which the test is sent
	 * @param exchange contains the HTTP response that must be written to
	 */
	public void sendTestFixture(MultiTestMethod meth, HttpExchange exchange) throws Exception {
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

	/**
	 * Writes to the HTTP response the HTML and/or javascript code that is necessary for the browser understand that there will be no more tests.
	 * @param browserSession The session to be notified
	 * @param exchange contains the HTTP response that must be written to
	 * @throws IOException
	 */
	public void sendNoMoreTestFixture(HttpExchange exchange) throws IOException {
		sendResponse("<html><body><h1>Tests completed!</h1></body></html>", exchange);
	}

	public void markAsDead(Throwable throwable, String userAgent) {
		this.isDead = true;
		this.methodUnderExecution.notifyExecutionResult(new TestResult(userAgent, throwable.getMessage(), null));
	}

	public long getId() {
		return this.id;
	}
}
