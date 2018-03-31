package org.stjs.generator.writer.inlineFunctions;

import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSObjectAdapter.$js;

import org.stjs.javascript.JSFunctionAdapter;
import org.stjs.javascript.functions.Function1;

public class InlineFunctions8 {

	public int method() {
		return 10;
	}

	public static int main(String[] args) {
		Function1<InlineFunctions8, Integer> f = new Function1<InlineFunctions8, Integer>() {
			@Override
			public Integer $invoke(InlineFunctions8 THIS) {
				return THIS.method();
			}
		};

		InlineFunctions8 obj = new InlineFunctions8();
		int result = JSFunctionAdapter.apply(f, obj, $array());

		$js("console.log(result)");
		return 1;
	}

}
