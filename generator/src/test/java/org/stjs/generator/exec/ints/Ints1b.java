package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Ints1b {
	public long method(long a) {
		return a;
	}

	public static long main(String[] args) {
		long result = new Ints1b().method((long) 1413492112445.3);
		$js("console.log(result)");
		return result;
	}
}
