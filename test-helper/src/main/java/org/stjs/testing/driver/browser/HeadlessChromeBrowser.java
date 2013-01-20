package org.stjs.testing.driver.browser;

import org.junit.runners.model.InitializationError;
import org.openqa.selenium.browserlaunchers.locators.GoogleChromeLocator;
import org.stjs.testing.driver.DriverConfiguration;

public class HeadlessChromeBrowser extends HeadlessBrowser {

	public HeadlessChromeBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void doStart() throws InitializationError {
		this.registerWithLongPollingServer();
		this.startProcess(new GoogleChromeLocator(), ChromeBrowser.PROP_CHROME_BIN, getStartPageUrl(getId(), false));
	}

}
