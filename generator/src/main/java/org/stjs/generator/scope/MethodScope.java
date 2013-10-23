package org.stjs.generator.scope;

import org.stjs.generator.GenerationContext;

/**
 * this is the scope of a method or a constructor
 * 
 * @author acraciun
 * 
 */
public class MethodScope extends AbstractScope {
	MethodScope(Scope parent, GenerationContext context) {
		super(parent, context);
	}

	@Override
	public <T> T apply(ScopeVisitor<T> visitor) {
		return visitor.apply(this);
	}

}
