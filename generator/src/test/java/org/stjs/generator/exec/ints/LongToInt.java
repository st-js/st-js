package org.stjs.generator.exec.ints;

public class LongToInt {
	public static final long BIG_LONG = 6442450941L;
	public static final long NEG_BIG_LONG = -6442450941L;

	public static int method(long a) {
		return (int) val(a);
	}

	private static long val(long a) {
		return a;
	}

	public static int main(String[] args) {
		return LongToInt.method(BIG_LONG);
	}
}
