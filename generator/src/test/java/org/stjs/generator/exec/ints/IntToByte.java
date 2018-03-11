package org.stjs.generator.exec.ints;

public class IntToByte {
	public static final int MAX_INT = 2147483647;
	public static final int MIN_INT = -2147483648;

	public static byte method(int a) {
		return (byte) val(a);
	}

	private static int val(int a) {
		return a;
	}

	public static int main(String[] args) {
		return IntToByte.method(MAX_INT);
	}
}
