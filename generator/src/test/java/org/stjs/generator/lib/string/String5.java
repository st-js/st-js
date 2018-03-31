package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String5 {
	public static boolean main(String[] args) {
		boolean result = "ababc".matches("a+bc");
		$js("console.log(result)");
		return result;
	}
}
