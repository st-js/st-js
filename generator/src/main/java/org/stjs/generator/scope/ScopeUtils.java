package org.stjs.generator.scope;

import static org.stjs.generator.scope.classloader.ClassWrapper.wrap;
import japa.parser.ast.expr.MethodCallExpr;

import java.lang.reflect.Method;

import org.stjs.generator.ASTNodeData;
import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.generator.scope.simple.ClassScope;
import org.stjs.generator.scope.simple.Scope;

public class ScopeUtils {
	public static boolean isDeclaredInThisScope(MethodCallExpr n) {
		Scope scope = ASTNodeData.scope(n);
		Method method = ASTNodeData.method(n);
		ClassWrapper methodDeclaringClass = wrap(method.getDeclaringClass());
		ClassScope thisClassScope = scope.closest(ClassScope.class);
		return (thisClassScope.getClazz().equals(methodDeclaringClass));
	}

}
