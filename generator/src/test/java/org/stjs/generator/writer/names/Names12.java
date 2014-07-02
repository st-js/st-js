package org.stjs.generator.writer.names;

public class Names12 {
	public static int staticMethod() {
		return 2;
	}

	public int method() {
		return new Names12().staticMethod();
	}
}
