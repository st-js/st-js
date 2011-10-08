package org.stjs.generator.scope.simple;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.stjs.generator.scope.classloader.ClassLoaderWrapper;
import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.generator.utils.Option;

public class SimpleScopeBuilder extends VoidVisitorAdapter<CompilationUnitScope> {

	private final ClassLoaderWrapper classLoader;

	public SimpleScopeBuilder(ClassLoaderWrapper classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public void visit(CompilationUnit n, CompilationUnitScope scope) {
		// asteriks declaration have lower priority => process them first (JLS §7.5.2)
		if (n.getImports() != null) {
			for (ImportDeclaration importDecl : n.getImports()) {
				NameExpr name = importDecl.getName();
				checkImport(importDecl, name.toString());
				if (importDecl.isAsterisk()) {
					if (importDecl.isStatic()) {
						QualifiedNameExpr expr = (QualifiedNameExpr) name;
						for (ClassWrapper clazz : identifyQualifiedNameExprClass(expr.getQualifier())) {
							for (Field field : clazz.getDeclaredNonPrivateStaticFields()) {
								scope.addField(field.getName(), field);
							}
							for (Method method : clazz.getDeclaredNonPrivateStaticMethods()) {
								scope.addMethod(method.getName(), method);
							}
							for (ClassWrapper type : clazz.getDeclaredNonPrivateStaticClasses()) {
								scope.addType(type.getSimpleName(), type);
							}
						}
					} else {
						scope.addTypeImportOnDemand(name);
					}
				}
			}
		}
		if (n.getImports() != null) {
			for (ImportDeclaration importDecl : n.getImports()) {
				NameExpr name = importDecl.getName();
				checkImport(importDecl, name.toString());
				if (!importDecl.isAsterisk()) {
					if (importDecl.isStatic()) {
						QualifiedNameExpr expr = (QualifiedNameExpr) name;
						for (ClassWrapper clazz : identifyQualifiedNameExprClass(expr.getQualifier())) {
							String fieldOrTypeOrMethodName = name.getName();
							for (Field field : clazz.getDeclaredField(fieldOrTypeOrMethodName)) {
								scope.addField(fieldOrTypeOrMethodName, field);
							}
							List<Method> methods = clazz.getDeclaredMethods(fieldOrTypeOrMethodName);
							if (!methods.isEmpty()) {
								scope.addMethods(fieldOrTypeOrMethodName, methods);
							}
							for (ClassWrapper innerClass : clazz.getDeclaredClass(fieldOrTypeOrMethodName)) {
								scope.addType(innerClass.getSimpleName(), innerClass);
								break;
							}
							// TODO : do wee need to continue here?
						}
					}
					for (ClassWrapper clazz : identifyQualifiedNameExprClass(name)) {
						scope.addType(name.getName(), clazz);

					}
				}
			}
		}
		super.visit(n, scope);
	}

	private Option<ClassWrapper> identifyQualifiedNameExprClass(NameExpr expr) {

		String classLoaderCompatibleName = expr.toString();
		while (true) {
			Option<ClassWrapper> clazz = classLoader.loadClass(classLoaderCompatibleName);
			if (clazz.isDefined()) {
				return clazz;
			}
			int lastIndexDot = classLoaderCompatibleName.lastIndexOf('.');
			if (lastIndexDot < 0) {
				return Option.none();
			}
			classLoaderCompatibleName = replaceCharAt(classLoaderCompatibleName, lastIndexDot, '$');
		}
	}

	private String replaceCharAt(String s, int pos, char c) {
		StringBuffer buf = new StringBuffer(s);
		buf.setCharAt(pos, c);
		return buf.toString();
	}

	private void checkImport(ImportDeclaration importDecl, String string) {
		// TODO Auto-generated method stub

	}
}