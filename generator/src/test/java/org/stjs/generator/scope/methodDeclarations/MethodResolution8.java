package org.stjs.generator.scope.methodDeclarations;

import org.stjs.javascript.functions.Function1;

public class MethodResolution8 {
	private Function1<String, Object> f() {
		return null;
	}

	public void test() {
		MethodResolution7.Inner<String> s = new MethodResolution7.Inner<String>();
		s.method(f());
	}
}
