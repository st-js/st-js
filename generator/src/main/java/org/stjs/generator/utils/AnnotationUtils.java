package org.stjs.generator.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

import org.stjs.generator.STJSRuntimeException;

public final class AnnotationUtils {
	private static final String ANNOTATED_PACKAGE = "annotation.";

	private AnnotationUtils() {
		//
	}

	private static Class<?> findClass(ClassLoader classLoader, String name) {
		try {
			return classLoader.loadClass(name);
		}
		catch (ClassNotFoundException e) {
			return null;
		}
	}

	private static String capitalize(String s) {
		if (s == null) {
			return null;
		}
		if (s.length() == 1) {
			return s.toUpperCase(Locale.getDefault());
		}
		return s.substring(0, 1).toUpperCase(Locale.getDefault()) + s.substring(1);
	}

	private static <T extends Annotation> T getAnnotationInHelperClass(String helperClassName, Method origMethod,
			Class<T> annotationClass) {
		Class<?> declaringClass = origMethod.getDeclaringClass();
		ClassLoader classLoader = declaringClass.getClassLoader();
		if (classLoader == null) {
			// XXX not sure what class loader should use here
			classLoader = Thread.currentThread().getContextClassLoader();
		}
		Class<?> annotatedHelperClass = findClass(classLoader, helperClassName);

		if (annotatedHelperClass == null) {
			return null;
		}

		// find a method with the same signature in the new class
		Method methodInAnnotatedClass;
		try {
			methodInAnnotatedClass =
					annotatedHelperClass.getDeclaredMethod(origMethod.getName(), origMethod.getParameterTypes());
		}
		catch (SecurityException e) {
			throw new STJSRuntimeException(e);
		}
		catch (NoSuchMethodException e) {
			return null;
		}
		return methodInAnnotatedClass.getAnnotation(annotationClass);

	}

	public static <T extends Annotation> T getAnnotation(Type ownerType, Method method, Class<T> annotationClass) {
		T annotation = method.getAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}
		if (!(ownerType instanceof Class)) {
			return null;
		}
		Class<?> ownerClass = (Class<?>) ownerType;
		// give it a second chance (for classes in another jars or in the JDK, by using ...)

		// 1. look for a class in the same package of the declaring class but with the name of the method (with the
		// 1st letter capitalized) attached
		// and the suffix "Annotated"
		annotation =
				getAnnotationInHelperClass(ANNOTATED_PACKAGE + ownerClass.getName() + capitalize(method.getName()),
						method, annotationClass);
		if (annotation != null) {
			return annotation;
		}

		// 2. look for a class in the same package of the declaring class but with the suffix "Annotated"
		annotation = getAnnotationInHelperClass(ANNOTATED_PACKAGE + ownerClass.getName(), method, annotationClass);
		return annotation;
	}
}
