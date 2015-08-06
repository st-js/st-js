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

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.stjs.generator.ClassResolver;
import org.stjs.generator.DefaultClassResolver;
import org.stjs.generator.DependencyCollector;
import org.stjs.testing.driver.browser.Browser;
import org.stjs.testing.driver.browser.ChromeBrowser;
import org.stjs.testing.driver.browser.DesktopDefaultBrowser;
import org.stjs.testing.driver.browser.FirefoxBrowser;
import org.stjs.testing.driver.browser.HeadlessChromeBrowser;
import org.stjs.testing.driver.browser.HeadlessFirefoxBrowser;
import org.stjs.testing.driver.browser.PhantomjsBrowser;
import org.stjs.testing.driver.browser.RemoteBrowser;
import org.stjs.testing.driver.browser.RhinoBrowser;

import com.google.common.io.Closeables;

/**
 * this is a wrapper around the configuration files stjs-test.properties.
 *
 * @author acraciun
 */
public class DriverConfiguration {
	private static final String FILE_NAME = "/stjs-test.properties";

	private static final String PROP_CONFIG = "stjs.test.config";
	private static final String PROP_PORT = "stjs.test.port";
	private static final String PROP_WAIT_FOR_BROWSER = "stjs.test.wait";
	private static final String PROP_SKIP_IF_NO_BROWSER = "stjs.test.skipIfNoBrowser";
	private static final String PROP_START_BROWSER = "stjs.test.startBrowser";
	private static final String PROP_BROWSER_COUNT = "stjs.test.browserCount";
	private static final String PROP_BROWSERS = "stjs.test.browsers";
	private static final String PROP_TEST_TIMEOUT = "stjs.test.testTimeout";
	private static final String PROP_DEBUG = "stjs.test.debug";
	private static final String PROP_DEBUG_JAVA_SCRIPT = "stjs.test.debugJavaScript";

	private int port = 8055;
	private int waitForBrowser = 10;
	private boolean skipIfNoBrowser = false;
	private boolean startBrowser = true;
	private int testTimeout = 2;
	private boolean debugEnabled = false;
	private boolean debugJavaScript = false;
	private List<Browser> browsers;

	private final ClassLoader classLoader;
	private final ClassResolver stjsClassResolver;
	private final TestResourceResolver resourceResolver;
	private final DependencyCollector dependencyCollector;

	private Properties props;

	public DriverConfiguration(Class<?> klass) {

		InputStream in = null;
		props = new Properties();
		try {
			in = klass.getResourceAsStream(getConfigFileLocation());
			if (in != null) {
				props.load(in);
			}
		} catch (IOException e) {
			// silent
		} finally {
			Closeables.closeQuietly(in);
		}

		// system properties take precedence
		props.putAll(System.getProperties());
		if (props.get(PROP_PORT) != null) {
			port = Integer.parseInt(props.getProperty(PROP_PORT));
		}
		if (props.get(PROP_WAIT_FOR_BROWSER) != null) {
			waitForBrowser = Integer.parseInt(props.getProperty(PROP_WAIT_FOR_BROWSER));
		}
		if (props.get(PROP_SKIP_IF_NO_BROWSER) != null) {
			skipIfNoBrowser = Boolean.parseBoolean(props.getProperty(PROP_SKIP_IF_NO_BROWSER));
		}
		if (props.get(PROP_START_BROWSER) != null) {
			startBrowser = Boolean.parseBoolean(props.getProperty(PROP_START_BROWSER));
		}
		if (props.get(PROP_TEST_TIMEOUT) != null) {
			testTimeout = Integer.parseInt(props.getProperty(PROP_TEST_TIMEOUT));
		}
		if (props.get(PROP_BROWSER_COUNT) != null) {
			System.out.println("Configuration property " + PROP_BROWSER_COUNT + " is now ignored, use " + PROP_BROWSERS
					+ " instead");
		}
		if (props.get(PROP_DEBUG) != null) {
			debugEnabled = Boolean.parseBoolean(props.getProperty(PROP_DEBUG));
		}

		if (props.get(PROP_DEBUG_JAVA_SCRIPT) != null) {
			debugJavaScript = Boolean.parseBoolean(props.getProperty(PROP_DEBUG_JAVA_SCRIPT));
		} else {
			debugJavaScript = isJavaDebuggerAttached();
		}

		classLoader = new WebAppClassLoader(new URL[] {}, klass.getClassLoader(), debugEnabled);
		stjsClassResolver = new DefaultClassResolver(classLoader);
		resourceResolver = new TestResourceResolver(classLoader);
		dependencyCollector = new DependencyCollector();

		// load browsers last
		browsers = instantiateBrowsers();
	}

	private boolean isJavaDebuggerAttached() {
		String vmargs = ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
		System.out.println(vmargs);
		return vmargs.contains("-agentlib:jdwp") || vmargs.contains("-Xrunjdwp");
	}

	private String getConfigFileLocation() {
		String location = System.getProperties().getProperty(PROP_CONFIG);
		if (location == null) {
			location = FILE_NAME;
		}
		return location;
	}

	private List<Browser> instantiateBrowsers() {
		if (props.getProperty(PROP_BROWSERS) == null) {
			return Arrays.asList(new Browser[] { new DesktopDefaultBrowser(this) });
		}
		String[] browserNames = props.getProperty(PROP_BROWSERS).split(",");
		browsers = new ArrayList<Browser>(browserNames.length);
		for (String browserName : browserNames) {
			Browser browser = BrowserBuilder.build(browserName.trim(), this);
			if (browser != null) {
				browsers.add(browser);
			}
		}
		return browsers;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getWaitForBrowser() {
		return waitForBrowser;
	}

	public void setWaitForBrowser(int waitForBrowser) {
		this.waitForBrowser = waitForBrowser;
	}

	public boolean isSkipIfNoBrowser() {
		return skipIfNoBrowser;
	}

	public void setSkipIfNoBrowser(boolean skipIfNoBrowser) {
		this.skipIfNoBrowser = skipIfNoBrowser;
	}

	public boolean isStartBrowser() {
		return startBrowser;
	}

	public void setStartBrowser(boolean startBrowser) {
		this.startBrowser = startBrowser;
	}

	public int getTestTimeout() {
		return testTimeout;
	}

	public void setTestTimeout(int testTimeout) {
		this.testTimeout = testTimeout;
	}

	public int getBrowserCount() {
		return browsers.size();
	}

	public List<Browser> getBrowsers() {
		return browsers;
	}

	public boolean isDebugEnabled() {
		return debugEnabled;
	}

	public void setDebugEnabled(boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	public boolean isDebugJavaScript() {
		return debugJavaScript;
	}

	public void setDebugJavaScript(boolean debugJavaScript) {
		this.debugJavaScript = debugJavaScript;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public ClassResolver getStjsClassResolver(){
		return this.stjsClassResolver;
	}

	public String getProperty(String name) {
		return this.props.getProperty(name);
	}

	public String getProperty(String name, String defaultValue) {
		return this.props.getProperty(name, defaultValue);
	}

	public DependencyCollector getDependencyCollector() {
		return dependencyCollector;
	}

	public TestResource getResource(String httpUrl) throws URISyntaxException {
		return resourceResolver.resolveResource(httpUrl);
	}

	public URL getServerURL() {
		try {
			return new URL("http", "localhost", port, "/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private static enum BrowserBuilder {
		PHANTOMJS("phantomjs", PhantomjsBrowser.class), //
		DESKTOP_DEFAULT("desktopDefault", DesktopDefaultBrowser.class), //
		FIREFOX("firefox", FirefoxBrowser.class), //
		CHROME("chrome", ChromeBrowser.class), //
		HEADLESS_FIREFOX("headlessFirefox", HeadlessFirefoxBrowser.class), //
		HEADLESS_CHROME("headlessChrome", HeadlessChromeBrowser.class), //
		REMOTE("remote", RemoteBrowser.class), //
		RHINO("rhino", RhinoBrowser.class);

		String name;
		Class<? extends Browser> clazz;

		BrowserBuilder(String name, Class<? extends Browser> clazz) {
			this.name = name;
			this.clazz = clazz;
		}

		static Browser build(String browserName, DriverConfiguration config) {
			BrowserBuilder builder = forName(browserName);
			if (builder == null) {
				System.out.println("Unable to create browser \"" + browserName + "\": Unknown browser name");
				return null;
			}
			try {
				Constructor<? extends Browser> cons = builder.clazz.getConstructor(DriverConfiguration.class);
				return cons.newInstance(config);
			} catch (Exception e) {
				System.out.println("Unable to create browser \"" + browserName + "\": " + e.getMessage());
			}
			return null;
		}

		static BrowserBuilder forName(String name) {
			for (BrowserBuilder builder : BrowserBuilder.values()) {
				if (builder.name.equals(name)) {
					return builder;
				}
			}
			return null;
		}
	};
}
