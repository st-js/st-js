package org.stjs.testing.driver;

import java.util.ArrayList;
import java.util.List;

public class TestResultCollection {
	private final List<TestResult> results = new ArrayList<TestResult>();

	public void addResult(TestResult result) {
		results.add(result);
	}

	public boolean isOk() {
		for (TestResult result : results) {
			if (!result.isOk()) {
				return false;
			}
		}
		return true;
	}

	public TestResult getResult(int i) {
		return results.get(i);
	}

	public int size() {
		return results.size();
	}
}
