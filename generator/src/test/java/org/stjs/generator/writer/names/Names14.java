package org.stjs.generator.writer.names;

public class Names14 {
	public static int staticField = 2;

	private static Names14 instance() {
		return new Names14();
	}

	public int method() {
		return instance().staticField;
	}
}
