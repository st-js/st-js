package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String3 {
	public static boolean main(String[] args) {
		boolean result = "abc".endsWith("bc");
		$js("console.log(result)");
		return result;
	}
}
