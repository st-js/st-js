package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String1 {
	public static boolean main(String[] args) {
		boolean result = "abc".startsWith("ab");
		$js("console.log(result)");
		return result;
	}
}
