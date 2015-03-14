package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Callback0;

public class Lambda9 {
	public static void calculate(Callback0 c) {
	}

	public void method() {
		for (int i = 0; i < 10; ++i) {
			calculate(() -> {
				int a = 0;
				int n = a + 1;
			});
		}
	}

}
