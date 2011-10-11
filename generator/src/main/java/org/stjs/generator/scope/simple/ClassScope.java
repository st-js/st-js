package org.stjs.generator.scope.simple;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.stjs.generator.scope.classloader.ClassWrapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;

public class ClassScope extends AbstractScope {

	private final ClassWrapper clazz;
	
	private Map<String, Field> fields = Maps.newHashMap();
	
	public void addField(Field field) {
		fields.put(field.getName(), field);
	}
	
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
	
	@VisibleForTesting
	public Field getField(String name) {
		return fields.get(name);
	}

}
