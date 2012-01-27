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

import java.util.ArrayList;
import java.util.List;

public class TestResultCollection {
	private final List<TestResult> results = new ArrayList<TestResult>();

	public synchronized void addResult(TestResult result) {
		results.add(result);
		notify();
	}

	public synchronized boolean isOk() {
		for (TestResult result : results) {
			if (!result.isOk()) {
				return false;
			}
		}
		return true;
	}

	public synchronized TestResult getResult(int i) {
		return results.get(i);
	}

	public synchronized int size() {
		return results.size();
	}

	/**
	 * 
	 * @param className
	 * @param methodName
	 * @return the exception for the first wrong result
	 */
	public synchronized AssertionError buildException(String className, String methodName) {
		for (TestResult result : results) {
			if (!result.isOk()) {
				return result.buildException(className, methodName);
			}
		}
		return null;
	}

}
