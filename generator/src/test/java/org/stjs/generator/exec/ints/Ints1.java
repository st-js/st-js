package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Ints1 {
	public int method(int a) {
		return a;
	}

	public static int main(String[] args) {
		int result = new Ints1().method((int) 2.3);
		$js("console.log(result)");
		return 1;
	}
}
