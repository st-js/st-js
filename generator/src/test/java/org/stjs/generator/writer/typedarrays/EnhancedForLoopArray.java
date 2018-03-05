package org.stjs.generator.writer.typedarrays;

public class EnhancedForLoopArray {

	private static int counter;

	private static String[] makeArray() {
		counter++;
		return new String[]{ "hello", "world" };
	}

	static String method() {
		counter = 0;
		String result = "";
		for (String string : makeArray()) {
			result += string;
		}
		return result + counter;
	}

	public static String main(String[] args) {
		return EnhancedForLoopArray.method();
	}

}
