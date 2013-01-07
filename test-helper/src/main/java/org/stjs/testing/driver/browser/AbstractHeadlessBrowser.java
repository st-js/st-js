package org.stjs.testing.driver.browser;

import java.util.Set;

import org.stjs.testing.driver.AsyncProcess;
import org.stjs.testing.driver.AsyncServerSession;
import org.stjs.testing.driver.DriverConfiguration;
import org.stjs.testing.driver.JUnitSession;
import org.stjs.testing.driver.Xvfb;

public abstract class AbstractHeadlessBrowser extends AbstractBrowser {

	public AbstractHeadlessBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	protected void startProcess(String defaultBinaryName, String binPropertyName, String url) {
		Xvfb xvfb = JUnitSession.getInstance().getDependency(Xvfb.class);
		ProcessBuilder builder = buildProcess(defaultBinaryName, binPropertyName, url);
		builder.environment().put("DISPLAY", xvfb.getDisplay());
		startProcess(builder);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<Class<? extends AsyncProcess>> getSharedDependencies() {
		return processSet(AsyncServerSession.class, Xvfb.class);
	}
}
