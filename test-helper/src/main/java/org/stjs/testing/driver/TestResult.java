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

import org.stjs.generator.sourcemap.JavascriptToJava;

public class TestResult {
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

	public Throwable buildException(ClassLoader testClassLoader) {
		Throwable ex = isAssert ? new AssertionError(message + ", user agent: " + userAgent) : new RuntimeException(
				message + ", user agent: " + userAgent);
		StackTraceElement[] stackTrace = new JavascriptToJava(testClassLoader).buildStacktrace(location, ";");
		ex.setStackTrace(stackTrace);
		return ex;
	}

	@Override
	public String toString() {
		return "TestResult [message=" + message + ", location=" + location + ", userAgent=" + userAgent + "]";
	}

}
