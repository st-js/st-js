package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Callback1;

public class Lambda7b {
	public static void calculate(Callback1<Integer> c) {
	}

	public void method() {
		for (int i = 0; i < 10; ++i) {
			calculate((b) -> {
				int n = b + 1;
			});
		}
	}

}
