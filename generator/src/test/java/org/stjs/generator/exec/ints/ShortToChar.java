package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class ShortToChar {
	public static final short NEG_SHORT = -31001;

	public static char method(short a) {
		return (char) val(a);
	}

	private static short val(short a) {
		return a;
	}

	public static int main(String[] args) {
		int result = ShortToChar.method(NEG_SHORT);
		$js("console.log(result)");
		return 1;
	}
}
