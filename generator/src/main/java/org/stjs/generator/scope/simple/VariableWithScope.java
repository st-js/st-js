package org.stjs.generator.scope.simple;

public class VariableWithScope {
	private final Scope scope;
	private final Variable variable;

	VariableWithScope(Scope scope, Variable variable) {
		super();
		this.scope = scope;
		this.variable = variable;
	}

	public Scope getScope() {
		return scope;
	}

	public Variable getVariable() {
		return variable;
	}

}
