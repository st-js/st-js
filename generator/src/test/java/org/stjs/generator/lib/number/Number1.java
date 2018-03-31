package org.stjs.generator.lib.number;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Number1 {
	public static int main(String[] args) {
		int result = Integer.parseInt("123");
		$js("console.log(result)");
		return 1;
	}
}
