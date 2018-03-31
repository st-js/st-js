package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class FloatToInt {
	public static final float BIG_FLOAT = 2644245094.1f;

	public static int method(float a) {
		return (int) val(a);
	}

	private static float val(float a) {
		return a;
	}

	public static int main(String[] args) {
		int result = FloatToInt.method(BIG_FLOAT);
		$js("console.log(result)");
		return 1;
	}
}
