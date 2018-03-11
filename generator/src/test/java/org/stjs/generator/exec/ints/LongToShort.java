package org.stjs.generator.exec.ints;

public class LongToShort {
	public static final long BIG_LONG = 6442450941L;
	public static final long NEG_BIG_LONG = -6442450941L;

	public static short method(long a) {
		return (short) val(a);
	}

	private static long val(long a) {
		return a;
	}

	public static int main(String[] args) {
		return LongToShort.method(BIG_LONG);
	}
}
