package org.stjs.generator.handlers;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.ObjectCreationExpr;

import org.stjs.generator.GenerationContext;

public class InlineFunctionHandler extends DefaultHandler {
	public InlineFunctionHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	private MethodDeclaration getMethodDeclaration(ObjectCreationExpr n) {
		for (BodyDeclaration d : n.getAnonymousClassBody()) {
			if (d instanceof MethodDeclaration) {
				return (MethodDeclaration) d;
			}
		}
		return null;
	}

	@Override
	public void visit(ObjectCreationExpr n, GenerationContext arg) {
		MethodDeclaration method = getMethodDeclaration(n);
		if (method == null) {
			// TODO error here
			return;
		}
		method.accept(getRuleVisitor(), arg);
		// getRuleVisitor().visit(, Boolean.TRUE);
	}

}
