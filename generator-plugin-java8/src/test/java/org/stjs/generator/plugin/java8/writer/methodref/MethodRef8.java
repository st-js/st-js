package org.stjs.generator.plugin.java8.writer.methodref;

import org.stjs.javascript.functions.Callback0;

public class MethodRef8 {
	public MethodRef8 x;

	public void method() {
		//
	}

	public MethodRef8 method2() {
		return this;
	}

	private static void calculate(Callback0 c) {
		c.$invoke();
	}

	public void test() {
		calculate(x.x.method2()::method);
	}

}
