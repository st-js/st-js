package org.stjs.generator.scope.simple;

import java.util.List;

public interface Scope {

	public interface ScopeVisitor<T> {
		T apply(CompilationUnitScope scope);

		T apply(ClassScope classScope);

		T apply(BasicScope basicScope);
	}

	<T> T apply(ScopeVisitor<T> visitor);

	public TypeWithScope resolveType(String name);

	public VariableWithScope resolveVariable(String string);

	public MethodsWithScope resolveMethods(String name);

	Scope addChild(Scope abstractScope);

	List<Scope> getChildren();

	Scope getParent();

	/**
	 * Find the closest parent of type T
	 * 
	 * @param <T>
	 * @param scopeType
	 * @return
	 */
	<T extends Scope> T closest(Class<T> scopeType);
}
