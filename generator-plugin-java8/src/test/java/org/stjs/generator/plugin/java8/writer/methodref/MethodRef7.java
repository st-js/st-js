package org.stjs.generator.plugin.java8.writer.methodref;

import org.stjs.javascript.functions.Callback0;

public class MethodRef7 {
	public void method() {
		//
	}

	public MethodRef7 method2() {
		return this;
	}

	private static void calculate(Callback0 c) {
		c.$invoke();
	}

	public void test() {
		calculate(method2()::method);
	}
}
