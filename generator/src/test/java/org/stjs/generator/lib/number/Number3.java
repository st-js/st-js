package org.stjs.generator.lib.number;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Number3 {
	public static int main(String[] args) {
		int result = Integer.valueOf("123");
		$js("console.log(result)");
		return 1;
	}
}
