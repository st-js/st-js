package org.stjs.testing.driver.browser;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import org.junit.runners.model.InitializationError;
import org.stjs.generator.GeneratorConstants;
import org.stjs.testing.driver.AsyncProcess;
import org.stjs.testing.driver.DriverConfiguration;
import org.stjs.testing.driver.MultiTestMethod;

/**
 * @author acraciun
 */
@SuppressWarnings({"restriction" /* for HttpExchange */, "deprecation" /* for @Scripts */})
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
	public void start() throws InitializationError {
		// TODO: assign this ID ourself by asking a persistent polling server or something like that
		//this.browserId = browserId;
		hasBrowsers = false;
	}

	@Override
	public void executeTest(MultiTestMethod method) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyNoMoreTests() {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Class<? extends AsyncProcess>> getSharedDependencies() {
		// TODO: specify dependencies
		return Collections.emptySet();
	}

}
