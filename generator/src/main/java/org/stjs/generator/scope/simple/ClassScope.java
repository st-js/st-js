package org.stjs.generator.scope.simple;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.scope.classloader.ClassWrapper;

public class ClassScope extends AbstractScope {

	private final ClassWrapper clazz;

	ClassScope(ClassWrapper clazz, Scope parent, GenerationContext context) {
		super(parent, context);
		this.clazz = clazz;
	}

	@Override
	public <T> T apply(ScopeVisitor<T> visitor) {
		return visitor.apply(this);
	}

	public ClassWrapper getClazz() {
		return clazz;
	}

}
