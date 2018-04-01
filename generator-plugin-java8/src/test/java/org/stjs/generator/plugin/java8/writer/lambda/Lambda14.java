package org.stjs.generator.plugin.java8.writer.lambda;

import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSObjectAdapter.$js;

import org.stjs.javascript.JSFunctionAdapter;
import org.stjs.javascript.functions.Function1;

public class Lambda14 {

	public static class Inner {
		public int inner() {
			return 5;
		}
	}

	public int method(int n) {
		return n + 10;
	}

	public Function1<Inner, Integer> func() {
		Function1<Inner, Integer> f = THIS -> method(THIS.inner());
		return f;
	}

	public static int main(String[] args) {
		Lambda14 obj = new Lambda14();
		int result = JSFunctionAdapter.apply(obj.func(), new Inner(), $array());
		$js("console.log(result)");
		return result;
	}

}
