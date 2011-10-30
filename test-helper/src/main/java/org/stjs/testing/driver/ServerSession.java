package org.stjs.testing.driver;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * There is an unique server session per unit test launch
 * 
 * @author acraciun
 * 
 */
public class ServerSession {
	private static ServerSession instance;

	private final STJSTestServer server;

	/**
	 * timeout to wait for all the connected clients
	 */
	private int timeout;
	private final boolean hasBrowsers;
	private final boolean startBrowser;

	public static synchronized ServerSession getInstance() throws IOException, InterruptedException {
		if (instance == null) {
			instance = new ServerSession();
		}
		return instance;
	}

	private ServerSession() throws IOException, InterruptedException {
		DriverConfiguration config = new DriverConfiguration();
		timeout = config.getWaitForBrowser() * 1000;
		startBrowser = config.isStartBrowser();
		server = new STJSTestServer(config.getPort(), config.getTestTimeout());
		server.start();
		hasBrowsers = checkBrowsers();
	}

	private boolean checkBrowsers() throws InterruptedException, IOException {
		// wait for any previously open browser to connect
		final int initialTimeout = 2000;
		final int stepTimeout = 500;

		try {
			Thread.sleep(Math.min(initialTimeout, timeout));
			if (server.getBrowserCount() == 0) {
				if (startBrowser && Desktop.isDesktopSupported()) {
					System.out.println("Starting the default browser ...");
					Desktop.getDesktop().browse(new URL(server.getHostURL(), "/start.html").toURI());
					for (int i = initialTimeout; i < timeout; i += stepTimeout) {
						Thread.sleep(stepTimeout);
						if (server.getBrowserCount() > 0) {
							System.out.println("Captured browsers");
							return true;
						}
					}
					System.out.println("Unable to capture a browser");
				} else {
					System.out.println("Cannot start a browser");
				}
				return false;
			}
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		System.out.println("Have " + server.getBrowserCount() + " browsers connected");
		return true;
	}

	public STJSTestServer getServer() {
		return server;
	}

	public boolean hasBrowsers() {
		return hasBrowsers;
	}

}
