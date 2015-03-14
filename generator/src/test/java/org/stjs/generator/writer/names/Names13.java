package org.stjs.generator.writer.names;

public class Names13 {
	public static int staticMethod() {
		return 2;
	}

	private static Names13 instance() {
		return new Names13();
	}

	public int method() {
		return instance().staticMethod();
	}
}
