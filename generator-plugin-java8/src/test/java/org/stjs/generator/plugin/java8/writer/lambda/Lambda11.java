package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Callback0;

public class Lambda11 {
	public static void execute(Callback0 c) {

	}

	public static int method() {
		return 0;
	}

	public void test() {
		execute(() -> {
			int n = method();
		});
	}
}
