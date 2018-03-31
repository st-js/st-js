package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Ints6 {
	public int method() {
		int a = 500;
		int b = 200;
		a /= b;
		return a;
	}

	public static int main(String[] args) {
		int result = new Ints6().method();
		$js("console.log(result)");
		return 1;
	}
}
