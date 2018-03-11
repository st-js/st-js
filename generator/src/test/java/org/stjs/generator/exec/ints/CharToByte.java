package org.stjs.generator.exec.ints;

public class CharToByte {
	public static final char BIG_CHAR = 33001;

	public static byte method(char a) {
		return val(a);
	}

	private static byte val(char a) {
		return (byte) a;
	}

	public static int main(String[] args) {
		return CharToByte.method(BIG_CHAR);
	}
}
