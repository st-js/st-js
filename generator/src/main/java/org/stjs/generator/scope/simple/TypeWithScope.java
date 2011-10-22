package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.classloader.TypeWrapper;

public class TypeWithScope {
	private final Scope scope;
	private final TypeWrapper type;

	TypeWithScope(Scope scope, TypeWrapper type) {
		this.scope = scope;
		this.type = type;
	}

	public Scope getScope() {
		return scope;
	}

	public TypeWrapper getType() {
		return type;
	}

}
