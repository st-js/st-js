package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Function1;

public class Lambda4 {
	public static void method(Function1<String, Integer> f) {

	}

	public static void main(String[] args) {
		method((x) -> x.length() + 1);
	}
}
