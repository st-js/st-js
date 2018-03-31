package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String6 {
	public static int main(String[] args) {
		int result = "abc".compareTo("b");
		$js("console.log(result)");
		return 1;
	}
}
