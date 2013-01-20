package org.stjs.testing.driver.browser;

import java.util.Set;

import org.junit.runners.model.InitializationError;
import org.openqa.selenium.browserlaunchers.locators.BrowserLocator;
import org.stjs.testing.driver.AsyncProcess;
import org.stjs.testing.driver.DriverConfiguration;
import org.stjs.testing.driver.HttpLongPollingServer;
import org.stjs.testing.driver.JUnitSession;
import org.stjs.testing.driver.Xvfb;

public abstract class HeadlessBrowser extends LongPollingBrowser {

	Process process;

	public HeadlessBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	protected void startProcess(BrowserLocator locator, String binPropertyName, String url) throws InitializationError {
		Xvfb xvfb = JUnitSession.getInstance().getDependency(Xvfb.class);
		ProcessBuilder builder = buildProcess(locator, binPropertyName, url);
		builder.environment().put("DISPLAY", xvfb.getDisplay());
		this.process = startProcess(builder);
	}

	@Override
	public void stop() {
		super.stop();
		if (this.process != null) {
			// Kill the process we started. This prevents many instances (or many tabs in one instance)
			// of the browsers from running invisibly at the same time.
			if (getConfig().isDebugEnabled()) {
				System.out.println("Stopping browser " + this.getClass().getSimpleName() + ", killing process");
			}
			this.process.destroy();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<Class<? extends AsyncProcess>> getSharedDependencies() {
		return processSet(HttpLongPollingServer.class, Xvfb.class);
	}
}
