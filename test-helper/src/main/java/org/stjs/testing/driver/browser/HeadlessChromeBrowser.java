package org.stjs.testing.driver.browser;

import org.junit.runners.model.InitializationError;
import org.stjs.testing.driver.AsyncBrowserSession;
import org.stjs.testing.driver.DriverConfiguration;

public class HeadlessChromeBrowser extends AbstractHeadlessBrowser {

	public HeadlessChromeBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void start(AsyncBrowserSession session) throws InitializationError {
		this.registerWithLongPollingServer(session);
		this.startProcess("google-chrome", ChromeBrowser.PROP_CHROME_BIN, getStartPageUrl(session.getId()));
	}

}
