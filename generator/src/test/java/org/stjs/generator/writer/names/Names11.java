package org.stjs.generator.writer.names;

public class Names11 {
	public static int staticMethod() {
		return 2;
	}

	public int method() {
		Names11 n = new Names11();
		return n.staticMethod();
	}
}
