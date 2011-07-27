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
package org.stjs.generator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Use this class to build a configuration needed by the {@link Generator}
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class GeneratorConfigurationBuilder {
	private Collection<String> allowedPackages = new HashSet<String>();
	private Set<String> allowedJavaLangClasses = new HashSet<String>();

	public GeneratorConfigurationBuilder allowedPackage(String packageName) {
		allowedPackages.add(packageName);
		return this;
	}

	public GeneratorConfigurationBuilder allowedJavaLangClasses(String className) {
		allowedJavaLangClasses.add(className);
		return this;
	}

	public GeneratorConfigurationBuilder allowedPackages(Collection<String> packageNames) {
		allowedPackages.addAll(packageNames);
		return this;
	}

	public GeneratorConfigurationBuilder allowedJavaLangClasses(Collection<String> classNames) {
		allowedJavaLangClasses.addAll(classNames);
		return this;
	}

	public GeneratorConfiguration build() {
		allowedJavaLangClasses.add("Object");
		allowedJavaLangClasses.add("Runnable");
		allowedJavaLangClasses.add("String");
		allowedJavaLangClasses.add("Number");
		allowedJavaLangClasses.add("Double");
		allowedJavaLangClasses.add("Float");
		allowedJavaLangClasses.add("Long");
		allowedJavaLangClasses.add("Integer");
		allowedJavaLangClasses.add("Short");
		allowedJavaLangClasses.add("Boolean");
		allowedJavaLangClasses.add("Character");
		allowedJavaLangClasses.add("Byte");

		allowedPackages.add("java.lang");
		return new GeneratorConfiguration(allowedPackages, allowedJavaLangClasses);
	}

}
