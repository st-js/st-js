package org.stjs.generator.handlers;

import static org.stjs.generator.handlers.utils.Joiner.joiner;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;

import org.stjs.generator.GenerationContext;

public class FieldDeclarationHandler extends DefaultHandler {

	public FieldDeclarationHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(FieldDeclaration n, GenerationContext arg) {
		n.getType().accept(getRuleVisitor(), arg);
		joiner(getRuleVisitor()).on(", ").join(n.getVariables(), arg);
	}

	@Override
	public void visit(VariableDeclarator n, GenerationContext arg) {
		n.getId().accept(getRuleVisitor(), arg);
		if (n.getInit() != null) {
			getPrinter().print(":  ");
			n.getInit().accept(getRuleVisitor(), arg);
		} else {
			getPrinter().print(": null");
		}
	}

	@Override
	public void visit(ClassOrInterfaceType n, GenerationContext arg) {
		// skip
	}

	@Override
	public void visit(PrimitiveType n, GenerationContext arg) {
		// skip
	}
}
