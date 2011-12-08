package org.stjs.generator.scope.methodDeclarations;

import org.stjs.generator.scope.methodDeclarations.sub.MethodDeclarations2;
import org.stjs.javascript.functions.Function1;

public class MethodDeclarations1 {
	private Function1<String, Object> f() {
		return null;
	}

	public void test() {
		MethodDeclarations2.Inner<String> s = new MethodDeclarations2.Inner<String>();
		s.method(f());

		MethodDeclarations2.Inner.A a;
	}
}
