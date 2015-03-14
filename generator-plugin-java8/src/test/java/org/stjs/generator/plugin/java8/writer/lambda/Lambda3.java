package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Callback1;

public class Lambda3 {
	public static void method(Callback1<Integer> f) {

	}

	public static void main(String[] args) {
		method((x) -> {
			int y = x;
		});
	}
}
