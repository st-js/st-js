package org.stjs.generator.writer.globalScope;

import static org.stjs.javascript.JSStringAdapterBase.replace;

import org.stjs.javascript.RegExp;

public class GlobalScope5 {
	public void test() {
		replace("a", new RegExp("abc"), "a" + "b" + "c");
	}
}
