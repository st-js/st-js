package org.stjs.generator.scope.methodDeclarations.sub;

import org.stjs.javascript.functions.Function1;

public class MethodDeclarations2<T> {

	public static class Inner<T> {
		public Inner<T> method(Function1<T, Object> t) {
			return this;
		}

		public static class A {

		}
	}

}
