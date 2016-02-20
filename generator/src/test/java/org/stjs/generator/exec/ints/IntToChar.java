package org.stjs.generator.exec.ints;

public class IntToChar {
	public static final int MAX_INT = 2147483647;
	public static final int MIN_INT = -2147483648;

	public static char method(int a) {
		return (char) val(a);
	}

	private static int val(int a) {
		return a;
	}

	public static int main(String[] args) {
		return IntToChar.method(MAX_INT);
	}
}
