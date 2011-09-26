package org.stjs.generator.utils;

import java.lang.annotation.Annotation;

import org.stjs.generator.scope.classloader.ClassWrapper;

public class ClassUtils {
	public static boolean isMockType(ClassWrapper clazz) {
		if (clazz == null) {
			return false;
		}
		return isMockType(clazz.getClazz());
	}

	public static boolean isMockType(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}
		// TODO : cache?
		for (Annotation annote : clazz.getAnnotations()) {
			if (annote.annotationType().getName().equals("org.stjs.javascript.MockType")) {
				return true;
			}
		}
		for (Annotation annote : clazz.getPackage().getAnnotations()) {
			if (annote.annotationType().getName().equals("org.stjs.javascript.MockType")) {
				return true;
			}
		}

		return false;
	}
}
