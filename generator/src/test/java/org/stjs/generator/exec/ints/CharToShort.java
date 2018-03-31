package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class CharToShort {
	public static final char BIG_CHAR = 33001;

	public static short method(char a) {
		return (short) val(a);
	}

	private static char val(char a) {
		return a;
	}

	public static int main(String[] args) {
		int result = CharToShort.method(BIG_CHAR);
		$js("console.log(result)");
		return 1;
	}
}
