package org.stjs.testing.driver.browser;

import org.junit.runners.model.InitializationError;
import org.openqa.selenium.browserlaunchers.locators.CombinedFirefoxLocator;
import org.stjs.testing.driver.DriverConfiguration;

public class FirefoxBrowser extends LongPollingBrowser {

	public static final String PROP_FIREFOX_BIN = "firefox.bin";

	public FirefoxBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void doStart() throws InitializationError {
		this.registerWithLongPollingServer();
		this.startProcess(new CombinedFirefoxLocator(), PROP_FIREFOX_BIN, getStartPageUrl(getId(), false));
	}
}
