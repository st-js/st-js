package org.stjs.generator.writer.methods;

public class Methods2 {
	@SuppressWarnings("unused")
	private int privateMethod(String arg1, String arg2) {
		return 0;
	}

	public int method() {
		return privateMethod("", "");
	}
}
