package org.stjs.generator.scope.simple;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.stjs.generator.scope.classloader.ClassWrapper;

public interface Scope {

	public interface ScopeVisitor<T> {
		T apply(CompilationUnitScope scope);

		T apply(ClassScope classScope);

		T apply(BasicScope basicScope);
	}
	
	<T> T apply(ScopeVisitor<T> visitor);
	
	public ClassWrapper resolveType(String name);

	Scope addChild(Scope abstractScope);

	List<Scope> getChildren();

	Variable resolveVariable(String string);

	Collection<Method> resolveMethods(String name);

	Scope getParent();
	
	/**
	 * Find the closest parent of type T
	 * @param <T>
	 * @param scopeType
	 * @return
	 */
	<T extends Scope> T closest(Class<T> scopeType);
}
