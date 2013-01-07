package org.stjs.testing.driver.browser;

import org.junit.runners.model.InitializationError;
import org.stjs.testing.driver.DriverConfiguration;

public class HeadlessFirefoxBrowser extends HeadlessBrowser {

	public HeadlessFirefoxBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void start() throws InitializationError {
		this.registerWithLongPollingServer();
		this.startProcess("firefox", FirefoxBrowser.PROP_FIREFOX_BIN, getStartPageUrl(getId(), false));
	}

}
