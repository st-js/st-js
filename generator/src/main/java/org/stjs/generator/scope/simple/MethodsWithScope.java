package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.classloader.MethodWrapper;

public class MethodsWithScope {
	private final Scope scope;
	private final MethodWrapper method;

	MethodsWithScope(Scope scope, MethodWrapper method) {
		this.scope = scope;
		this.method = method;
	}

	public Scope getScope() {
		return scope;
	}

	public MethodWrapper getMethod() {
		return method;
	}

}
