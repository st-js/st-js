package org.stjs.generator;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.stjs.javascript.annotation.Namespace;

public final class NamespaceUtil {

	private NamespaceUtil() {
		// nobody should extend this class
	}

	public static String resolveNamespace(Class<?> clazz) {
		String ns = resolveNamespaceSimple(clazz);
		if (ns == null && clazz.getPackage() != null) {
			// the class is not annotated, let's see if its package is
			return resolvePackageNamespace(clazz.getPackage().getName(), clazz.getClassLoader());
		}
		return ns;
	}

	public static String resolveNamespace(String className, ClassLoader classLoader) {
		try {
			Class<?> clazz = Class.forName(className, false, classLoader);
			return resolveNamespace(clazz);
		}
		catch (ClassNotFoundException cnfe) {
			throw new STJSRuntimeException(cnfe);
		}
	}

	public static String resolvePackageNamespace(String packageName, ClassLoader classLoader) {
		Package pack = getPackage(packageName, classLoader);
		if (pack != null) {
			String ns = getNamespaceAnnotationValue(pack, classLoader);
			if (ns != null) {
				return ns;
			}
		}

		// no annotation found on this package, let's check the parent
		int lastDotIndex = packageName.lastIndexOf('.');
		if (lastDotIndex < 0) {
			// there is no parent...
			return null;
		}

		return resolvePackageNamespace(packageName.substring(0, lastDotIndex), classLoader);
	}

	public static String resolveNamespaceSimple(String className, ClassLoader classLoader) {
		try {
			Class<?> clazz = Class.forName(className, false, classLoader);
			return resolveNamespaceSimple(clazz);
		}
		catch (ClassNotFoundException cnfe) {
			throw new STJSRuntimeException(cnfe);
		}
	}

	public static String resolveNamespaceSimple(Class<?> clazz) {
		String ns = getNamespaceAnnotationValue(clazz, clazz.getClassLoader());
		if (ns != null) {
			return ns;
		}

		// Annotation not found, maybe we have a namespace in the corresponding.stjs file.
		return getNamespacePropertyValue(clazz);
	}

	private static String getNamespacePropertyValue(Class<?> clazz) {
		if (clazz.getClassLoader() == null) {
			// the class was loaded by the system classloader.
			// There is nothing in the system ClassLoader that could possibly have been annotated with
			// @Namespace
			return null;
		}

		String propertiesPath = clazz.getName().replace(".", "/") + ".stjs";
		InputStream in = clazz.getClassLoader().getResourceAsStream(propertiesPath);
		if (in == null) {
			// the properties file does not exist
			return null;
		}

		try {
			Properties props = new Properties();
			props.load(in);
			return props.getProperty(STJSClass.JS_NAMESPACE);
		}
		catch (IOException e) {
			// Properties exists, but we couldn't read it for some reason.
			throw new STJSRuntimeException(e);
		}
	}

	private static String getNamespaceAnnotationValue(AnnotatedElement element, ClassLoader classLoader) {
		if (classLoader == null) {
			// If the classLoader is null, then the element has been loaded by the system ClassLoader.
			// There is nothing in the system ClassLoader that could possibly have been annotated with
			// @Namespace
			return null;
		}

		try {
			Annotation annotation = element.getAnnotation(getNamespaceClass(classLoader));
			if (annotation == null) {
				return null;
			}
			return (String) annotation.getClass().getMethod("value").invoke(annotation);
		}
		catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			throw new STJSRuntimeException(e);
		}
	}

	/**
	 * Returns an instance of the @Namespace annotation class that is loaded from the specified ClassLoader.
	 * In most cases, the current thread runs within the maven plugin which uses a different ClassLoader than
	 * the one that contains the classpath of the project that is being compile. This method is useful to make
	 * sure that calling Class.getAnnotation or Package.getAnnotation returns the expected result.
	 */
	@SuppressWarnings("unchecked")
	private static Class<? extends Annotation> getNamespaceClass(ClassLoader classLoader) throws ClassNotFoundException {
		return (Class<? extends Annotation>) Class.forName(Namespace.class.getName(), true, classLoader);
	}

	private static Package getPackage(String packageName, ClassLoader classLoader) {
		// try to load the package info class to force the package loading
		Class<?> clazz;
		try {
			clazz = Class.forName(packageName + ".package-info", true, classLoader);
		}
		catch (ClassNotFoundException e) {
			// no class;
			return null;
		}

		return clazz.getPackage();
	}
}
