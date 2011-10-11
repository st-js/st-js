package org.stjs.generator.scope.simple;

import java.lang.reflect.Field;

import org.stjs.generator.scope.classloader.ClassWrapper;

public class FieldVariable implements Variable {
	private final Field field;

	public FieldVariable(Field field) {
		this.field = field;
	}

	public String getName() {
		return field.getName();
	}

	public ClassWrapper getType() {
		return ClassWrapper.wrap(field.getType());
	}

	public Field getField() {
		return field;
	}
}