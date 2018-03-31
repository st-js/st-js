package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Ints4 {
	public double method() {
		return 3.0 / 2 + 1;
	}

	public static double main(String[] args) {
		double result = new Ints4().method();
		$js("console.log(result)");
		return result;
	}
}
