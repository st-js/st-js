package org.stjs.generator.scope;

import java.util.HashSet;
import java.util.Set;

/**
 * This scope is for the a method's parameters.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ParameterScope extends NameScope {
	private static final String PARAMETER_SCOPE_NAME = null;

	private final Set<String> parameters = new HashSet<String>();

	public ParameterScope(NameScope parent) {
		super(parent);
	}

	public ParameterScope(NameScope parent, String parameter) {
		this(parent);
		parameters.add(parameter);
	}

	public void addParameter(String parameter) {
		parameters.add(parameter);
	}

	@Override
	public QualifiedName resolveIdentifier(String name, NameScope currentScope) {
		if (parameters.contains(name)) {
			return new QualifiedName(PARAMETER_SCOPE_NAME, name);
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
		return "ParameterScope [parameters=" + parameters + ", getChildren()=" + getChildren() + "]";
	}
}
