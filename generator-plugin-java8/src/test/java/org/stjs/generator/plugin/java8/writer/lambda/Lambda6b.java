package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Function0;

public class Lambda6b {
	private int outerMethod() {
		return 3;
	}

	public int method() {
		Function0<Integer> c = () -> outerMethod() + 1;
		return c.$invoke();
	}

	public static int main(String[] args) {
		return new Lambda6b().method();
	}
}
