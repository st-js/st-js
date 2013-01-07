package org.stjs.testing.driver.browser;

import org.junit.runners.model.InitializationError;
import org.stjs.testing.driver.DriverConfiguration;

public class FirefoxBrowser extends LongPollingBrowser {

	public static final String PROP_FIREFOX_BIN = "firefox.bin";

	public FirefoxBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void start() throws InitializationError {
		this.registerWithLongPollingServer();
		this.startProcess("firefox", PROP_FIREFOX_BIN, getStartPageUrl(getId(), false));
	}
}
