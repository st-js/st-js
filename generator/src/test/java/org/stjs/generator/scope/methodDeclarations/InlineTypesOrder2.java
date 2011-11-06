package org.stjs.generator.scope.methodDeclarations;

import static org.stjs.javascript.JSStringAdapter.replace;

import org.stjs.javascript.RegExp;
import org.stjs.javascript.functions.Function1;

public class InlineTypesOrder2 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String x = replace("abc", new RegExp("\\{\\}", "g"), new Function1<String, String>() {
			public String $invoke(String m) {
				return m;
			}
		});
	}
}
