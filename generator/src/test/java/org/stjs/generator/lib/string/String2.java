package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String2 {
	public static boolean main(String[] args) {
		boolean result = "abc".startsWith("bc", 1);
		$js("console.log(result)");
		return result;
	}
}
