package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String11 {
	public static String main(String[] args) {
		String result = "abca".replaceFirst("a", "x");
		$js("console.log(result)");
		return result;
	}
}
