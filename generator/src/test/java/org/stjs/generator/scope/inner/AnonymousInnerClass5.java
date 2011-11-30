package org.stjs.generator.scope.inner;

import org.stjs.javascript.Array;
import org.stjs.javascript.functions.Callback3;
import org.stjs.javascript.functions.Function1;

abstract public class AnonymousInnerClass5 {
	AnonymousInnerClass5() {

		Callback3<Array<String>, String, String> callback = new Callback3<Array<String>, String, String>() {
			@Override
			public void $invoke(Array<String> response, String textStatus, String jqXHR) {
				// collect strikes and expdates from the results and sort them
				method(response, new Function1<String, String>() {
					@Override
					public String $invoke(String quote) {
						return quote;
					}
				});
			}
		};

	}

	public static void method(Array<String> response, Function1<String, String> f) {

	}

}
