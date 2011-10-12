package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.simple.Scope.ScopeVisitor;

public class DefaultScopeVisitor<T> implements ScopeVisitor<T> {

	@Override
	public T apply(CompilationUnitScope scope) {
		return null;
	}

	@Override
	public T apply(ClassScope classScope) {
		return null;
	}

	@Override
	public T apply(BasicScope basicScope) {
		return null;
	}

}
