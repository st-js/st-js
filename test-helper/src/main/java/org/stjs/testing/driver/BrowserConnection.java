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

/**
 * This class represents a connection from a browser to the embedded test server.
 * 
 * @author acraciun
 * 
 */
public class BrowserConnection {

	private final long id;
	private final String userAgent;

	public BrowserConnection(long id, String userAgent) {
		this.id = id;
		this.userAgent = userAgent;
	}

	public long getId() {
		return id;
	}

	public TestResult buildResult(String message, String location) {
		return new TestResult(userAgent, message, location);
	}

	public String getUserAgent() {
		return userAgent;
	}

	@Override
	public String toString() {
		return "BrowserConnection [id=" + id + ", userAgent=" + userAgent + "]";
	}

}
