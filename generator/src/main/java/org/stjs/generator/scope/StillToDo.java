package org.stjs.generator.scope;

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

import static org.stjs.generator.ast.ASTNodeData.resolvedVariable;
import japa.parser.ast.Node;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.SynchronizedStmt;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.ast.SourcePosition;
import org.stjs.generator.type.FieldWrapper;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.variable.Variable;
import org.stjs.javascript.annotation.GlobalScope;

/**
 * this class generate different checks made on the Java statements before they are converted to Javascript
 * @author acraciun
 */
public final class StillToDo {
	private StillToDo() {
		//
	}

	public static void checkGlobalVariable(NameExpr n, GenerationContext context, Scope currentScope) {
		Variable var = resolvedVariable(n);
		if (var instanceof FieldWrapper) {
			FieldWrapper field = (FieldWrapper) var;
			TypeWrapper scopeType = field.getOwnerType();
			checkGlobalVariable(n, scopeType, field.getName(), currentScope, context);
		}
	}

	public static void checkGlobalVariable(FieldAccessExpr n, GenerationContext context, Scope currentScope) {
		FieldWrapper field = (FieldWrapper) resolvedVariable(n);
		TypeWrapper scopeType = field.getOwnerType();
		checkGlobalVariable(n, scopeType, field.getName(), currentScope, context);
	}

	private static void checkGlobalVariable(Node n, TypeWrapper scopeType, String name, Scope currentScope, GenerationContext context) {
		if (scopeType.hasAnnotation(GlobalScope.class)) {
			// look if there is any variable with the same name in the method
			MethodScope methodScope = currentScope.closest(MethodScope.class);
			if (methodScope == null) {
				return;
			}
			Variable existent = resolveVariableInDescendantBlocks(methodScope, name);
			if (existent != null) {
				throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
						"A variable with the same name as your global variable is already defined in this method's scope. "
								+ "Please rename either the local variable/parameter or the global variable.");
			}
		}
	}

	/**
	 * look for the variable in all the blocks (without going in other types)
	 * @param methodScope
	 * @param name
	 * @return
	 */
	private static Variable resolveVariableInDescendantBlocks(Scope scope, String name) {
		VariableWithScope existent = scope.resolveVariable(name);
		if (existent != null && existent.getScope() == scope) {
			// take only this scope, not a parent class scope
			return existent.getVariable();
		}
		for (Scope child : scope.getChildren()) {
			if (child instanceof BasicScope) {
				Variable var = resolveVariableInDescendantBlocks(child, name);
				if (var != null) {
					return var;
				}
			}
		}
		return null;
	}

	public void visit(SynchronizedStmt n, GenerationContext context) {
		throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
				"synchronized blocks are not supported by Javascript");
	}

	public void visit(AssertStmt n, GenerationContext context) {
		throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
				"Assert statement is not supported by Javascript");
	}

}
