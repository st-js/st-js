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

/**
 * this interface is used to lazily resolve a class with its corresponding javascript, when the list of dependencies is
 * browsed.
 *
 * @author acraciun
 * @version $Id: $Id
 */
public interface ClassResolver {
	/**
	 * <p>resolve.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @return a {@link org.stjs.generator.ClassWithJavascript} object.
	 */
	ClassWithJavascript resolve(String className);
	/**
	 * <p>resolveJavaClass.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @return a {@link java.lang.Class} object.
	 */
	Class<?> resolveJavaClass(String className);
}
