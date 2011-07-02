package org.stjs.generator.handlers;

import japa.parser.ast.body.VariableDeclaratorId;

import org.stjs.generator.GenerationContext;

public class VariableDeclarationHandler extends DefaultHandler {

	public VariableDeclarationHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(VariableDeclaratorId n, GenerationContext arg) {
		getPrinter().print(n.getName());
	}

}
