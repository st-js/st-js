package org.stjs.generator.handlers;

import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;

import org.stjs.generator.GenerationContext;

public class VariableTypeHandler extends DefaultHandler {

	public VariableTypeHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(ClassOrInterfaceType n, GenerationContext arg) {
		getPrinter().print("var");
	}

	@Override
	public void visit(PrimitiveType n, GenerationContext arg) {
		getPrinter().print("var");
	}
}
