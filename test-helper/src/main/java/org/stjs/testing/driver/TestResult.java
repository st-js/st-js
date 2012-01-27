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

public class TestResult {
	private final String message;
	private final int line;
	private final String file;
	private final String userAgent;

	public TestResult(String userAgent, String message, String location) {
		this.userAgent = userAgent;
		this.message = message;
		if ((location != null) && !location.isEmpty()) {
			String[] locData = location.split(":");
			if (locData.length == 2) {
				file = locData[0];
				line = Integer.parseInt(locData[1]);
			} else {
				file = "none";
				line = -1;
			}
		} else {
			file = "none";
			line = -1;
		}
	}

	public String getMessage() {
		return message;
	}

	public int getLine() {
		return line;
	}

	public String getFile() {
		return file;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public boolean isOk() {
		return "OK".equals(message);
	}

	public AssertionError buildException(String className, String methodName) {
		AssertionError ex = new AssertionError(message + ", user agent: " + userAgent);
		if (line >= 0) {
			StackTraceElement[] stackTrace = new StackTraceElement[1];
			stackTrace[0] = new StackTraceElement(className, methodName, file, line);
			ex.setStackTrace(stackTrace);
		}
		return ex;
	}

	@Override
	public String toString() {
		return "TestResult [message=" + message + ", line=" + line + ", file=" + file + ", userAgent=" + userAgent
				+ "]";
	}

}
