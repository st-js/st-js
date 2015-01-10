package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.functions.Callback0;

public class Lambda13 {
	@SyntheticType
	public static class Inner {
		public int x;
	}

	public static void execute(Callback0 c) {

	}

	public void test() {
		execute(() -> {
			Inner n = new Inner() {
				{
					x = 1;
				}
			};
		});
	}
}
