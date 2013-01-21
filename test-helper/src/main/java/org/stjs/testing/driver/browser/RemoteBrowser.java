package org.stjs.testing.driver.browser;

import org.junit.runners.model.InitializationError;
import org.stjs.testing.driver.DriverConfiguration;

/**
 * @author acraciun
 */
public class RemoteBrowser extends LongPollingBrowser {

	public RemoteBrowser(DriverConfiguration config) {
		super(config);
		// timeout = config.getWaitForBrowser() * 1000;
	}

	@Override
	protected void doStart() throws InitializationError {
		this.registerWithLongPollingServer();
	}

}
