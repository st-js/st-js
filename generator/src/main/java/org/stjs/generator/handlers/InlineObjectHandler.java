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
package org.stjs.generator.handlers;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.Statement;

import org.stjs.generator.GenerationContext;

public class InlineObjectHandler extends DefaultHandler {
	public InlineObjectHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	private InitializerDeclaration getInitializerDeclaration(ObjectCreationExpr n) {
		for (BodyDeclaration d : n.getAnonymousClassBody()) {
			if (d instanceof InitializerDeclaration) {
				return (InitializerDeclaration) d;
			}
		}
		return null;
	}

	@Override
	public void visit(ObjectCreationExpr n, GenerationContext arg) {
		InitializerDeclaration block = getInitializerDeclaration(n);
		if (block == null) {
			// TODO error here
			return;
		}
		block.accept(getRuleVisitor(), arg);
	}

	@Override
	public void visit(BlockStmt n, GenerationContext arg) {
		printer.printLn("{");
		if (n.getStmts() != null) {
			printer.indent();
			for (int i = 0; i < n.getStmts().size(); ++i) {
				Statement s = n.getStmts().get(i);
				s.accept(getRuleVisitor(), arg);
				if ((i < n.getStmts().size() - 1) && (n.getStmts().size() > 1)) {
					printer.print(",");
				}
				printer.printLn();
			}
			printer.unindent();
		}
		printer.print("}");
	}

	@Override
	public void visit(ExpressionStmt n, GenerationContext arg) {
		n.getExpression().accept(getRuleVisitor(), arg);
	}

	@Override
	public void visit(AssignExpr n, GenerationContext arg) {
		n.getTarget().accept(getRuleVisitor(), arg);
		printer.print(" ");
		switch (n.getOperator()) {
		case assign:
			printer.print(":");
			break;
		default:
			// TODO - what here!?
			break;
		}
		printer.print(" ");
		n.getValue().accept(getRuleVisitor(), arg);
	}
}
