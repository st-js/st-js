package org.stjs.testing.driver.browser;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.runners.model.InitializationError;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;
import org.stjs.testing.driver.DriverConfiguration;

/**
 * This browser uses Rhino Javascript engine and env.js to run a headless browser inside the virtual machine.
 * 
 * @author acraciun
 */
public class RhinoBrowser extends LongPollingBrowser {

	public RhinoBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void doStart() throws InitializationError {
		this.registerWithLongPollingServer();

		// start evaluating the tests by loading the page. Since page load happens inside the current thread
		// and the contract of this method says we should return as soon as possible, let's do that in another thread.
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// bootstrap Rhino with env.rhino.js so that our rhino looks like a real browser
					final Context cx = ContextFactory.getGlobal().enterContext();
					cx.setOptimizationLevel(-1);
					cx.setLanguageVersion(Context.VERSION_1_5);
					final ScriptableObject scope = cx.initStandardObjects();
					String printFunction = "function print(message) {java.lang.System.out.println(message);}";
					cx.evaluateString(scope, printFunction, "print", 1, null);
					cx.evaluateReader(scope, new InputStreamReader(Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("env.rhino.js")), "env.rhino.js", 1, null);
					cx.evaluateString(scope, "window.location='" + getStartPageUrl(getId(), false) + "';", "eval", 1,
							null);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}, "rhino-context");
		t.setDaemon(true);
		t.start();
	}

}
