package org.stjs.generator.plugin.java8.writer.lambda;

import org.stjs.javascript.functions.Function0;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Lambda6b {
	private int outerMethod() {
		return 3;
	}

	public int method() {
		Function0<Integer> c = () -> outerMethod() + 1;
		return c.$invoke();
	}

	public static int main(String[] args) {
		int result = new Lambda6b().method();

		$js("console.log(result)");
		return result;
	}
}
