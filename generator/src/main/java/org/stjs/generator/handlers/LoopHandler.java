package org.stjs.generator.handlers;

import japa.parser.ast.stmt.ForeachStmt;

import org.stjs.generator.GenerationContext;

public class LoopHandler extends DefaultHandler {
	public LoopHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(ForeachStmt n, GenerationContext arg) {
		getPrinter().print("for (");
		n.getVariable().accept(getRuleVisitor(), arg);
		getPrinter().print(" in ");
		n.getIterable().accept(getRuleVisitor(), arg);
		getPrinter().print(") ");
		n.getBody().accept(getRuleVisitor(), arg);
	}

}
