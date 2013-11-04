package org.stjs.generator.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Locale;

import org.stjs.generator.STJSRuntimeException;
import org.stjs.generator.type.ClassWrapper;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.TypeWrappers;

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

	private static <T extends Annotation> T getAnnotationInHelperClass(ClassLoader classLoader, String helperClassName,
			MethodWrapper origMethod, Class<T> annotationClass) {
		Class<?> annotatedHelperClass = findClass(classLoader, helperClassName);

		if (annotatedHelperClass == null) {
			return null;
		}

		// find a method with the same signature in the new class
		Option<MethodWrapper> methodInAnnotatedClass;
		try {
			ClassWrapper cw = TypeWrappers.wrap(annotatedHelperClass);
			methodInAnnotatedClass = cw.findMethod(origMethod.getName(), origMethod.getParameterTypes());
		}
		catch (SecurityException e) {
			throw new STJSRuntimeException(e);
		}

		return methodInAnnotatedClass.isDefined() ? methodInAnnotatedClass.getOrNull().getAnnotationDirectly(annotationClass) : null;

	}

	// public static <T extends Annotation> T getAnnotation(Type ownerType, Method method, Class<T> annotationClass) {
	// }

	public static <T extends Annotation> T getAnnotation(ClassLoader builtProjectClassLoader, Type ownerType, MethodWrapper method,
			Class<T> annotationClass) {
		T annotation = method.getAnnotationDirectly(annotationClass);
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
		annotation = getAnnotationInHelperClass(builtProjectClassLoader,
				ANNOTATED_PACKAGE + ownerClass.getName() + capitalize(method.getName()), method, annotationClass);
		if (annotation != null) {
			return annotation;
		}

		// 2. look for a class in the same package of the declaring class but with the suffix "Annotated"
		annotation = getAnnotationInHelperClass(builtProjectClassLoader, ANNOTATED_PACKAGE + ownerClass.getName(), method, annotationClass);
		return annotation;
	}
}
