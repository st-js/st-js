package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.classloader.ClassWrapper;

public class TypeWithScope {
	private final Scope scope;
	private final ClassWrapper clazz;

	TypeWithScope(Scope scope, ClassWrapper clazz) {
		this.scope = scope;
		this.clazz = clazz;
	}

	public Scope getScope() {
		return scope;
	}

	public ClassWrapper getClazz() {
		return clazz;
	}

}
