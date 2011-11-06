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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.stjs.generator.utils.ClassUtils;
import org.stjs.generator.utils.Option;

import com.google.common.collect.ImmutableList;

public class ClassLoaderWrapper {

	private static final String ANONYMOUS_CLASS_NAME = "(\\$\\d+)+$";
	private final ClassLoader classLoader;
	private final Set<String> resolvedClasses = new LinkedHashSet<String>();
	private final Collection<String> allowedPackages;
	private final Collection<String> allowedJavaLangClasses;

	public ClassLoaderWrapper(ClassLoader classLoader, Collection<String> allowedPackages,
			Collection<String> allowedJavaLangClasses) {
		this.classLoader = classLoader;
		this.allowedJavaLangClasses = allowedJavaLangClasses;
		this.allowedPackages = allowedPackages;
	}

	public boolean hasClass(String name) {
		return loadClass(name).isDefined();
	}

	private Class<?> getTopDeclaringClass(Class<?> clazz) throws ClassNotFoundException {
		if (clazz.getSimpleName().isEmpty()) {// anonymous class
			String cleanedClassName = clazz.getName().replaceAll(ANONYMOUS_CLASS_NAME, "");
			return classLoader.loadClass(cleanedClassName);
		}
		if (clazz.getDeclaringClass() == null) {
			return clazz;
		}
		return getTopDeclaringClass(clazz.getDeclaringClass());
	}

	public Option<ClassWrapper> loadClass(String name) {
		try {
			Class<?> clazz = classLoader.loadClass(name);
			Class<?> topDeclaringClass = getTopDeclaringClass(clazz);
			checkAndAddResolvedClass(topDeclaringClass);
			return Option.<ClassWrapper> some(new ClassWrapper(clazz));
		} catch (ClassNotFoundException e) {
			return Option.none();
		}
	}

	private IllegalArgumentException typeNotAllowedException(Class<?> clazz) {
		return new IllegalArgumentException(
				"The usage of the class "
						+ clazz.getName()
						+ " is not allowed. If it's one of your own bridge types, please add the annotation @STJSBridge to the class or to its package.");
	}

	private void checkAndAddResolvedClass(Class<?> clazz) {
		if (resolvedClasses.contains(clazz.getName())) {
			return;
		}
		if (clazz.isAnnotation()) {
			return;
		}
		if (clazz.getName().startsWith("java.lang.")) {
			if (!allowedJavaLangClasses.contains(clazz.getSimpleName())) {
				throw typeNotAllowedException(clazz);
			}
			// no need to store java lang classes
			return;
		}
		if (!ClassUtils.isBridge(clazz)) {
			boolean found = false;
			for (String packageName : allowedPackages) {
				if (clazz.getName().startsWith(packageName)) {
					found = true;
					break;
				}
			}

			if (!found) {
				throw typeNotAllowedException(clazz);
			}
		}

		resolvedClasses.add(clazz.getName());
	}

	public Option<ClassWrapper> loadClassOrInnerClass(String classLoaderCompatibleName) {
		while (true) {
			Option<ClassWrapper> clazz = loadClass(classLoaderCompatibleName);
			if (clazz.isDefined()) {
				return clazz;
			}
			int lastIndexDot = classLoaderCompatibleName.lastIndexOf('.');
			if (lastIndexDot < 0) {
				return Option.none();
			}
			classLoaderCompatibleName = replaceCharAt(classLoaderCompatibleName, lastIndexDot, '$');
		}
	}

	private String replaceCharAt(String s, int pos, char c) {
		StringBuffer buf = new StringBuffer(s);
		buf.setCharAt(pos, c);
		return buf.toString();
	}

	public List<String> getResolvedClasses() {
		return ImmutableList.copyOf(resolvedClasses);
	}

}