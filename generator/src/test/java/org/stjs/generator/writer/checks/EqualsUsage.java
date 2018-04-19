package org.stjs.generator.writer.checks;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class EqualsUsage {
	public boolean main(int[] args) {
		SomeClass first = new SomeClass("someString");
		SomeClass second = new SomeClass("someOtherString");

		Boolean result = first.equals(second);
		$js("console.log(result)");
		return result;
	}
}
