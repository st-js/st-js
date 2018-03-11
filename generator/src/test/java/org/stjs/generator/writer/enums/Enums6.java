package org.stjs.generator.writer.enums;

public class Enums6 {
	public enum Value {
		a, b, c;
	}

	@SuppressWarnings("unused")
	public static String main(String[] args) {
		String result = "";
		for (Value v : Value.values()) {
			result += v.name() + ":" + v.ordinal();
		}
		return result;
	}
}
