package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Function0;

public class Lambda6 {
	private int outerMethod() {
		return 0;
	}

	public void method() {
		Function0<Integer> c = () -> outerMethod() + 1;
	}

}
