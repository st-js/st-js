package org.stjs.generator.writer.typedarrays;

public class EnhancedForLoopArray {

	static String method() {
		String[] arr = { "hello", "world" };
		String result = "";
		for (String string : arr) {
			result += string;
		}
		return result;
	}

	public static String main(String[] args) {
		return EnhancedForLoopArray.method();
	}

}
