package org.stjs.generator.writer.typedarrays;

public class ArrayMath {
	public static int method() {
		int[] a = { 1, 2, 3, 4 };
		int[] b = { 2, 2, 2, 2 };
		int[] c = new int[4];
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			c[i] = a[i] + b[i];
			c[i]++;
			sum += c[i];
		}
		return sum;
	}

	public static int main(String[] args) {
		return ArrayMath.method();
	}

}
