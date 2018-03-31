package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String12 {
	public static boolean main(String[] args) {
		boolean result = "abca".regionMatches(1, "bc", 0, 2);
		$js("console.log(result)");
		return result;
	}
}
