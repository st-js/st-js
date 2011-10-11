package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.classloader.ClassWrapper;

public interface Scope {

	public interface ScopeVisitor {
		void apply(CompilationUnitScope scope);

		void apply(ClassScope classScope);

		void apply(BasicScope basicScope);
	}
	
	void apply(ScopeVisitor visitor);
	
	public ClassWrapper resolveType(String name);

	Scope addChild(Scope abstractScope);
}
