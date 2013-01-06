package org.stjs.testing.driver.browser;

import java.util.Set;

import org.stjs.testing.driver.DriverConfiguration;
import org.stjs.testing.driver.SharedExternalProcess;
import org.stjs.testing.driver.Xvfb;

public abstract class AbstractHeadlessBrowser extends AbstractBrowser {

	public AbstractHeadlessBrowser(DriverConfiguration config) {
		super(config);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<Class<? extends SharedExternalProcess>> getExternalProcessDependencies() {
		return this.getExternalProcessDependencies(Xvfb.class);
	}
}
