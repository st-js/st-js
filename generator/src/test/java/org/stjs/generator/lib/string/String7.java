package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String7 {
	public static int main(String[] args) {
		int result = "abc".compareToIgnoreCase("B");
		$js("console.log(result)");
		return 1;
	}
}
