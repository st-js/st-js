package org.stjs.generator.scope.simple;

public class BasicScope extends AbstractScope {

	BasicScope(Scope parent) {
		super(parent);
	}

	@Override
	public <T> T apply(ScopeVisitor<T> visitor) {
		return visitor.apply(this);
	}

}
