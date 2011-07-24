package org.stjs.generator.handlers;

import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;

import java.util.Iterator;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptGenerationException;
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
			SourcePosition pos = new SourcePosition(n);
			qname = context.resolveMethod(pos);
		}

		if (!specialMethodHandlers.handle(this, n, qname, context)) {
			if (qname != null && TypeScope.THIS_SCOPE.equals(qname.getScope())) {
				getPrinter().print("this.");
			} else if (n.getScope() instanceof SuperExpr) {
				getPrinter().print("this._super(\"" + n.getName() + "\"");
				if (n.getArgs() != null && n.getArgs().size() > 0) {
					getPrinter().print(", ");
				}
				printArguments(n.getArgs(), context);
				getPrinter().print(")");
				return;
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
		if (GeneratorConstants.SPECIAL_THIS.equals(n.getName())) {
			getPrinter().print("this");
			return;
		}
		SourcePosition pos = new SourcePosition(n);
		QualifiedName<IdentifierName> qname = context.resolveIdentifier(pos);
		if (qname != null && TypeScope.THIS_SCOPE.equals(qname.getScopeName())) {
			getPrinter().print("this.");
		}
		getPrinter().print(n.getName());
	}

	private void printArguments(List<Expression> args, GenerationContext context) {
		if (args != null) {
			for (Iterator<Expression> i = args.iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(getRuleVisitor(), context);
				if (i.hasNext()) {
					getPrinter().print(", ");
				}
			}
		}
	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, GenerationContext context) {
		if (n.isThis()) {
			// This should not happen as another constructor is forbidden
			throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
					"Only one constructor is allowed");
		}

		getPrinter().print("this._super(null");
		if (n.getArgs() != null && n.getArgs().size() > 0) {
			getPrinter().print(", ");
		}
		printArguments(n.getArgs(), context);
		getPrinter().print(");");
	}

	// @Override
	// public void visit(SuperExpr n, T arg) {
	// if (n.getClassExpr() != null) {
	// n.getClassExpr().accept(this, arg);
	// printer.print(".");
	// }
	// printer.print("super");
	// }
}
