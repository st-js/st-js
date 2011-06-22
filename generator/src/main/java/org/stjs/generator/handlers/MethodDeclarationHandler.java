package org.stjs.generator.handlers;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;

import java.util.Iterator;

public class MethodDeclarationHandler extends DefaultHandler {
	private final boolean anonymous;

	public MethodDeclarationHandler(RuleBasedVisitor ruleVisitor, boolean anonymous) {
		super(ruleVisitor);
		this.anonymous = anonymous;
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		getPrinter().print(" ");
		if (anonymous) {
			getPrinter().print("function");
		} else {
			getPrinter().print(n.getName());
			getPrinter().print(": function");
		}

		getPrinter().print("(");
		if (n.getParameters() != null) {
			for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
				Parameter p = i.next();
				p.accept(getRuleVisitor(), arg);
				if (i.hasNext()) {
					getPrinter().print(", ");
				}
			}
		}
		getPrinter().print(")");
		//skip  throws
		if (n.getBody() == null) {
			getPrinter().print(";");
		} else {
			getPrinter().print(" ");
			n.getBody().accept(getRuleVisitor(), arg);
		}
	}

	@Override
	public void visit(Parameter n, Object arg) {
		n.getType().accept(getRuleVisitor(), arg);
		n.getId().accept(getRuleVisitor(), arg);
	}
}
