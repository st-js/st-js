package org.stjs.generator.scope.classloader;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

public class TypeWrappers {
	private static final Map<Type, TypeWrapper> cache = new HashMap<Type, TypeWrapper>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static TypeWrapper wrap(Type type) {
		if (type == null) {
			return null;
		}
		TypeWrapper w = cache.get(type);
		if (w != null) {
			return w;
		}
		if (type instanceof TypeVariable) {
			w = new TypeVariableWrapper((TypeVariable) type);
		} else if (type instanceof ParameterizedType) {
			w = new ParameterizedTypeWrapper((ParameterizedType) type);
		} else if (type instanceof WildcardType) {
			w = new WildcardTypeWrapper((WildcardType) type);
		} else if (type instanceof GenericArrayType) {
			w = new GenericArrayTypeWrapper((GenericArrayType) type);
		} else if (type instanceof Class) {
			return new ClassWrapper((Class) type);
		} else {
			throw new IllegalArgumentException("Cannot handle the type:" + type);
		}
		cache.put(type, w);
		return w;
	}

	@SuppressWarnings("unchecked")
	public static <T extends GenericDeclaration> TypeVariableWrapper<T> wrap(TypeVariable<T> type) {
		return (TypeVariableWrapper<T>) wrap((Type) type);
	}

	public static ParameterizedTypeWrapper wrap(ParameterizedType type) {
		return (ParameterizedTypeWrapper) wrap((Type) type);
	}

	public static ClassWrapper wrap(Class<?> type) {
		return (ClassWrapper) wrap((Type) type);
	}
}
