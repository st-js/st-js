package org.stjs.generator.handlers;

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.stmt.BlockStmt;

import java.util.Iterator;
import java.util.List;

import org.stjs.generator.GenerationContext;

public class MethodDeclarationHandler extends DefaultHandler {
	private final boolean anonymous;

	public MethodDeclarationHandler(RuleBasedVisitor ruleVisitor, boolean anonymous) {
		super(ruleVisitor);
		this.anonymous = anonymous;
	}

	private void printMethod(String name, List<Parameter> parameters, BlockStmt body, GenerationContext arg) {
		getPrinter().print(" ");
		if (anonymous) {
			getPrinter().print("function");
		} else {
			getPrinter().print(name);
			getPrinter().print(": function");
		}

		getPrinter().print("(");
		if (parameters != null) {
			boolean first = true;
			for (Iterator<Parameter> i = parameters.iterator(); i.hasNext();) {
				Parameter p = i.next();
				// don't display the special THIS parameter
				if (GeneratorConstants.SPECIAL_THIS.equals(p.getId().getName())) {
					continue;
				}
				if (!first) {
					getPrinter().print(", ");
				}
				p.accept(getRuleVisitor(), arg);
				first = false;
			}
		}
		getPrinter().print(")");
		// skip throws
		if (body == null) {
			getPrinter().print(";");
		} else {
			getPrinter().print(" ");
			body.accept(getRuleVisitor(), arg);
		}
	}

	@Override
	public void visit(MethodDeclaration n, GenerationContext arg) {
		printMethod(n.getName(), n.getParameters(), n.getBody(), arg);
	}

	@Override
	public void visit(ConstructorDeclaration n, GenerationContext arg) {
		printMethod(n.getName(), n.getParameters(), n.getBlock(), arg);
	}

	@Override
	public void visit(Parameter n, GenerationContext arg) {
		n.getType().accept(getRuleVisitor(), arg);
		n.getId().accept(getRuleVisitor(), arg);
	}
}
