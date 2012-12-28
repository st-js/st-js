package org.stjs.testing.driver;

import java.io.IOException;
import java.net.URISyntaxException;

import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public interface Browser {

	public void start(long browserId);

	public DriverConfiguration getConfig();

	public void sendTestFixture(AsyncMethod meth, AsyncBrowserSession browser, HttpExchange exchange) throws IOException, URISyntaxException;
}
