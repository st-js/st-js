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

import org.stjs.generator.utils.Option;

public class ClassLoaderWrapper {

	private final ClassLoader classLoader;

	public ClassLoaderWrapper(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public boolean hasClass(String name) {
		return loadClass(name).isDefined();
	}

	public Option<ClassWrapper> loadClass(String name) {
		try {
			return Option.<ClassWrapper> some(new ClassWrapper(classLoader.loadClass(name)));
		} catch (ClassNotFoundException e) {
			return Option.none();
		}
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
}