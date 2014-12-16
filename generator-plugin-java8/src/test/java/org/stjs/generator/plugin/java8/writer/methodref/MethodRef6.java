package org.stjs.generator.plugin.java8.writer.methodref;

import org.stjs.javascript.functions.Callback0;

public class MethodRef6 {
	private MethodRef6 field;

	public void method() {
		//
	}

	private static void calculate(Callback0 c) {
		c.$invoke();
	}

	public void test() {
		calculate(field::method);
	}
}
