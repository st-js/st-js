package org.stjs.generator.handlers;

import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.QualifiedNameExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.QualifiedName;
import org.stjs.generator.scope.TypeScope;

public class NameResolverHandler extends DefaultHandler {
	private SpecialMethodHandlers specialMethodHandlers = new SpecialMethodHandlers();

	public NameResolverHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(MethodCallExpr n, GenerationContext context) {
		QualifiedName<MethodName> qname = null;
		if (n.getScope() == null) {
			// only for methods without a scope
			SourcePosition pos = new SourcePosition(n.getBeginLine(), n.getBeginColumn());
			qname = context.resolveMethod(pos);

		}
		if (!specialMethodHandlers.handle(this, n, qname, context)) {
			if (qname != null && TypeScope.THIS_SCOPE.equals(qname.getScope())) {
				getPrinter().print("this.");
			}
			n.accept(getRuleVisitor(), context.skipHandlers());
		}
	}

	@Override
	public void visit(QualifiedNameExpr n, GenerationContext context) {
		n.getQualifier().accept(getRuleVisitor(), context);
		getPrinter().print(".");
		getPrinter().print(n.getName());
	}

	@Override
	public void visit(NameExpr n, GenerationContext context) {
		SourcePosition pos = new SourcePosition(n.getBeginLine(), n.getBeginColumn());
		QualifiedName<IdentifierName> qname = context.resolveIdentifier(pos);
		if (qname != null && TypeScope.THIS_SCOPE.equals(qname.getScopeName())) {
			getPrinter().print("this.");
		}
		getPrinter().print(n.getName());
	}
}
