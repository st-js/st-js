package org.stjs.generator.scope.simple;

import static org.stjs.generator.scope.classloader.ClassWrapper.wrap;

import java.lang.reflect.Field;

import org.stjs.generator.scope.classloader.ClassWrapper;

public class FieldVariable implements Variable {

	private final Field field;
	
	public FieldVariable(Field field) {
		this.field = field;
	}
	
	public ClassWrapper getType() {
		return wrap(field.getType());
	}
	
	public String getName() {
		return field.getName();
	}

	public Field getField() {
		return field;
	}
}
