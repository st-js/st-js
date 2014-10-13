package org.stjs.generator.exec.aserts;

import org.stjs.javascript.functions.Callback3;
import org.stjs.javascript.stjs.STJSAssert;

public class Asserts4 {
	private static int value = 0;

	public static int main(String[] args) {
		STJSAssert.setAssertHandler(new Callback3<String, String, String>() {
			@Override
			public void $invoke(String position, String code, String msg) {
				if (msg != null && msg.indexOf("Wrong") >= 0) {
					value = 2;
				}
			}
		});
		STJSAssert.assertStateEquals(1, 2);
		return value;
	}
}
