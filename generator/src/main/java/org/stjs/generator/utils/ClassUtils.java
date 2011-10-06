package org.stjs.generator.utils;

import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.Type;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.javascript.annotation.DataType;
import org.stjs.javascript.annotation.MockType;

import com.google.common.primitives.Primitives;

public class ClassUtils {
	private static Set<String> basicTypeNames = new HashSet<String>();
	static {
		for (Class<?> clazz : Primitives.allWrapperTypes()) {
			basicTypeNames.add(clazz.getName());
		}
		basicTypeNames.add(String.class.getName());
	}

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

	public static boolean isMockType(ClassWrapper clazz) {
		if (clazz == null) {
			return false;
		}
		return isMockType(clazz.getClazz());
	}

	public static boolean isMockType(Class<?> clazz) {
		return hasAnnotation(clazz, MockType.class.getName());
	}

	public static boolean isDataType(ClassWrapper clazz) {
		if (clazz == null) {
			return false;
		}
		return isMockType(clazz.getClazz());
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

}
