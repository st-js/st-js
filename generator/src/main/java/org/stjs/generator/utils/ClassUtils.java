package org.stjs.generator.utils;

import java.lang.annotation.Annotation;

import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.javascript.annotation.DataType;
import org.stjs.javascript.annotation.MockType;

public class ClassUtils {
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
