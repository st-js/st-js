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
package org.stjs.generator.scope;

import static org.stjs.generator.scope.QualifiedName.NameTypes.INNER_CLASS;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.visitor.GenericVisitorAdapter;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.util.Collection;
import java.util.Set;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;

/**
 * This visitor goes through the AST and resolves all the found identifiers using the {@link NameScopeWalker} previously
 * built from the same source.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class NameResolverVisitor extends VoidVisitorAdapter<NameScopeWalker> {

	private final NameScope rootScope;
	private final Collection<String> allowedPackages;

	private final Set<String> allowedJavaLangClasses;

	public NameResolverVisitor(NameScope rootScope, Collection<String> allowedPackages,
			Set<String> allowedJavaLangClasses) {
		this.rootScope = rootScope;
		this.allowedPackages = allowedPackages;
		this.allowedJavaLangClasses = allowedJavaLangClasses;
	}

	public NameScope getRootScope() {
		return rootScope;
	}


	@Override
	public void visit(CompilationUnit n, NameScopeWalker currentScope) {
		super.visit(n, currentScope.nextChild());
	}

	@Override
	public void visit(BlockStmt n, NameScopeWalker currentScope) {
		super.visit(n, currentScope.nextChild());
	}

	@Override
	public void visit(CatchClause n, NameScopeWalker currentScope) {
		super.visit(n, currentScope.nextChild());
	}

	@Override
	public void visit(MethodDeclaration n, NameScopeWalker currentScope) {
		super.visit(n, currentScope.nextChild());
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, NameScopeWalker currentScope) {
		NameScopeWalker classScope = currentScope;
		if (n.getExtends() != null && n.getExtends().size() > 0 && !n.isInterface()) {
			classScope = currentScope.nextChild();
		}

		if (n.getMembers() != null) {
			classScope = classScope.nextChild();
		}
		super.visit(n, classScope);
	}

	@Override
	public void visit(ObjectCreationExpr n, NameScopeWalker currentScope) {
		NameScopeWalker nextScope = currentScope;
		if (n.getAnonymousClassBody() != null) {
			nextScope = currentScope.nextChild();
		}
		super.visit(n, nextScope);
	}

	@Override
	public void visit(ConstructorDeclaration n, NameScopeWalker currentScope) {
		super.visit(n, currentScope.nextChild());
	}

	@Override
	public void visit(EnumDeclaration n, NameScopeWalker currentScope) {
		super.visit(n, currentScope);
	}

	@Override
	public void visit(ForeachStmt n, NameScopeWalker currentScope) {
		super.visit(n, currentScope.nextChild());
	}

	@Override
	public void visit(ForStmt n, NameScopeWalker currentScope) {
		super.visit(n, currentScope.nextChild());
	}

	private String nodeNameWithScope(Expression node) {
	  return node.accept(new GenericVisitorAdapter<String, String>() {
      @Override
      public String visit(FieldAccessExpr parentExpr, String arg) {
        if (parentExpr.getScope() != null) {
          String parentName = nodeNameWithScope(parentExpr.getScope());
          if (parentName != null) {
            return parentName+"."+parentExpr.getField();
          }
        }
        return parentExpr.getField();
      }
      @Override
      public String visit(NameExpr n, String arg) {
        return n.getName();
      }
    }, null);
	}
	/*------ method having to resolve identifiers ---------*/
  @Override
  public void visit(MethodCallExpr n, NameScopeWalker currentScope) {
    String name = n.getName();
    if (n.getScope() != null) {
      name = nodeNameWithScope(n.getScope()) +"."+name;
    }
    // only for methods without a scope
    SourcePosition pos = new SourcePosition(n);
    QualifiedName<MethodName> qname = currentScope.getScope().resolveMethod(pos, name);
    if (qname != null) {
      checkNonStaticAccessToOuterScope(currentScope, pos, qname);
      n.setData(qname);
    }

    super.visit(n, currentScope);
  }

  private void checkNonStaticAccessToOuterScope(NameScopeWalker currentScope, SourcePosition pos,
      QualifiedName<?> qname) {
    if (qname.isAccessingOuterScope() && !qname.isStatic()) {
    	throw new JavascriptGenerationException(
    			currentScope.getScope().getInputFile(),
    			pos,
    			"In Javascript you cannot call methods or fields from the outer type. "
    					+ "You should define a variable var that=this outside your function definition and call the methods on this object");
    }
  }

	@Override
	public void visit(FieldAccessExpr n, NameScopeWalker currentScope) {
		SourcePosition pos = new SourcePosition(n);
		// try to figure out if it's variable.field or Package.Class.field
		QualifiedName<IdentifierName> qname = currentScope.getScope().resolveIdentifier(pos, getFirstScope(n));
		if (qname == null) {
		  QualifiedName<TypeName> resolvedType = currentScope.getScope().resolveType(pos, getFirstScope(n));
		  if (resolvedType != null) {
		    // no need to persist it
		    return;
		  }
		}
		if (qname == null) {
			checkImport(n, n.getScope().toString(), currentScope);
			qname = currentScope.getScope().resolveIdentifier(pos, n.toString());
		}
		if (qname != null) {
			n.setData(qname);
		}
	}

	private String getFirstScope(FieldAccessExpr n) {
		if (n.getScope() instanceof FieldAccessExpr) {
			return getFirstScope((FieldAccessExpr) n.getScope());
		}
		return n.getScope().toString();
	}

  /**
   * throws an exception if none of the allowedPackages is found as parent
   * package of the given declaration
   * 
   * @param importDecl
   */
  private void checkImport(Node n, String importName, NameScopeWalker currentScope)
      throws JavascriptGenerationException {
    if (importName.equals("this")) {
      return;
    }
    if (importName.startsWith("java.lang.")) {
      String checkClass = importName.substring("java.lang.".length());
      if (allowedJavaLangClasses.contains(checkClass)) {
        return;
      }
    } else {
      for (String allowedPackage : allowedPackages) {
        if (importName.startsWith(allowedPackage)) {
          return;
        }
      }
    }
    try {
      throw new JavascriptGenerationException(currentScope.getScope().getInputFile(),
          new SourcePosition(n),
          "The qualified name:" + importName + " is not part of the allowed packages");
    } catch (JavascriptGenerationException e) {
      // e.printStackTrace();
      throw e;
    }
  }

	@Override
	public void visit(NameExpr n, NameScopeWalker currentScope) {
		SourcePosition pos = new SourcePosition(n);
		QualifiedName<IdentifierName> qname = currentScope.getScope().resolveIdentifier(pos, n.getName());
		if (qname != null) {
			checkNonStaticAccessToOuterScope(currentScope, pos, qname);
			n.setData(qname);
		}
		super.visit(n, currentScope);
	}
	

	@Override
	public void visit(ClassOrInterfaceType n, NameScopeWalker currentScope) {
		SourcePosition pos = new SourcePosition(n);
		StringBuilder fullName = new StringBuilder(n.getName());
		if (n.getScope() == null) {
			// not fully-specified classes
		  QualifiedName<TypeName> qname = currentScope.getScope().resolveType(pos, n.getName());
			if (qname != null) {
			    fullName = new StringBuilder(qname.getDefinitionPoint().getOrThrow().getFullName(true).getOrThrow());			    
			    n.setData(qname);
			    if (qname.getType() == INNER_CLASS) {
			      // no need to check for imports
			      super.visit(n, currentScope);
			      return;
			    }
			}
		} else {
		  for (ClassOrInterfaceType t = n.getScope(); t != null; t = t.getScope()) {
	      fullName.insert(0, t.getName() + ".");
	    }
		}
		checkImport(n, fullName.toString(), currentScope);

		super.visit(n, currentScope);
	}

	@Override
	public void visit(PrimitiveType n, NameScopeWalker currentScope) {
		super.visit(n, currentScope);
	}
}
