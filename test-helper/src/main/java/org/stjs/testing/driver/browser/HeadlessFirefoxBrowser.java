package org.stjs.testing.driver.browser;

import org.junit.runners.model.InitializationError;
import org.stjs.testing.driver.AsyncBrowserSession;
import org.stjs.testing.driver.DriverConfiguration;

public class HeadlessFirefoxBrowser extends AbstractHeadlessBrowser {

	public HeadlessFirefoxBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void start(AsyncBrowserSession session) throws InitializationError {
		this.registerWithLongPollingServer(session);
		this.startProcess("firefox", FirefoxBrowser.PROP_FIREFOX_BIN, getStartPageUrl(session.getId()));
	}

}
