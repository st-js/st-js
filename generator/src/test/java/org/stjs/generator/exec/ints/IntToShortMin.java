package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class IntToShortMin {
	public static final int MAX_INT = 2147483647;
	public static final int MIN_INT = -2147483648;

	public static short method(int a) {
		return (short) a;
	}

	public static int main(String[] args) {
		int result = IntToShortMin.method(MIN_INT);
		$js("console.log(result)");
		return 1;
	}
}
