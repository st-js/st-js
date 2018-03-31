package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class LongToChar {
	public static final long BIG_LONG = 6442450941L;
	public static final long NEG_BIG_LONG = -6442450941L;

	public static char method(long a) {
		return (char) val(a);
	}

	private static long val(long a) {
		return a;
	}

	public static int main(String[] args) {
		int result = LongToChar.method(BIG_LONG);
		$js("console.log(result)");
		return 1;
	}
}
