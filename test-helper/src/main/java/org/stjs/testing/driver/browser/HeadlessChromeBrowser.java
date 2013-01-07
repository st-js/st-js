package org.stjs.testing.driver.browser;

import org.junit.runners.model.InitializationError;
import org.stjs.testing.driver.DriverConfiguration;

public class HeadlessChromeBrowser extends HeadlessBrowser {

	public HeadlessChromeBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void start() throws InitializationError {
		this.registerWithLongPollingServer();
		this.startProcess("google-chrome", ChromeBrowser.PROP_CHROME_BIN, getStartPageUrl(getId()));
	}

}
