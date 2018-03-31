package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String8 {
	public static boolean main(String[] args) {
		boolean result = "abc".equalsIgnoreCase("ABc");
		$js("console.log(result)");
		return result;
	}
}
