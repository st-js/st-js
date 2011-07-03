package org.stjs.generator.scope;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;

/**
 * This visitor goes through the AST and resolves all the found identifiers using the {@link NameScopeWalker} previously
 * built from the same source.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class NameResolverVisitor extends VoidVisitorAdapter<NameScopeWalker> {
	private final Map<SourcePosition, QualifiedName<MethodName>> resolvedMethods = new LinkedHashMap<SourcePosition, QualifiedName<MethodName>>();
	private final Map<SourcePosition, QualifiedName<IdentifierName>> resolvedIdentifiers = new LinkedHashMap<SourcePosition, QualifiedName<IdentifierName>>();

	private final NameScope rootScope;

	public NameResolverVisitor(NameScope rootScope) {
		this.rootScope = rootScope;
	}

	public NameScope getRootScope() {
		return rootScope;
	}

	public Map<SourcePosition, QualifiedName<MethodName>> getResolvedMethods() {
		return resolvedMethods;
	}

	public Map<SourcePosition, QualifiedName<IdentifierName>> getResolvedIdentifiers() {
		return resolvedIdentifiers;
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
		// TODO Auto-generated method stub
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

	/*------ method having to resolve identifiers ---------*/
	@Override
	public void visit(MethodCallExpr n, NameScopeWalker currentScope) {
		if (n.getScope() == null) {
			// only for methods without a scope
			SourcePosition pos = new SourcePosition(n.getBeginLine(), n.getBeginColumn());
			QualifiedName<MethodName> qname = currentScope.getScope().resolveMethod(pos, n.getName());
			if (qname != null) {
				resolvedMethods.put(pos, qname);
			}
		}
		super.visit(n, currentScope);
	}

	@Override
	public void visit(QualifiedNameExpr n, NameScopeWalker arg) {
		System.out.println("Q:" + n.getQualifier().getName() + "!!" + n.getName());
		super.visit(n, arg);
	}

	@Override
	public void visit(NameExpr n, NameScopeWalker currentScope) {
		SourcePosition pos = new SourcePosition(n.getBeginLine(), n.getBeginColumn());
		QualifiedName<IdentifierName> qname = currentScope.getScope().resolveIdentifier(pos, n.getName());
		if (qname != null) {
			resolvedIdentifiers.put(pos, qname);
		}
		super.visit(n, currentScope);
	}

}
