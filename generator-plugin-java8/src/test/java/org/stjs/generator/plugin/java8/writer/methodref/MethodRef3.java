package org.stjs.generator.plugin.java8.writer.methodref;

import org.stjs.javascript.functions.Function1;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class MethodRef3 {
	private final int n = 3;

	public int inc2(int i) {
		return i + n;
	}

	private static int calculate(Function1<Integer, Integer> f, int n) {
		return f.$invoke(n);
	}

	public static int main(String[] args) {
		MethodRef3 ref = new MethodRef3();
		int result = calculate(ref::inc2, 1);
		$js("console.log(result)");
		return result;
	}
}
