package org.stjs.generator.plugin.java8.writer.methodref;

import org.stjs.javascript.functions.Callback0;

public class MethodRef5 {

	public void method() {
		//
	}

	private static void calculate(Callback0 c) {
		c.$invoke();
	}

	public void test() {
		calculate(this::method);
	}
}
