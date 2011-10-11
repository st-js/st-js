package org.stjs.generator.scope.simple;

import java.util.List;


public class BasicScope extends AbstractScope {

	BasicScope(Scope parent) {
		super(parent);
	}

	@Override
	public void apply(ScopeVisitor visitor) {
		visitor.apply(this);
	}


}
