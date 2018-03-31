package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String4 {
	public static boolean main(String[] args) {
		boolean result = "aaabc".matches("a+bc");
		$js("console.log(result)");
		return result;
	}
}
