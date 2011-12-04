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
package org.stjs.generator.utils;

import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.stjs.generator.type.ClassWrapper;
import org.stjs.generator.type.GenericArrayTypeImpl;
import org.stjs.generator.type.GenericArrayTypeWrapper;
import org.stjs.generator.type.PrimitiveTypes;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.javascript.annotation.Adapter;
import org.stjs.javascript.annotation.DataType;
import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.STJSBridge;

import com.google.common.primitives.Primitives;

public class ClassUtils {
	/**
	 * these are packages that don't have the annotation but are considered as bridges
	 */
	private static final Pattern implicitBridge = Pattern.compile("java\\.lang.*|org\\.junit.*");

	private static final Set<String> basicTypeNames = new HashSet<String>();
	static {
		for (Class<?> clazz : Primitives.allWrapperTypes()) {
			basicTypeNames.add(clazz.getName());
		}
		basicTypeNames.add(String.class.getName());
	}

	// private static Map<Class<?>, String> primitiveArrayId;

	public static boolean isBasicType(Type type) {
		if (type instanceof PrimitiveType) {
			return true;
		}
		String typeName = type.toString();
		if (!typeName.contains(".")) {
			typeName = "java.lang." + typeName;
		}
		return basicTypeNames.contains(typeName);
	}

	public static boolean isBridge(Class<?> clazz) {
		boolean ok = hasAnnotation(clazz, STJSBridge.class.getName());
		if (ok) {
			return ok;
		}
		if (implicitBridge.matcher(clazz.getName()).matches()) {
			return true;
		}
		return false;
	}

	public static boolean isDataType(ClassWrapper clazz) {
		if (clazz == null) {
			return false;
		}
		return isDataType(clazz.getClazz());
	}

	public static boolean isDataType(Class<?> clazz) {
		return hasAnnotation(clazz, DataType.class.getName());
	}

	public static boolean hasAnnotation(ClassWrapper clazz, String annotationName) {
		if (clazz == null) {
			return false;
		}
		return hasAnnotation(clazz.getClazz(), annotationName);
	}

	public static boolean hasAnnotation(Class<?> clazz, String annotationName) {
		if (clazz == null) {
			return false;
		}
		// TODO : cache?
		for (Annotation annote : clazz.getAnnotations()) {
			if (annote.annotationType().getName().equals(annotationName)) {
				return true;
			}
		}

		for (Annotation annote : clazz.getPackage().getAnnotations()) {
			if (annote.annotationType().getName().equals(annotationName)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isAdapter(TypeWrapper clazz) {
		if (clazz == null) {
			return false;
		}
		return clazz.hasAnnotation(Adapter.class);
	}

	public static boolean isAdapter(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}
		return hasAnnotation(clazz, Adapter.class.getName());
	}

	public static boolean isJavascriptFunction(TypeWrapper clazz) {
		if (clazz == null) {
			return false;
		}
		return clazz.hasAnnotation(JavascriptFunction.class);
	}

	/**
	 * 
	 * @param resolvedType
	 * @param arrayCount
	 * @return the ClassWrapper representing an array of the given type with the given number of dimensions
	 */
	public static TypeWrapper arrayOf(TypeWrapper resolvedType, int arrayCount) {
		if (resolvedType.getClass() == ClassWrapper.class) {
			return new ClassWrapper(Array.newInstance((Class<?>) resolvedType.getType(), new int[arrayCount])
					.getClass());
		}
		TypeWrapper returnType = resolvedType;
		for (int i = 0; i < arrayCount; ++i) {
			returnType = new GenericArrayTypeWrapper(new GenericArrayTypeImpl(returnType.getType()));
		}
		return returnType;
	}

	public static Method findDeclaredMethod(Class<?> clazz, String name) {
		for (Method m : clazz.getDeclaredMethods()) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

	public static Constructor<?> findConstructor(Class<?> clazz) {
		if (clazz.getDeclaredConstructors().length != 0) {
			return clazz.getDeclaredConstructors()[0];
		}
		return null;
	}

	public static boolean isAssignableFromType(final Class<?> cls, final java.lang.reflect.Type type) {
		if (type instanceof Class<?>) {
			Class<?> otherClass = (Class<?>) type;
			if (cls.isAssignableFrom(otherClass)) {
				return true;
			}
			// try primitives
			if (Primitives.wrap(cls).isAssignableFrom(Primitives.wrap(otherClass))) {
				return true;
			}

			// go on with primitive rules double/float -> accept long/int -> accept byte/char (but this only if there is
			// none more specific!)
			if (PrimitiveTypes.isAssignableFrom(cls, otherClass)) {
				return true;
			}
			return false;
		}
		if (type instanceof GenericArrayType) {
			return isAssignableFromGenericArrayType(cls, (GenericArrayType) type);
		}
		if (type instanceof ParameterizedType) {
			return isAssignableFromParameterizedType(cls, (ParameterizedType) type);
		}
		if (type instanceof TypeVariable<?>) {
			return isAssignableFromTypeVariable(cls, (TypeVariable<?>) type);
		}
		if (type instanceof WildcardType) {
			return isAssignableFromWildcardType(cls, (WildcardType) type);
		}

		throw new IllegalArgumentException("Unsupported type: " + type);
	}

	private static boolean isAssignableFromGenericArrayType(final Class<?> cls, final GenericArrayType genericArrayType) {
		if (!cls.isArray()) {
			return false;
		}

		final java.lang.reflect.Type componentType = genericArrayType.getGenericComponentType();
		return isAssignableFromType(cls.getComponentType(), componentType);
	}

	private static boolean isAssignableFromParameterizedType(final Class<?> cls,
			final ParameterizedType parameterizedType) {
		return isAssignableFromType(cls, parameterizedType.getRawType());
	}

	private static boolean isAssignableFromTypeVariable(final Class<?> cls, final TypeVariable<?> typeVariable) {
		return isAssignableFromUpperBounds(cls, typeVariable.getBounds());
	}

	private static boolean isAssignableFromWildcardType(final Class<?> cls, final WildcardType wildcardType) {
		return isAssignableFromUpperBounds(cls, wildcardType.getUpperBounds());
	}

	private static boolean isAssignableFromUpperBounds(final Class<?> cls, final java.lang.reflect.Type[] bounds) {
		for (final java.lang.reflect.Type bound : bounds) {
			if (isAssignableFromType(cls, bound)) {
				return true;
			}
		}
		return false;
	}

	public static Class<?> getRawClazz(java.lang.reflect.Type type) {
		if (type instanceof Class<?>) {
			return (Class<?>) type;
		}
		if (type instanceof ParameterizedType) {
			return getRawClazz(((ParameterizedType) type).getRawType());
		}
		if (type instanceof GenericArrayType) {
			return Object[].class;
		}
		// TODO what to do exacly here !?
		return Object.class;
	}

}
