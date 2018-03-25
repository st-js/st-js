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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.concurrent.Immutable;

import org.stjs.generator.name.DependencyType;
import org.stjs.generator.utils.ClassUtils;
import org.stjs.generator.utils.PreConditions;
import org.stjs.javascript.annotation.STJSBridge;

/**
 * This class represents a bridge class. As javascript files it has the corresponding source files from the javascript
 * library. As dependencies it can have other bridge classes or even stjs classes.
 *
 * @author acraciun
 * @version $Id: $Id
 */
@Immutable
public class BridgeClass implements ClassWithJavascript {
	private final Class<?> clazz;
	private final String jsNamespace;

	/**
	 * <p>Constructor for BridgeClass.</p>
	 *
	 * @param classResolver a {@link org.stjs.generator.ClassResolver} object.
	 * @param clazz a {@link java.lang.Class} object.
	 */
	public BridgeClass(ClassResolver classResolver, Class<?> clazz) {
		PreConditions.checkNotNull(classResolver);
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

	/** {@inheritDoc} */
	@Override
	public String getJavaClassName() {
		return clazz.getName();
	}

	/** {@inheritDoc} */
	@Override
	public Class<?> getJavaClass() {
		return this.clazz;
	}

	/** {@inheritDoc} */
	@Override
	public String getJavascriptNamespace() {
		return this.jsNamespace;
	}

	private boolean hasSourceAnnotation(STJSBridge bridgeAnnotation) {
		return bridgeAnnotation != null && bridgeAnnotation.sources() != null && bridgeAnnotation.sources().length > 0;
	}

	/** {@inheritDoc} */
	@Override
	public List<URI> getJavascriptFiles() {
		STJSBridge bridgeAnnotation = ClassUtils.getAnnotation(clazz, STJSBridge.class);
		if (!hasSourceAnnotation(bridgeAnnotation)) {
			return Collections.emptyList();
		}

		List<URI> files = new ArrayList<URI>();
		for (String src : bridgeAnnotation.sources()) {
			try {
				if (src.length() > 0) {
					files.add(new URI(src));
				}
			}
			catch (URISyntaxException e) {
				throw new JavascriptClassGenerationException(getJavaClassName(), e);
			}
		}
		return files;
	}

	/** {@inheritDoc} */
	@Override
	public List<ClassWithJavascript> getDirectDependencies() {
		// TODO use annotations
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public Map<ClassWithJavascript, DependencyType> getDirectDependencyMap() {
		// TODO use annotations
		return Collections.emptyMap();
	}

	/** {@inheritDoc} */
	@Override
	public String getJavascriptClassName() {
		String simpleName = getJavaClass().getSimpleName();
		String ns = getJavascriptNamespace();
		if (ns != null && !ns.isEmpty()) {
			return ns + "." + simpleName;
		}
		return simpleName;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + clazz.getName().hashCode();
		return result;
	}

	// public Class<?> getClazz() {
	// return clazz;
	// }

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "BridgeClass:" + clazz.getName();
	}

}
