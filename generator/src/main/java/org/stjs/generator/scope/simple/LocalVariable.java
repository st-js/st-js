package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.classloader.TypeWrapper;

public class LocalVariable implements Variable {

	private final TypeWrapper type;
	private final String name;

	public LocalVariable(TypeWrapper type, String name) {
		this.type = type;
		this.name = name;
	}

	public TypeWrapper getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
