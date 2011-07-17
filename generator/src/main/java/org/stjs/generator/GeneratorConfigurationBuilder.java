package org.stjs.generator;

import java.util.Collection;
import java.util.HashSet;

/**
 * Use this class to build a configuration needed by the {@link Generator}
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class GeneratorConfigurationBuilder {
	private Collection<String> allowedPackages = new HashSet<String>();

	public GeneratorConfigurationBuilder allowedPackage(String packageName) {
		allowedPackages.add(packageName);
		return this;
	}

	public GeneratorConfigurationBuilder allowedPackages(Collection<String> packageNames) {
		allowedPackages.addAll(packageNames);
		return this;
	}

	public GeneratorConfiguration build() {
		allowedPackages.add("java.lang");
		return new GeneratorConfiguration(allowedPackages);
	}

}
