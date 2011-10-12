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
package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.simple.Scope;

public class NameScopeWalker {
	private final Scope scope;
	private int currentChild = 0;

	public NameScopeWalker(Scope scope) {
		this.scope = scope;
	}

	public Scope getScope() {
		return scope;
	}

	public NameScopeWalker nextChild() {
		if (currentChild >= scope.getChildren().size()) {
			throw new IllegalStateException("The scope [" + scope + "] does not have a child #"
					+ currentChild);
		}
		return new NameScopeWalker(scope.getChildren().get(currentChild++));
	}

}
