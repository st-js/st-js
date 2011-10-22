package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.classloader.TypeWrapper;

public class ParameterVariable implements Variable {

	private final TypeWrapper type;
	private final String name;

	public ParameterVariable(TypeWrapper type, String name) {
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
