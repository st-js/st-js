package org.stjs.generator.exec.ints;

public class CharLiteralToByte {
	public static final char CYRILLIC_IA = 'я';

	public static byte method(char a) {
		return val(a);
	}

	private static byte val(char a) {
		return (byte) a;
	}

	public static int main(String[] args) {
		return CharLiteralToByte.method(CYRILLIC_IA);
	}
}
