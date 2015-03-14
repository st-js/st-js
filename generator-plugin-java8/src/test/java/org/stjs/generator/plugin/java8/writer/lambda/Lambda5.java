package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Function0;

public class Lambda5 {
	private int field;

	public void method() {
		Function0<Integer> c = () -> field + 1;
	}

}
