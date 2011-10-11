package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.classloader.ClassWrapper;

public class ClassScope extends AbstractScope {

	private final ClassWrapper clazz;
	
	ClassScope(ClassWrapper clazz, Scope parent) {
		super(parent);
		this.clazz = clazz;
	}

	@Override
	public void apply(ScopeVisitor visitor) {
		visitor.apply(this);
	}

	public ClassWrapper getClazz() {
		return clazz;
	}

}
