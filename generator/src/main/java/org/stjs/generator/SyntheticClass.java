package org.stjs.generator;

import org.stjs.generator.name.DependencyType;
import org.stjs.generator.utils.PreConditions;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SyntheticClass implements ClassWithJavascript {
	private final Class<?> clazz;
	private final String jsNamespace;

	public SyntheticClass(DependencyResolver dependencyResolver, Class<?> clazz) {
		PreConditions.checkNotNull(dependencyResolver);
		PreConditions.checkNotNull(clazz);
		this.clazz = clazz;
		String ns = NamespaceUtil.resolveNamespace(clazz);
		if (ns == null) {
			// If we can't find a namespace in the bridge stuff, then we have to assume
			// there is no namespace
			ns = "";
		}
		this.jsNamespace = ns;
	}

	@Override
	public String getClassName() {
		return clazz.getName();
	}

	@Override
	public String getJavascriptNamespace() {
		return this.jsNamespace;
	}

	@Override
	public List<URI> getJavascriptFiles() {
		return Collections.emptyList();
	}

	@Override
	public List<ClassWithJavascript> getDirectDependencies() {
		// TODO use annotations
		return Collections.emptyList();
	}

	@Override
	public Map<ClassWithJavascript, DependencyType> getDirectDependencyMap() {
		// TODO use annotations
		return Collections.emptyMap();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + clazz.getName().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SyntheticClass other = (SyntheticClass) obj;

		return clazz == other.clazz;
	}

	@Override
	public String toString() {
		return "SyntheticClass:" + clazz.getName();
	}
}
