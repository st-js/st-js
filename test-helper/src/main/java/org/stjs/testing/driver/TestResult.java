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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.stjs.generator.STJSClass;
import org.stjs.generator.sourcemap.JavascriptToJava;

import com.google.common.io.Closeables;

public class TestResult {
	private final static Pattern STACKTRACE_JS_PATTERN = Pattern.compile("\\s*at(.+)\\.(.+)\\((.+)\\)");
	private final String message;
	private final String location;
	private final String userAgent;
	private final boolean isAssert;

	public TestResult(String userAgent, String message, String location, boolean isAssert) {
		this.userAgent = userAgent;
		this.message = message;
		this.location = location;
		this.isAssert = isAssert;
	}

	public String getMessage() {
		return message;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public boolean isOk() {
		return "OK".equals(message);
	}

	private String getClassName(ClassLoader testClassLoader, String propertiesFile) {
		InputStream in = null;
		try {
			in = testClassLoader.getResourceAsStream(propertiesFile.substring(1));
			if (in == null) {
				throw new RuntimeException("Cannot find STJS properties file:" + propertiesFile);
			}
			Properties p = new Properties();
			p.load(in);
			return p.getProperty(STJSClass.CLASS_PROP);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Closeables.closeQuietly(in);
		}
	}

	public Throwable buildException(ClassLoader testClassLoader) {
		String sourceFile;
		int line;
		String className;
		String methodName;
		// the format is the one given by stacktrace.js:
		// at prototype.method (url)
		// where url is http://localhost:xxxx/org/stjs/TestClass.js:row:col
		Matcher m = STACKTRACE_JS_PATTERN.matcher(location);
		if (!m.matches()) {
			// wrong pattern !?
			throw new RuntimeException("Unknown location format:" + location);
		}
		methodName = m.group(2);
		try {
			URL url = new URL(m.group(3));
			String file = url.getFile();
			String[] fileParts = file.split(":");
			sourceFile = fileParts[0].replaceAll("\\.js$", ".java");
			String cleanJsPath = url.getPath().split(":")[0];
			int jsLineNumber = Integer.valueOf(fileParts[1]);
			line = new JavascriptToJava(testClassLoader).getJavaLine(cleanJsPath, jsLineNumber);
			String stjsPropertyFile = cleanJsPath.replaceAll("\\.js$", ".stjs");
			className = getClassName(testClassLoader, stjsPropertyFile);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

		Throwable ex = isAssert ? new AssertionError(message + ", user agent: " + userAgent) : new RuntimeException(
				message + ", user agent: " + userAgent);
		StackTraceElement[] stackTrace = new StackTraceElement[1];
		stackTrace[0] = new StackTraceElement(className, methodName, sourceFile, line);
		ex.setStackTrace(stackTrace);
		return ex;
	}

	@Override
	public String toString() {
		return "TestResult [message=" + message + ", location=" + location + ", userAgent=" + userAgent + "]";
	}

}
