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
import static org.junit.Assert.assertSame;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;

import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;

import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.QualifiedName.NameTypes;

public class ScopeAssert {
	private static final int MY_TAB_CONFIG = 4;

	private CompilationUnit compilationUnit;

	private QualifiedName<IdentifierName> qname;
	private String nodeName;

	private int line = -1;
	private int column = -1;

	private ScopeAssert(CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}

	public static ScopeAssert assertScope(CompilationUnit compilationUnit) {
		return new ScopeAssert(compilationUnit);
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
			final AtomicReference<QualifiedName<IdentifierName>> qNamePointer = new AtomicReference<QualifiedName<IdentifierName>>();
			final AtomicReference<String> nodeNameP = new AtomicReference<String>();
			new VoidVisitorAdapter<Object>() {
				@SuppressWarnings("unchecked")
				private void matchOnName(final AtomicReference<QualifiedName<IdentifierName>> qNamePointer,
						final AtomicReference<String> nodeNameP, Expression n, String name) {
					if (n.getBeginLine() == line && n.getBeginColumn() == column) {
						qNamePointer.set((QualifiedName<IdentifierName>) n.getData());
						nodeNameP.set(name);
					}
				}

				@Override
				public void visit(NameExpr n, Object arg) {
					matchOnName(qNamePointer, nodeNameP, n, n.getName());
				}

				@Override
				public void visit(FieldAccessExpr n, Object arg) {
					matchOnName(qNamePointer, nodeNameP, n, n.getField());
				}

				@Override
				public void visit(MethodCallExpr n, Object arg) {
					matchOnName(qNamePointer, nodeNameP, n, n.getName());
				}
			}.visit(compilationUnit, null);
			qname = qNamePointer.get();
			nodeName = nodeNameP.get();
		}
	}

	public ScopeAssert assertName(String name) {
		assertNotNull(qname);
		assertEquals(name, nodeName);
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

	public void assertType(NameTypes type) {
		assertSame(type, qname.getType());
	}
}
