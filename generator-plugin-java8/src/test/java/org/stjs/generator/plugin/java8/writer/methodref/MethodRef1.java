package org.stjs.generator.plugin.java8.writer.methodref;

import org.stjs.javascript.functions.Function1;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class MethodRef1 {

	private static int inc(int i) {
		return i + 1;
	}

	private static int calculate(Function1<Integer, Integer> f) {
		return f.$invoke(0);
	}

	public static int main(String[] args) {
		int result = calculate(MethodRef1::inc);
		$js("console.log(result)");
		return result;
	}
}
