package org.stjs.generator.scope;

import java.util.HashSet;
import java.util.Set;

import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;

/**
 * This scope is for the a method's parameters.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ParameterScope extends NameScope {
	private static final String PARAMETER_SCOPE_NAME = null;

	private final Set<String> parameters = new HashSet<String>();

	public ParameterScope(String name, NameScope parent) {
		super(name, parent);
	}

	public ParameterScope(String name, NameScope parent, String parameter) {
		this(name, parent);
		parameters.add(parameter);
	}

	public void addParameter(String parameter) {
		parameters.add(parameter);
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(String name, NameScope currentScope) {
		if (parameters.contains(name)) {
			return new QualifiedName<IdentifierName>(PARAMETER_SCOPE_NAME, name, this);
		}
		if (getParent() != null) {
			return getParent().resolveIdentifier(name, currentScope);
		}
		return null;
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(String name, NameScope currentScope) {
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
