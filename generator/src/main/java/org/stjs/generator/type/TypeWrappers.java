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

public class TypeWrappers {
	private static final Map<Type, TypeWrapper> cache = new HashMap<Type, TypeWrapper>();

	public static TypeWrapper[] wrap(Type[] types) {
		TypeWrapper[] wrapped = new TypeWrapper[types.length];
		for (int i = 0; i < types.length; ++i) {
			wrapped[i] = TypeWrappers.wrap(types[i]);
		}
		return wrapped;
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
