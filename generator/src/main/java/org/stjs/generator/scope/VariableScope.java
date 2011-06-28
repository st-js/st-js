package org.stjs.generator.scope;

import java.util.HashSet;
import java.util.Set;

/**
 * This scope is for the variables defined within a block
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class VariableScope extends NameScope {
	private static final String VARIABLE_SCOPE_NAME = null;

	private final Set<String> variables = new HashSet<String>();

	public VariableScope(NameScope parent) {
		super(parent);
	}

	public void addParameter(String parameter) {
		variables.add(parameter);
	}

	@Override
	public QualifiedName resolveIdentifier(String name, NameScope currentScope) {
		if (variables.contains(name)) {
			return new QualifiedName(VARIABLE_SCOPE_NAME, name);
		}
		if (getParent() != null) {
			return getParent().resolveIdentifier(name, currentScope);
		}
		return null;
	}

	@Override
	public QualifiedName resolveMethod(String name, NameScope currentScope) {
		if (getParent() != null) {
			return getParent().resolveMethod(name, currentScope);
		}
		return null;
	}

	@Override
	public String toString() {
		return "VariableScope [variables=" + variables + ", getChildren()=" + getChildren() + "]";
	}
}
