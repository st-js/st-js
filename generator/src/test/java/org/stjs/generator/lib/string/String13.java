package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String13 {
	public static boolean main(String[] args) {
		boolean result = "abca".regionMatches(true, 1, "BC", 0, 2);
		$js("console.log(result)");
		return result;
	}
}
