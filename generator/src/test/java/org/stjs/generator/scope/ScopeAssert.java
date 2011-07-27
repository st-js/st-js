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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import junit.framework.Assert;

import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;

public class ScopeAssert {
	private static final int MY_TAB_CONFIG = 4;

	private final NameResolverVisitor resolver;

	private QualifiedName<IdentifierName> qname;

	private int line = -1;
	private int column = -1;

	private ScopeAssert(NameResolverVisitor resolver) {
		this.resolver = resolver;
	}

	public static ScopeAssert assertScope(NameResolverVisitor resolver) {
		return new ScopeAssert(resolver);
	}

	public ScopeAssert line(int line) {
		this.line = line;
		resolve();
		return this;
	}

	/**
	 * The AST parser uses a 8-space tab when calculating the column. translate it to a 4-space tab
	 * 
	 * @param columnInEditor
	 * @param tabs
	 * @return
	 */
	public ScopeAssert column(int columnInEditor, int tabs) {
		column = columnInEditor + (8 - MY_TAB_CONFIG) * tabs;
		resolve();
		return this;
	}

	private void resolve() {
		if (column >= 0 && line >= 0) {
			qname = resolver.getResolvedIdentifiers().get(new SourcePosition(line, column));
		}
	}

	public ScopeAssert assertName(String name) {
		assertNotNull(qname);
		assertEquals(name, qname.getName());
		return this;
	}

	public ScopeAssert assertScopePath(String scopePath) {
		assertNotNull(qname);
		assertEquals(scopePath, qname.getScope().getPath());
		return this;
	}

	public ScopeAssert assertNull() {
		Assert.assertNull(qname);
		return this;
	}
}
