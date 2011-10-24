package test.generator.globalScope;

import static org.stjs.javascript.JSStringAdapter.replace;

import org.stjs.javascript.RegExp;

public class GlobalScope5 {
	public void test() {
		replace("a", new RegExp("abc"), "a" + "b" + "c");
	}
}
