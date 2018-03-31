package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Ints7 {
	public int method() {
		int a = 500;
		int b = 200;
		int c = 1;
		a /= b + c;
		return a;
	}

	public static int main(String[] args) {
		int result = new Ints7().method();
		$js("console.log(result)");
		return 1;
	}
}
