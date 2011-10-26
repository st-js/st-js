package org.stjs.testing.driver;

public class TestResult {
	private final String message;
	private final int line;
	private final String file;

	public TestResult(String message, String location) {
		this.message = message;
		if (location != null && !location.isEmpty()) {
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

	public boolean isOk() {
		return "OK".equals(message);
	}

	public AssertionError buildException(String className, String methodName) {
		AssertionError ex = new AssertionError(message);
		if (line >= 0) {
			StackTraceElement[] stackTrace = new StackTraceElement[1];
			stackTrace[0] = new StackTraceElement(className, methodName, file, line);
			ex.setStackTrace(stackTrace);
		}
		return ex;
	}

	@Override
	public String toString() {
		return "TestResult [message=" + message + ", line=" + line + ", file=" + file + "]";
	}

}
