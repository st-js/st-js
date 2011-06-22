package org.stjs.generator.handlers;

import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;

public class VariableTypeHandler extends DefaultHandler {

	public VariableTypeHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(ClassOrInterfaceType n, Object arg) {
		getPrinter().print("var");
	}

	@Override
	public void visit(PrimitiveType n, Object arg) {
		getPrinter().print("var");
	}
}
