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

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;

import java.util.concurrent.atomic.AtomicReference;

import org.stjs.generator.handlers.ForEachNodeVisitor;
import org.stjs.generator.scope.simple.CompilationUnitScope;
import org.stjs.generator.scope.simple.VariableWithScope;

public class ScopeAssert {
	private static final int MY_TAB_CONFIG = 4;

	private CompilationUnit compilationUnit;
	private CompilationUnitScope rootScope;

	private VariableWithScope resolvedVariable;
	private Node node;

	private int line = -1;
	private int column = -1;

	private ScopeAssert(CompilationUnitScope rootScope, CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
		this.rootScope = rootScope;
	}

	public static ScopeAssert assertScope(CompilationUnitScope rootScope, CompilationUnit compilationUnit) {
		return new ScopeAssert(rootScope, compilationUnit);
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
			final AtomicReference<Node> nodePointer = new AtomicReference<Node>();
			new ForEachNodeVisitor<Object>() {
				@Override
				protected void before(Node n, Object arg) {
					if (n.getBeginLine() == line && n.getBeginColumn() == column) {
						nodePointer.set(n);
					}
				}
			}.visit(compilationUnit, null);
			node = nodePointer.get();
		}
	}

	public ScopeAssert assertName(String name) {
		// TODO assertNotNull(qname);
		// TODO assertEquals(name, nodeName);
		return this;
	}

	public ScopeAssert assertScopePath(String scopePath) {
		// TODO check path from the root
		//
		// assertNotNull(qname);
		// assertEquals(scopePath, qname.getScope().getPath());
		return this;
	}

	public ScopeAssert assertNull() {
		// TODO Assert.assertNull(qname);
		return this;
	}

	public void assertType(Class<?> clazz) {
		// TODO assertSame(type, qname.getType());
	}
}
