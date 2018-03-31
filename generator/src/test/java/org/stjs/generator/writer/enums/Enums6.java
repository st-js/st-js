package org.stjs.generator.writer.enums;

import static org.stjs.javascript.JSObjectAdapter.$js;

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
		$js("console.log(result)");
		return result;
	}
}
