package org.stjs.generator.writer.typedarrays;

public class BooleanArray {
	public static int method() {
		int _i = 1;
		boolean[] a = new boolean[3];
		a[0] = 42 != Float.NaN;
		a[1] = 42 != 43;
		a[2] = 42. == 41 + _i;

		boolean truth = true;
		for (int i = 0; i < a.length; i++) {
			truth &= a[i];
		}
		return truth ? 42 : 43;
	}

	public static int main(String[] args) {
		return BooleanArray.method();
	}

}
