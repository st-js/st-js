/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
			qname = context.resolveMethod(n);
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
			} else if (qname != null && TypeScope.STATIC_SCOPE.equals(qname.getScopeName())) {
        // TODO : use visitor pattern (or similar) and do not cast
        TypeScope scope = (TypeScope) qname.getScope();
        if (scope.getDeclaredTypeName().isAnonymous()) {
          throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
              "Cannot generate static field access for anonymous class"); // I think that this is not possible in Java (static field in anonymous class)
        }
        getPrinter().print(scope.getDeclaredTypeName().getName().getOrThrow()+".");
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
		QualifiedName<IdentifierName> qname = context.resolveIdentifier(n);
		if (qname != null) {
		  if (TypeScope.THIS_SCOPE.equals(qname.getScopeName())) {
		    getPrinter().print("this.");
		  } else if (TypeScope.STATIC_SCOPE.equals(qname.getScopeName())) {
		    // TODO : use visitor pattern (or similar) and do not cast
		    TypeScope scope = (TypeScope) qname.getScope();
		    if (scope.getDeclaredTypeName().isAnonymous()) {
		      throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
		          "Cannot generate static field access for anonymous class"); // I think that this is not possible in Java (static field in anonymous class)
		    }
		    getPrinter().print(scope.getDeclaredTypeName().getName().getOrThrow()+".");
		 }
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
