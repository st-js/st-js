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
