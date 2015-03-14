package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Function0;

public class Lambda2 {
	public static void method(Function0<Integer> f) {

	}

	public static void main(String[] args) {
		method(() -> 1);
	}
}
