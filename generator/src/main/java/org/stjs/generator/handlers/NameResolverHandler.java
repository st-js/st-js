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

import japa.parser.ast.Node;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import java.util.Iterator;
import java.util.List;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.JavaTypeName;
import org.stjs.generator.scope.NameScope;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.ParentTypeScope;
import org.stjs.generator.scope.QualifiedName;
import org.stjs.generator.scope.TypeScope;

public class NameResolverHandler extends DefaultHandler {
	private SpecialMethodHandlers specialMethodHandlers = new SpecialMethodHandlers();

	public NameResolverHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(final MethodCallExpr n, final GenerationContext context) {
		QualifiedName<MethodName> qname = context.resolveMethod(n);
		if (!specialMethodHandlers.handle(this, n, qname, context)) {
		  if (qname != null) {
  		  if (qname.isStatic()) {
  		    printStaticFieldOrMethodAccessPrefix(n, context, qname);
  	      printer.print(n.getName());
  	      printArguments(n.getArgs(), context, true);
  	      return;
  		  } else {
  		    if (qname.getScope() != null) {
    		    qname.getScope().visit(new NameScope.EmptyVoidNameScopeVisitor(false) {
      		    @Override
      		    public void caseTypeScope(TypeScope typeScope) {
      		      // Non static reference to current enclosing type.
      		      printer.print("this.");
      		    }
      		    @Override
      		    public void caseParentTypeScope(ParentTypeScope parentTypeScope) {
      		      // Non static reference to parent type
      		      printer.print("this._super(\"" + n.getName() + "\"");
                if (n.getArgs() != null && n.getArgs().size() > 0) {
                  printer.print(", ");
                }
                printArguments(n.getArgs(), context, false);
                printer.print(")");
      		    }
      		  });
    		    }
  		  }
		  }
		  n.accept(getRuleVisitor(), context.skipHandlers());
		}
	}

  private void printStaticFieldOrMethodAccessPrefix(final Node n,
      final GenerationContext context, final QualifiedName<?> qname) {
    if (!qname.isGlobal()) {
      JavaTypeName definitionPoint = qname.getDefinitionPoint().getOrThrow();
      if (definitionPoint.isAnonymous()) {
        throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
            "Cannot generate static field access for anonymous class"); // I think that this is not possible in Java (static field in anonymous class)
      }
      printer.print(definitionPoint.getFullName(false).getOrThrow()+".");
    }
  }

  @Override
  public void visit(ClassOrInterfaceType n, GenerationContext context) {
    if (n.getScope() != null) {
      n.getScope().accept(this, context);
      printer.print(".");
    }
    QualifiedName<IdentifierName> qname = context.resolveIdentifier(n);
    if (qname != null && qname.getScope() != null && qname.isStatic()) {
      printStaticFieldOrMethodAccessPrefix(n, context, qname);
    } 
    printer.print(n.getName());
  }
  
	@Override
	public void visit(QualifiedNameExpr n, GenerationContext context) {
		n.getQualifier().accept(getRuleVisitor(), context);
		printer.print(".");
		printer.print(n.getName());
	}

	@Override
	public void visit(NameExpr n, GenerationContext context) {
		if (GeneratorConstants.SPECIAL_THIS.equals(n.getName())) {
			printer.print("this");
			return;
		}
		QualifiedName<IdentifierName> qname = context.resolveIdentifier(n);
		if (qname != null) {
		  if (qname.isStatic()) {
		    printStaticFieldOrMethodAccessPrefix(n, context, qname);
		  } else {
  		  qname.getScope().visit(new NameScope.EmptyVoidNameScopeVisitor(false) {
  		    @Override
  		    public void caseTypeScope(TypeScope typeScope) {
  	        printer.print("this.");
  		    }
  		    @Override
  		    public void caseParentTypeScope(ParentTypeScope parentTypeScope) {
  		      // TODO : do we need to use a pointer to the super class the same it is done for mehtods Alex?
  		      // Test it
  		    }
  		  });
		  }
		} 
		
		printer.print(n.getName());
	}

	private void printArguments(List<Expression> args, GenerationContext context, boolean withParents) {
		if (withParents) {
		  printer.print("(");
		}
	  if (args != null) {
			for (Iterator<Expression> i = args.iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(getRuleVisitor(), context);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
	  if (withParents) {
      printer.print(")");
    }
	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, GenerationContext context) {
		if (n.isThis()) {
			// This should not happen as another constructor is forbidden
			throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
					"Only one constructor is allowed");
		}

		printer.print("this._super(null");
		if (n.getArgs() != null && n.getArgs().size() > 0) {
			printer.print(", ");
		}
		printArguments(n.getArgs(), context, false);
		printer.print(");");
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
