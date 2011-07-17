package org.stjs.generator;

import java.util.Collection;

public class GeneratorConfiguration {
	private final Collection<String> allowedPackages;

	GeneratorConfiguration(Collection<String> allowedPackages) {
		this.allowedPackages = allowedPackages;
	}

	/**
	 * 
	 * @return the parent packages that contain the classes that can be called from the processed source file. Note that
	 *         sub-packages of a package from this collection are also allowed. java.lang is implicit
	 */
	public Collection<String> getAllowedPackages() {
		return allowedPackages;
	}

}
