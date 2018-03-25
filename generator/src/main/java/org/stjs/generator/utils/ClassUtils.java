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

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

import org.stjs.generator.JavascriptClassGenerationException;
import org.stjs.javascript.annotation.STJSBridge;

/**
 * <p>ClassUtils class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public final class ClassUtils {
	/**
	 * these are packages that don't have the annotation but are considered as bridges
	 */
	private static final Pattern IMPLICIT_BRIDGE = Pattern.compile("java\\.lang.*|org\\.junit.*");

	// private static Map<Class<?>, String> primitiveArrayId;

	private ClassUtils() {
		//
	}

	/**
	 * <p>getClazz.</p>
	 *
	 * @param builtProjectClassLoader a {@link java.lang.ClassLoader} object.
	 * @param className a {@link java.lang.String} object.
	 * @return a {@link java.lang.Class} object.
	 */
	public static Class<?> getClazz(ClassLoader builtProjectClassLoader, String className) {
		try {
			return builtProjectClassLoader.loadClass(className);
		}
		catch (ClassNotFoundException e) {
			throw new JavascriptClassGenerationException(className, "Cannot load class:" + e);
		}
	}

	/**
	 * <p>isBridge.</p>
	 *
	 * @param builtProjectClassLoader a {@link java.lang.ClassLoader} object.
	 * @param clazz a {@link java.lang.Class} object.
	 * @return a boolean.
	 */
	@SuppressWarnings("unchecked")
	public static boolean isBridge(ClassLoader builtProjectClassLoader, Class<?> clazz) {
		boolean ok = hasAnnotation(clazz, (Class<? extends Annotation>) getClazz(builtProjectClassLoader, STJSBridge.class.getName()));
		if (ok) {
			return ok;
		}
		if (IMPLICIT_BRIDGE.matcher(clazz.getName()).matches()) {
			return true;
		}
		return false;
	}

	/**
	 * <p>hasAnnotation.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param annotationClass a {@link java.lang.Class} object.
	 * @return a boolean.
	 */
	public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		return getAnnotation(clazz, annotationClass) != null;
	}

	/**
	 * <p>getAnnotation.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param annotationClass a {@link java.lang.Class} object.
	 * @return a T object.
	 */
	public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
		if (clazz == null) {
			return null;
		}
		// TODO : cache?
		T ann = clazz.getAnnotation(annotationClass);
		if (ann != null) {
			return ann;
		}

		if (clazz.getPackage() == null) {
			return null;
		}

		return clazz.getPackage().getAnnotation(annotationClass);
	}

	/**
	 * <p>getPropertiesFileName.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getPropertiesFileName(String className) {
		return className.replace('.', '/') + ".stjs";
	}

}
