package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Ints5 {
	public double method() {
		return 3 / 2.0 + 1;
	}

	public static double main(String[] args) {
		double result = new Ints5().method();
		$js("console.log(result)");
		return 1;
	}
}
