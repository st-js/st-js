package org.stjs.generator.handlers;

import static org.stjs.generator.handlers.utils.Joiner.joiner;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;

public class FieldDeclarationHandler extends DefaultHandler {

	public FieldDeclarationHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(FieldDeclaration n, Object arg) {
		n.getType().accept(getRuleVisitor(), arg);
		joiner(getRuleVisitor()).on(", ").join(n.getVariables(), arg);
	}

	@Override
	public void visit(VariableDeclarator n, Object arg) {
		n.getId().accept(getRuleVisitor(), arg);
		if (n.getInit() != null) {
			getPrinter().print(":  ");
			n.getInit().accept(getRuleVisitor(), arg);
		} else {
			getPrinter().print(": null");
		}
	}

	@Override
	public void visit(ClassOrInterfaceType n, Object arg) {
		//skip
	}

	@Override
	public void visit(PrimitiveType n, Object arg) {
		//skip
	}
}
