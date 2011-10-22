package org.stjs.generator.scope.simple;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.generator.scope.classloader.FieldWrapper;
import org.stjs.generator.scope.classloader.MethodWrapper;
import org.stjs.generator.scope.classloader.TypeWrapper;

public class ClassScope extends AbstractScope {

	private final ClassWrapper clazz;

	ClassScope(ClassWrapper clazz, Scope parent, GenerationContext context) {
		super(parent, context);
		this.clazz = clazz;
		addClassElements(clazz);
	}

	private void addClassElements(ClassWrapper c) {
		for (TypeWrapper typeParam : c.getTypeParameters()) {
			addType(typeParam);
		}
		for (FieldWrapper field : c.getDeclaredFields()) {
			addField(field);
		}
		for (MethodWrapper method : c.getDeclaredMethods()) {
			addMethod(method);
		}
	}

	@Override
	public <T> T apply(ScopeVisitor<T> visitor) {
		return visitor.apply(this);
	}

	public ClassWrapper getClazz() {
		return clazz;
	}

}
