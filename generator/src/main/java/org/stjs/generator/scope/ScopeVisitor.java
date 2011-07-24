package org.stjs.generator.scope;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.JavascriptKeywords;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.handlers.utils.PreConditions;

/**
 * This class visits the code's tree to determine the scope of each name.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ScopeVisitor extends VoidVisitorAdapter<NameScope> {

	private final File inputFile;
	/**
	 * this is the class loader where the class corresponding to the parsed source is defined
	 */
	private final ClassLoader classLoader;

	private final Collection<String> allowedPackages;

	public ScopeVisitor(File inputFile, ClassLoader classLoader, Collection<String> allowedPackages) {
		super();
		this.classLoader = classLoader;
		this.inputFile = inputFile;
		this.allowedPackages = allowedPackages;
	}

	@Override
	public void visit(CompilationUnit n, NameScope currentScope) {
		ImportScope scope = new ImportScope(inputFile, currentScope, n.getPackage() != null ? n.getPackage().getName()
				.toString() : null, classLoader);
		if (n.getImports() != null) {
			for (ImportDeclaration importDecl : n.getImports()) {
				checkImport(importDecl);
				scope.addImport(importDecl.getName().toString(), importDecl.isStatic(), importDecl.isAsterisk());
			}
		}
		super.visit(n, scope);
	}

	/**
	 * throws an exception if none of the allowedPackages is found as parent package of the given declaration
	 * 
	 * @param importDecl
	 */
	private void checkImport(ImportDeclaration importDecl) throws JavascriptGenerationException {
		String importName = importDecl.getName().toString();
		for (String allowedPackage : allowedPackages) {
			if (importName.startsWith(allowedPackage)) {
				return;
			}
		}
		throw new JavascriptGenerationException(inputFile, new SourcePosition(importDecl), "The import declaration:"
				+ importName + " is not part of the allowed packages");
	}

	@Override
	public void visit(BlockStmt n, NameScope currentScope) {
		super.visit(n, new VariableScope(inputFile, "block-" + n.getBeginLine(), currentScope));
	}

	@Override
	public void visit(VariableDeclarationExpr n, NameScope currentScope) {
		PreConditions.checkState(currentScope instanceof VariableScope, "A VariableScope expected");
		if (n.getVars() != null) {
			VariableScope varScope = (VariableScope) currentScope;
			for (VariableDeclarator var : n.getVars()) {
				JavascriptKeywords.checkIdentifier(inputFile, new SourcePosition(var), var.getId().getName());
				varScope.addVariable(var.getId().getName());
			}
		}

		super.visit(n, currentScope);
	}

	@Override
	public void visit(CatchClause n, NameScope currentScope) {
		JavascriptKeywords.checkIdentifier(inputFile, new SourcePosition(n), n.getExcept().getId().getName());
		super.visit(n, new ParameterScope(inputFile, "catch-" + n.getBeginLine(), currentScope, n.getExcept().getId()
				.getName()));
	}

	@Override
	public void visit(MethodDeclaration n, NameScope currentScope) {
		ParameterScope scope = new ParameterScope(inputFile, "param-" + n.getBeginLine(), currentScope);
		if (n.getParameters() != null) {
			for (Parameter p : n.getParameters()) {
				JavascriptKeywords.checkIdentifier(inputFile, new SourcePosition(p), p.getId().getName());
				scope.addParameter(p.getId().getName());
			}
		}
		super.visit(n, scope);
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, NameScope currentScope) {
		NameScope classScope = currentScope;
		if (n.getExtends() != null && n.getExtends().size() > 0 && !n.isInterface()) {
			String parentClassName = n.getExtends().get(0).getName();

			classScope = new ParentTypeScope(inputFile, classScope, parentClassName);
		}

		if (n.getMembers() != null) {
			classScope = visitTypeDeclaration("type-" + n.getName(), n.getMembers(), classScope);
		}
		super.visit(n, classScope);
	}

	private NameScope visitTypeDeclaration(String name, List<BodyDeclaration> declarations, NameScope currentScope) {
		TypeScope typeScope = new TypeScope(inputFile, name, currentScope);
		for (BodyDeclaration member : declarations) {

			if (member instanceof FieldDeclaration) {
				FieldDeclaration field = (FieldDeclaration) member;
				for (VariableDeclarator var : field.getVariables()) {
					SourcePosition sourcePosition = new SourcePosition(var);
					JavascriptKeywords.checkIdentifier(inputFile, sourcePosition, var.getId().getName());
					if (ModifierSet.isStatic(field.getModifiers())) {
						typeScope.addStaticField(var.getId().getName(), sourcePosition);
					} else {
						typeScope.addInstanceField(var.getId().getName(), sourcePosition);
					}
				}
			} else if (member instanceof MethodDeclaration) {
				MethodDeclaration method = (MethodDeclaration) member;
				SourcePosition sourcePosition = new SourcePosition(method);
				JavascriptKeywords.checkMethod(inputFile, sourcePosition, method.getName());
				if (ModifierSet.isStatic(method.getModifiers())) {
					typeScope.addStaticMethod(method.getName(), sourcePosition);
				} else {
					typeScope.addInstanceMethod(method.getName(), sourcePosition);
				}
			} else if (member instanceof TypeDeclaration) {
				TypeDeclaration type = (TypeDeclaration) member;
				typeScope.addInnerType(type.getName());
			}
		}
		return typeScope;
	}

	@Override
	public void visit(ObjectCreationExpr n, NameScope currentScope) {
		NameScope newScope = currentScope;
		if (n.getAnonymousClassBody() != null) {
			newScope = visitTypeDeclaration("anonymous-" + n.getBeginLine(), n.getAnonymousClassBody(), currentScope);
		}
		super.visit(n, newScope);
	}

	@Override
	public void visit(ConstructorDeclaration n, NameScope currentScope) {
		ParameterScope paramScope = new ParameterScope(inputFile, "constructor-" + n.getBeginLine(), currentScope);
		if (n.getParameters() != null) {
			for (Parameter p : n.getParameters()) {
				paramScope.addParameter(p.getId().getName());
			}
		}
		super.visit(n, paramScope);
	}

	@Override
	public void visit(EnumDeclaration n, NameScope arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	@Override
	public void visit(ForeachStmt n, NameScope currentScope) {
		super.visit(n, new VariableScope(inputFile, "foreach-" + n.getBeginLine(), currentScope));
	}

	@Override
	public void visit(ForStmt n, NameScope currentScope) {
		super.visit(n, new VariableScope(inputFile, "for-" + n.getBeginLine(), currentScope));
	}

}
