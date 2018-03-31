package org.stjs.generator.lib.string;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class String9 {
	public static int main(String[] args) {
		int result = "abc".codePointAt(1);
		$js("console.log(result)");
		return 1;
	}
}
