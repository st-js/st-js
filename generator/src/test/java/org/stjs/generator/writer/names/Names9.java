package org.stjs.generator.writer.names;

public class Names9 {
	static class Inner {
		public static int staticMethod() {
			return 2;
		}
	}

	public int method() {
		return Inner.staticMethod();
	}
}
