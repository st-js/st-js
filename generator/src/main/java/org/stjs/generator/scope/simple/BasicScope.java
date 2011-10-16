package org.stjs.generator.scope.simple;

import org.stjs.generator.GenerationContext;

public class BasicScope extends AbstractScope {

	BasicScope(Scope parent, GenerationContext context) {
		super(parent, context);
	}

	@Override
	public <T> T apply(ScopeVisitor<T> visitor) {
		return visitor.apply(this);
	}

}
