package org.stjs.generator.handlers;

import japa.parser.ast.body.VariableDeclaratorId;

public class VariableDeclarationHandler extends DefaultHandler {

	public VariableDeclarationHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(VariableDeclaratorId n, Object arg) {
		getPrinter().print(n.getName());
	}

}
