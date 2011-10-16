package org.stjs.generator.scope.classloader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeWrappers {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static TypeWrapper wrap(Type type) {
		if (type instanceof TypeVariable) {
			return new TypeVariableWrapper((TypeVariable) type);
		}
		if (type instanceof ParameterizedType) {
			return new ParameterizedTypeWrapper((ParameterizedType) type);
		}

		if (type instanceof Class) {
			return new ClassWrapper((Class) type);
		}
		throw new IllegalArgumentException("Cannot handle the type:" + type);
	}
}
