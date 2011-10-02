package test.generator.globalScope;

import static test.generator.globalScope.Globals.field;

public class GlobalScope4 {
	public void test() {
		@SuppressWarnings("unused")
		String s = field;
	}
}
