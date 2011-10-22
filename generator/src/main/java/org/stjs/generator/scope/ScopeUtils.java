package org.stjs.generator.scope;

import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.ASTNodeData;
import org.stjs.generator.scope.classloader.MethodWrapper;
import org.stjs.generator.scope.classloader.TypeWrapper;
import org.stjs.generator.scope.simple.ClassScope;
import org.stjs.generator.scope.simple.Scope;

public class ScopeUtils {
	public static boolean isDeclaredInThisScope(MethodCallExpr n) {
		Scope scope = ASTNodeData.scope(n);
		MethodWrapper method = ASTNodeData.resolvedMethod(n);
		TypeWrapper methodDeclaringClass = method.getOwnerType();
		ClassScope thisClassScope = scope.closest(ClassScope.class);
		return (thisClassScope.getClazz().equals(methodDeclaringClass));
	}

}
