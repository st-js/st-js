package org.stjs.generator;

import java.util.Collection;
import java.util.Set;

public class GeneratorConfiguration {
	private final Collection<String> allowedPackages;
	private final Set<String> allowedJavaLangClasses;

	GeneratorConfiguration(Collection<String> allowedPackages, Set<String> allowedJavaLangClasses) {
		this.allowedPackages = allowedPackages;
		this.allowedJavaLangClasses = allowedJavaLangClasses;
	}

	/**
	 * 
	 * @return the parent packages that contain the classes that can be called from the processed source file. Note that
	 *         sub-packages of a package from this collection are also allowed. java.lang is implicit
	 */
	public Collection<String> getAllowedPackages() {
		return allowedPackages;
	}

	public Set<String> getAllowedJavaLangClasses() {
		return allowedJavaLangClasses;
	}

}
