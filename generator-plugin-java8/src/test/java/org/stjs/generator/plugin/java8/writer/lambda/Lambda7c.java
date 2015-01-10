package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Callback0;
import org.stjs.javascript.functions.Callback1;

public class Lambda7c {
	public static void calculate(Callback1<Integer> c) {
	}

	public static void execute(Callback0 c) {

	}

	public void method() {
		execute(() -> {
			int x = 0;
			for (int i = 0; i < 10; ++i) {
				calculate((b) -> {
					int n = b + x;
				});
			}
		});
	}
}
