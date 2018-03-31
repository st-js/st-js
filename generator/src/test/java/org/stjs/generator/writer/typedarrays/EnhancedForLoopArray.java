package org.stjs.generator.writer.typedarrays;

import static org.stjs.javascript.JSObjectAdapter.$js;

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
		String result = EnhancedForLoopArray.method();
		$js("console.log(result)");
		return result;
	}

}
