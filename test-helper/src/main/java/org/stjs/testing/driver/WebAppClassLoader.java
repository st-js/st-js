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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Enumeration;

import org.stjs.generator.GeneratorConstants;

import com.google.common.io.Files;

/**
 * This class loader allows the loading of files from the src/main/webapp folder, target/stjs-test and war dependencies
 * 
 * @author acraciun
 * 
 */
public class WebAppClassLoader extends URLClassLoader {
	private final boolean debugEnabled;
	// TODO get this from maven config
	public static final File WEBAPP_DIR = new File("src/main/webapp");

	public WebAppClassLoader(URL[] urls, ClassLoader parent, boolean debugEnabled) {
		super(urls, parent);

		this.debugEnabled = debugEnabled;
		try {
			// make sure the folder exists before adding it
			STJSTestDriverRunner.targetDirectory.mkdirs();
			addURL(STJSTestDriverRunner.targetDirectory.toURI().toURL());
			addURL(WEBAPP_DIR.toURI().toURL());

			addCurrentProjectSTJSClasspath();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private void addCurrentProjectSTJSClasspath() {
		try {
			Enumeration<URL> cpFiles = getResources(GeneratorConstants.CLASSPATH_FILE);
			while (cpFiles.hasMoreElements()) {
				URL cpFile = cpFiles.nextElement();
				if ("file".equals(cpFile.getProtocol())) {
					for (String cp : Files.readLines(new File(cpFile.toURI()), Charset.defaultCharset())) {
						addURL(new File(cp).toURI().toURL());
					}
				}
			}
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	protected void addURL(URL url) {
		if (debugEnabled) {
			System.out.println("Added to test CLASSPATH: " + url);
		}
		super.addURL(url);
	}

}
