package org.stjs.generator.writer.methods;

public class Methods10_basic_varargs {
	public static String main(String[] args) {
		String test = method(0);
		return method(0, "1", "2", "3");
	}

	public static String method(int param, String... strings) {
		String joinedStrings = "";
		for (String string : strings) {
			joinedStrings += string;
		}
		return joinedStrings;
	}
}
