package org.stjs.generator.writer.typedarrays;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class EnhancedForLoopIntArray {

	static int method() {
		int[] arr = { 42, 43 };
		int result = 0;
		for (int item : arr) {
			result += item;
		}
		return result;
	}

	public static int main(String[] args) {
		int result = EnhancedForLoopIntArray.method();

		$js("console.log(result)");
		return 1;
	}

}
