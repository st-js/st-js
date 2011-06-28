package org.stjs.generator.scope;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.visitor.VoidVisitorAdapter;

/**
 * This class visits the code's tree to determine the scope of each name. The problem is that a f
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ScopeVisitor extends VoidVisitorAdapter<Boolean> {
	private final ImportScope rootScope = new ImportScope();
	private NameScope currentScope = rootScope;

	public ImportScope getRootScope() {
		return rootScope;
	}

	@Override
	public void visit(BlockStmt n, Boolean arg) {
		currentScope = new VariableScope(currentScope);
		super.visit(n, arg);
		currentScope = currentScope.getParent();
	}

	@Override
	public void visit(CatchClause n, Boolean arg) {
		currentScope = new ParameterScope(currentScope, n.getExcept().getId().getName());
		super.visit(n, arg);
		currentScope = currentScope.getParent();
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Boolean arg) {
		NameScope saveScope = currentScope;
		if (n.getExtends() != null && n.getExtends().size() > 0 && !n.isInterface()) {
			currentScope = new ParentTypeScope(currentScope, n.getExtends().get(0).getName());
		}

		if (n.getMembers() != null) {
			TypeScope typeScope = new TypeScope(currentScope);
			currentScope = typeScope;
			for (BodyDeclaration member : n.getMembers()) {
				if (member instanceof FieldDeclaration) {
					FieldDeclaration field = (FieldDeclaration) member;
					for (VariableDeclarator var : field.getVariables()) {
						if (ModifierSet.isStatic(field.getModifiers())) {
							typeScope.addStaticField(var.getId().getName());
						} else {
							typeScope.addInstanceField(var.getId().getName());
						}
					}
				} else if (member instanceof MethodDeclaration) {
					MethodDeclaration method = (MethodDeclaration) member;
					if (ModifierSet.isStatic(method.getModifiers())) {
						typeScope.addStaticMethod(method.getName());
					} else {
						typeScope.addInstanceMethod(method.getName());
					}

				}
				member.accept(this, arg);
			}

		}
		super.visit(n, arg);
		currentScope = saveScope;
	}

	@Override
	public void visit(ConstructorDeclaration n, Boolean arg) {
		ParameterScope paramScope = new ParameterScope(currentScope);
		for (Parameter p : n.getParameters()) {
			paramScope.addParameter(p.getId().getName());
		}
		currentScope = paramScope;
		super.visit(n, arg);
		currentScope = currentScope.getParent();
	}

	@Override
	public void visit(EnumDeclaration n, Boolean arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	@Override
	public void visit(ForeachStmt n, Boolean arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	@Override
	public void visit(ForStmt n, Boolean arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	@Override
	public void visit(MethodCallExpr n, Boolean arg) {
		// here identify call
		super.visit(n, arg);
	}

	@Override
	public void visit(SwitchStmt n, Boolean arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

}
