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
