package org.stjs.testing.driver;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.testing.GeneratorWrapper;
import org.stjs.testing.annotation.HTMLFixture;

import com.google.common.io.Files;

/**
 * add the STJSBridge annotation only to allow it to be present in the junit annotation
 * 
 * @author acraciun
 * 
 */
@STJSBridge
public class STJSTestDriverRunner extends BlockJUnit4ClassRunner {
	private final STJSTestServer server;
	/**
	 * timeout to wait for all the connected clients
	 */
	private int timeout = 5000;

	public STJSTestDriverRunner(Class<?> klass) throws InitializationError {
		super(klass);
		try {
			server = new STJSTestServer();
			server.start();
			checkBrowsers();
		} catch (Exception e) {
			throw new InitializationError(e);
		}
	}

	private void checkBrowsers() throws InterruptedException, MalformedURLException, IOException, URISyntaxException {
		// wait for any previously open browser to connect
		Thread.sleep(2000);
		if (server.getBrowserCount() == 0) {
			if (Desktop.isDesktopSupported()) {
				System.out.println("Capturing the default browser ...");
				Desktop.getDesktop().browse(new URL(server.getHostURL(), "/start.html").toURI());
				for (int i = 0; i < 10; ++i) {
					Thread.sleep(500);
					if (server.getBrowserCount() > 0) {
						System.out.println("Captured browsers");
						return;
					}
				}
				System.out.println("Unable to capture a browser");
			}
		} else {
			System.out.println("Have " + server.getBrowserCount() + " browsers connected");
		}
	}

	@Override
	protected Statement methodBlock(final FrameworkMethod method) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {

				File jsFile = new GeneratorWrapper().generateCode(getTestClass());
				final HTMLFixture htmlFixture = getTestClass().getJavaClass().getAnnotation(HTMLFixture.class);
				try {
					File htmlFile = File.createTempFile(getTestClass().getJavaClass().getName()
							+ "_js_test_driver_adapter", ".html");
					FileWriter writer = new FileWriter(htmlFile);

					writer.append("<html>");
					writer.append("<head>");
					writer.append("<script src='/stjs.js'></script>");
					writer.append("<script src='/junit.js'></script>");
					writer.append("<script language='javascript'>");
					Files.copy(jsFile, Charset.defaultCharset(), writer);
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

					System.out.println("Added source file:" + htmlFile);
					TestResultCollection response = server.test(htmlFile, timeout);
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
