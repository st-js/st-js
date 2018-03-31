package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String10 {
	public static String main(String[] args) {
		String result = "abca".replaceAll("a", "x");
		$js("console.log(result)");
		return result;
	}
}
