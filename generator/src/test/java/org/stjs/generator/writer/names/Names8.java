package org.stjs.generator.writer.names;

public class Names8 {
	public static int staticMethod() {
		return 2;
	}

	public int method() {
		return org.stjs.generator.writer.names.Names8.staticMethod();
	}
}
