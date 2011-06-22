package org.stjs.generator.handlers;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.Statement;

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
	public void visit(ObjectCreationExpr n, Object arg) {
		InitializerDeclaration block = getInitializerDeclaration(n);
		if (block == null) {
			//TODO error here
			return;
		}
		block.accept(getRuleVisitor(), arg);
		//getRuleVisitor().visit(, Boolean.TRUE);
	}

	@Override
	public void visit(BlockStmt n, Object arg) {
		getPrinter().printLn("{");
		if (n.getStmts() != null) {
			getPrinter().indent();
			for (int i = 0; i < n.getStmts().size(); ++i) {
				Statement s = n.getStmts().get(i);
				s.accept(getRuleVisitor(), arg);
				if ((i < n.getStmts().size() - 1) && (n.getStmts().size() > 1)) {
					getPrinter().print(",");
				}
				getPrinter().printLn();
			}
			getPrinter().unindent();
		}
		getPrinter().print("}");
	}

	@Override
	public void visit(ExpressionStmt n, Object arg) {
		n.getExpression().accept(getRuleVisitor(), arg);
	}

	@Override
	public void visit(AssignExpr n, Object arg) {
		n.getTarget().accept(getRuleVisitor(), arg);
		getPrinter().print(" ");
		switch (n.getOperator()) {
			case assign:
				getPrinter().print(":");
				break;
			default:
				//TODO - what here!?
				break;
		}
		getPrinter().print(" ");
		n.getValue().accept(getRuleVisitor(), arg);
	}
}
