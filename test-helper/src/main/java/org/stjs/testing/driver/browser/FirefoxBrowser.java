package org.stjs.testing.driver.browser;

import org.stjs.testing.driver.DriverConfiguration;

public class FirefoxBrowser extends AbstractBrowser {

	public static final String PROP_FIREFOX_BIN = "firefox.bin";

	public FirefoxBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void start(long browserId) {
		this.startProcess("firefox", PROP_FIREFOX_BIN, getStartPageUrl(browserId));
	}

}
