package org.stjs.testing.driver;

public class BrowserConnection {
	/**
	 * the result of the last test
	 */
	private String result;

	private long lastTestId;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public long getLastTestId() {
		return lastTestId;
	}

	public void setLastTestId(long lastTestId) {
		this.lastTestId = lastTestId;
	}

	@Override
	public String toString() {
		return "BrowserConnection [result=" + result + ", lastTestId=" + lastTestId + "]";
	}

}
