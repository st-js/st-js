package org.stjs.generator.scope.methodDeclarations;

import org.stjs.javascript.functions.Function1;

public class MethodDeclarations3 {
	private Function1<String, Object> f() {
		return null;
	}

	public void test() {
		MethodDeclarations4.Inner<String> s = new MethodDeclarations4.Inner<String>();
		s.method(f());
	}
}
