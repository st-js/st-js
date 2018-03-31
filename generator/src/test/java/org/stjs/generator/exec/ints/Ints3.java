package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Ints3 {
	public int method() {
		return 3 / 2 + 1;
	}

	public static long main(String[] args) {
		long result = new Ints3().method();
		$js("console.log(result)");
		return result;
	}
}
