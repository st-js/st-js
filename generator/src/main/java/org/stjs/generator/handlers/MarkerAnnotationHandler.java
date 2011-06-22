package org.stjs.generator.handlers;

import japa.parser.ast.expr.MarkerAnnotationExpr;

public class MarkerAnnotationHandler extends DefaultHandler {
	public MarkerAnnotationHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(MarkerAnnotationExpr n, Object arg) {
		//skip
	}

}
