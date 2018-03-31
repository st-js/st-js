package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class LongToByte {
	public static final long BIG_LONG = 6442450941L;
	public static final long SMALL_LONG = 33000L;
	public static final long NEG_BIG_LONG = -6442450941L;

	public static byte method(long a) {
		return (byte) val(a);
	}

	private static long val(long a) {
		return a;
	}

	public static int main(String[] args) {
		int result = LongToByte.method(BIG_LONG);
		$js("console.log(result)");
		return 1;
	}
}
