package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class CharLiteralToByte {
	public static final char CYRILLIC_IA = 'я';

	public static byte method(char a) {
		return val(a);
	}

	private static byte val(char a) {
		return (byte) a;
	}

	public static int main(String[] args) {
		int result = CharLiteralToByte.method(CYRILLIC_IA);

		$js("console.log(result)");
		return 1;
	}
}
