package org.stjs.generator.exec.ints;

public class ByteMath {
	public static final byte B1 = 100;
	public static final byte B2 = 50;

	public static byte sum(byte b1, byte b2) {
		return (byte) (val(b1) + b2);
	}

	private static byte val(byte a) {
		return a;
	}

	public static int main(String[] args) {
		return ByteMath.sum(B1, B2);
	}
}
