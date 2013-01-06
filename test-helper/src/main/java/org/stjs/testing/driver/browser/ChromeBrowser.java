package org.stjs.testing.driver.browser;

import org.stjs.testing.driver.AsyncBrowserSession;
import org.stjs.testing.driver.DriverConfiguration;

public class ChromeBrowser extends AbstractBrowser {

	public static final String PROP_CHROME_BIN = "chrome.bin";

	public ChromeBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	public void start(AsyncBrowserSession session) {
		this.registerWithLongPollingServer(session);
		this.startProcess("google-chrome", PROP_CHROME_BIN, getStartPageUrl(session.getId()));
	}
}
