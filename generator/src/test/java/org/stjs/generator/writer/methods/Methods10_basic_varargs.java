package org.stjs.generator.writer.methods;

public class Methods10_basic_varargs<T> {
	public static String main(String[] args) {
		String test = new Methods10_basic_varargs<String>().method(0);
		return new Methods10_basic_varargs<String>().method(0, "1", "2", "3");
	}

	public String method(int param, T... strings) {
		String joinedStrings = "";
		for (T string : strings) {
			joinedStrings += string;
		}
		new Methods10_basic_varargs<String>().method2("1");
		return joinedStrings;
	}

	public String method2(T... strings) {
		return strings.toString();
	}
}
