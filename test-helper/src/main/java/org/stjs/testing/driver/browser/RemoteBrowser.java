package org.stjs.testing.driver.browser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.stjs.generator.GeneratorConstants;
import org.stjs.testing.driver.AsyncBrowserSession;
import org.stjs.testing.driver.DriverConfiguration;

import com.sun.net.httpserver.HttpExchange;

/**
 * 
 * @author acraciun
 * 
 */
@SuppressWarnings({ "restriction" /* for HttpExchange */, "deprecation" /* for @Scripts */})
public class RemoteBrowser extends AbstractBrowser {
	public final static File targetDirectory = new File("target", GeneratorConstants.STJS_TEST_TEMP_FOLDER);

	/**
	 * timeout to wait for all the connected clients
	 */
	private int timeout;
	private boolean hasBrowsers;
	private long browserId;

	public RemoteBrowser(DriverConfiguration config) {
		super(config);
		timeout = config.getWaitForBrowser() * 1000;
	}

	@Override
	public void start(long browserId) {
		this.browserId = browserId;
		hasBrowsers = false;
	}

	@Override
	public void sendNoMoreTestFixture(AsyncBrowserSession browser, HttpExchange exchange) throws IOException,
			URISyntaxException {
		// TODO Auto-generated method stub

	}

}
