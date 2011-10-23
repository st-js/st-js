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
package org.stjs.generator.scope;

import java.util.List;

import org.stjs.generator.type.TypeWrapper;

public interface Scope {

	public interface ScopeVisitor<T> {
		T apply(CompilationUnitScope scope);

		T apply(ClassScope classScope);

		T apply(BasicScope basicScope);
	}

	<T> T apply(ScopeVisitor<T> visitor);

	public TypeWithScope resolveType(String name);

	public VariableWithScope resolveVariable(String string);

	public MethodsWithScope resolveMethod(String name, TypeWrapper... paramTypes);

	Scope addChild(Scope abstractScope);

	List<Scope> getChildren();

	Scope getParent();

	/**
	 * Find the closest parent of type T
	 * 
	 * @param <T>
	 * @param scopeType
	 * @return
	 */
	<T extends Scope> T closest(Class<T> scopeType);
}
