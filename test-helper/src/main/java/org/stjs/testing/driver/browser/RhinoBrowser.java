package org.stjs.testing.driver.browser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;
import org.stjs.testing.driver.DriverConfiguration;

import com.google.common.io.Closeables;

/**
 * This browser uses Rhino Javascript engine and env.js to run a headless browser inside the virtual machine.
 * 
 * @author acraciun
 * 
 */
public class RhinoBrowser extends AbstractBrowser {

	public RhinoBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void start(long browserId) {

		Reader reader = null;
		try {
			Context cx = ContextFactory.getGlobal().enterContext();
			cx.setOptimizationLevel(-1);
			cx.setLanguageVersion(Context.VERSION_1_5);
			// Global global = Main.getGlobal();
			// global.init(cx);
			ScriptableObject scope = cx.initStandardObjects();
			String printFunction = "function print(message) {java.lang.System.out.println(message);}";
			cx.evaluateString(scope, printFunction, "print", 1, null);
			cx.evaluateReader(scope, new InputStreamReader(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("env.rhino.js")), "env.rhino.js", 1, null);
			cx.evaluateString(scope, "window.location='" + getStartPageUrl(browserId) + "';", "eval", 1, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Closeables.closeQuietly(reader);
		}
	}
}
