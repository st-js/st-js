/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

public final class TypeWrappers {

	private static final Map<Type, TypeWrapper> CACHE = new HashMap<Type, TypeWrapper>();

	private TypeWrappers() {
		//
	}

	public static void clearCache() {
		CACHE.clear();
	}

	public static TypeWrapper[] wrap(Type[] types) {
		TypeWrapper[] wrapped = new TypeWrapper[types.length];
		for (int i = 0; i < types.length; ++i) {
			wrapped[i] = TypeWrappers.wrap(types[i]);
		}
		return wrapped;
	}

	public static TypeWrapper[] wrapMore(Type... types) {
		return wrap(types);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static TypeVariableWrapper[] wrap(TypeVariable[] types) {
		TypeVariableWrapper[] wrapped = new TypeVariableWrapper[types.length];
		for (int i = 0; i < types.length; ++i) {
			wrapped[i] = TypeWrappers.wrap(types[i]);
		}
		return wrapped;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static TypeWrapper wrapNoCache(Type type) {
		if (type instanceof TypeVariable) {
			return new TypeVariableWrapper((TypeVariable) type);
		}
		if (type instanceof ParameterizedType) {
			return new ParameterizedTypeWrapper((ParameterizedType) type);
		}
		if (type instanceof WildcardType) {
			return new WildcardTypeWrapper((WildcardType) type);
		}
		if (type instanceof GenericArrayType) {
			return new GenericArrayTypeWrapper((GenericArrayType) type);
		}
		if (type instanceof Class) {
			return new ClassWrapper((Class) type);
		}
		throw new IllegalArgumentException("Cannot handle the type:" + type);
	}

	public static TypeWrapper wrap(Type type) {
		if (type == null) {
			return null;
		}
		TypeWrapper w = CACHE.get(type);
		if (w != null) {
			return w;
		}
		w = wrapNoCache(type);
		CACHE.put(type, w);
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
