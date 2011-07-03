package org.stjs.generator.handlers;

import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.VariableDeclarationExpr;

import java.util.Iterator;

import org.stjs.generator.GenerationContext;

public class VariableDeclarationHandler extends DefaultHandler {

	public VariableDeclarationHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(VariableDeclaratorId n, GenerationContext arg) {
		getPrinter().print(n.getName());
	}

	@Override
	public void visit(VariableDeclarationExpr n, GenerationContext arg) {
		n.getType().accept(getRuleVisitor(), arg);
		getPrinter().print(" ");

		for (Iterator<VariableDeclarator> i = n.getVars().iterator(); i.hasNext();) {
			VariableDeclarator v = i.next();
			v.accept(getRuleVisitor(), arg);
			if (i.hasNext()) {
				getPrinter().print(", ");
			}
		}
	}
}
