package org.stjs.generator.exec.ints;

public class FloatToInt {
	public static final float BIG_FLOAT = 2644245094.1f;

	public static int method(float a) {
		return (int) val(a);
	}

	private static float val(float a) {
		return a;
	}

	public static int main(String[] args) {
		return FloatToInt.method(BIG_FLOAT);
	}
}
