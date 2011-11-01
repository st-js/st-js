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

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.stjs.generator.utils.PreConditions;

/**
 * This class represents a bridge class. As javascript files it has the corresponding source files from the javascript
 * library. As dependencies it can have other bridge classes or even stjs classes.
 * 
 * @author acraciun
 * 
 */
public class BridgeClass implements ClassWithJavascript {
	private final Class<?> clazz;
	private final DependencyResolver dependencyResolver;

	public BridgeClass(DependencyResolver dependencyResolver, Class<?> clazz) {
		PreConditions.checkNotNull(dependencyResolver);
		PreConditions.checkNotNull(clazz);
		this.dependencyResolver = dependencyResolver;
		this.clazz = clazz;
	}

	@Override
	public String getClassName() {
		return clazz.getName();
	}

	@Override
	public List<URI> getJavascriptFiles() {
		// TODO use annotations
		return Collections.emptyList();
	}

	@Override
	public List<ClassWithJavascript> getDirectDependencies() {
		// TODO use annotations
		return Collections.emptyList();
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
		BridgeClass other = (BridgeClass) obj;

		return clazz == other.clazz;
	}

}
