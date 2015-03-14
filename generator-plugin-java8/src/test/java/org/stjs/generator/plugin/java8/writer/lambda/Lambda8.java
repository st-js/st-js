package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Callback0;

public class Lambda8 {
	public static void calculate(Callback0 c) {
	}

	public void method() {
		final int a = 0;
		for (int i = 0; i < 10; ++i) {
			calculate(() -> {
				int n = a + 1;
			});
		}
	}

}
