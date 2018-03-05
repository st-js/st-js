package org.stjs.generator.writer.typedarrays;

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
		return EnhancedForLoopIntArray.method();
	}

}
