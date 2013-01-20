package org.stjs.testing.driver.browser;

import org.junit.runners.model.InitializationError;
import org.openqa.selenium.browserlaunchers.locators.GoogleChromeLocator;
import org.stjs.testing.driver.DriverConfiguration;

public class ChromeBrowser extends LongPollingBrowser {

	public static final String PROP_CHROME_BIN = "chrome.bin";

	public ChromeBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void doStart() throws InitializationError {
		this.registerWithLongPollingServer();
		this.startProcess(new GoogleChromeLocator(), PROP_CHROME_BIN, getStartPageUrl(getId(), false));
	}
}
