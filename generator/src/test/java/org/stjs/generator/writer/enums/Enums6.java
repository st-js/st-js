package org.stjs.generator.writer.enums;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Enums6 {
	public enum Value {
		a, b, c;
	}

	@SuppressWarnings("unused")
	public static String main(String[] args) {
		// TODO :: add checks to disable ".values" ".name" and ".ordinal" calls on enums
		String result = "";
		result += "a:" + Value.a;
		result += "b:" + Value.b;
		result += "c:" + Value.c;
		$js("console.log(result)");
		return result;
	}
}
