/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
	private final int browserCount;
	private final boolean startBrowser;

	public static synchronized ServerSession getInstance(DriverConfiguration config) throws IOException,
			InterruptedException {
		if (instance == null) {
			instance = new ServerSession(config);
		}
		return instance;
	}

	private ServerSession(DriverConfiguration config) throws IOException, InterruptedException {
		timeout = config.getWaitForBrowser() * 1000;
		startBrowser = config.isStartBrowser();
		browserCount = config.getBrowserCount();
		server = new STJSTestServer(config.getPort(), config.getTestTimeout(), config.isDebugEnabled());
		server.start();
		hasBrowsers = checkBrowsers();
	}

	private boolean checkBrowsers() throws InterruptedException, IOException {
		// wait for any previously open browser to connect
		final int initialTimeout = 2000;
		final int stepTimeout = 500;

		System.out.println("Waiting for " + browserCount + " browser(s)");
		try {
			Thread.sleep(Math.min(initialTimeout, timeout));
			if (server.getBrowserCount() < browserCount) {
				if (startBrowser && Desktop.isDesktopSupported() && (server.getBrowserCount() == 0)) {
					System.out.println("Starting the default browser ...");
					Desktop.getDesktop().browse(new URL(server.getHostURL(), "/start.html").toURI());
				}

				for (int i = initialTimeout; i < timeout; i += stepTimeout) {
					Thread.sleep(stepTimeout);
					if (server.getBrowserCount() >= browserCount) {
						System.out.println("Captured browsers");
						return true;
					}
				}
				System.err.println("Unable to capture at least " + browserCount + " browser(s)");
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
