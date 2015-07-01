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
import java.util.List;
import java.util.Map;

import org.stjs.generator.name.DependencyType;

/**
 * this interface represents the link between a java class and corresponding Javascript file (or files). As we need the
 * full dependencies list at compile time, they are set either during the code generation (for generated files) or
 * during the bridge construction
 *
 * @author acraciun
 */
public interface ClassWithJavascript {
	String getJavaClassName();

	Class<?> getJavaClass();

	String getJavascriptClassName();

	String getJavascriptNamespace();

	List<URI> getJavascriptFiles();

	List<ClassWithJavascript> getDirectDependencies();

	Map<ClassWithJavascript, DependencyType> getDirectDependencyMap();
}
