package org.stjs.testing.driver;

import org.junit.runners.model.InitializationError;

public interface AsyncProcess {

	public void start() throws InitializationError;

	public void stop();

}
