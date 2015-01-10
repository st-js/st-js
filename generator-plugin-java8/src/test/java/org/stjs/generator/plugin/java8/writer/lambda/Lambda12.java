package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Callback1;

public class Lambda12 {
	public static void execute(Callback1<Integer> c) {

	}

	public void test() {
		execute((m) -> {
			int n = m;
		});
	}
}
