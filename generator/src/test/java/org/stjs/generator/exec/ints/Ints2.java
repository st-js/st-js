package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Ints2 {
	public long method(long a) {
		return a;
	}

	public static long main(String[] args) {
		long result = new Ints2().method((long) 2.3);
		$js("console.log(result)");
		return result;
	}
}
