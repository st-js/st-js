package org.stjs.generator.lib.number;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Number2 {
	public static int main(String[] args) {
		int result = new Double(123.4).intValue();
		$js("console.log(result)");
		return 1;
	}
}
