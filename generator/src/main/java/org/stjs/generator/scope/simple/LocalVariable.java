package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.classloader.ClassWrapper;

public class LocalVariable implements Variable {

	private final ClassWrapper type;
	private final String name; 
	
	public LocalVariable(ClassWrapper type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public ClassWrapper getType() {
		return type;
	}
	public String getName() {
		return name;
	}
}
