package org.stjs.generator.exec.ints;

public class IntToShortMin {
	public static final int MAX_INT = 2147483647;
	public static final int MIN_INT = -2147483648;

	public static short method(int a) {
		return (short) a;
	}

	public static int main(String[] args) {
		return IntToShortMin.method(MIN_INT);
	}
}
