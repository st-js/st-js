package org.stjs.generator.scope.methodDeclarations;

import org.stjs.javascript.functions.Function1;

public class MethodDeclarations4<T> {

	public static class Inner<T> {
		public Inner<T> method(Function1<T, Object> t) {
			return this;
		}
	}

}
