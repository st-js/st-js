package org.stjs.generator.scope.simple;

public class CompilationUnitScope {

	private Package packageDeclaration;

	public CompilationUnitScope(Package packageDeclaration) {
		// super(null);
		this.packageDeclaration = packageDeclaration;
	}

	public Package getPackage() {
		return packageDeclaration;
	}

	// @Override
	// public void apply(ScopeVisitor visitor) {
	// visitor.apply(this);
	// }

}
