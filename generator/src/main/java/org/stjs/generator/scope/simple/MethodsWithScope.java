package org.stjs.generator.scope.simple;

import java.lang.reflect.Method;
import java.util.Collection;

public class MethodsWithScope {
	private final Scope scope;
	private final Collection<Method> methods;

	MethodsWithScope(Scope scope, Collection<Method> methods) {
		this.scope = scope;
		this.methods = methods;
	}

	public Scope getScope() {
		return scope;
	}

	public Collection<Method> getMethods() {
		return methods;
	}

}
