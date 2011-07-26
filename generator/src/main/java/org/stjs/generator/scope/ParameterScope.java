package org.stjs.generator.scope;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;

/**
 * This scope is for the a method's parameters.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ParameterScope extends NameScope {
	private static final String PARAMETER_SCOPE_NAME = null;

	private final Set<String> parameters = new HashSet<String>();

	public ParameterScope(File inputFile, String name, NameScope parent) {
		super(inputFile, name, parent);
	}

	public ParameterScope(File inputFile, String name, NameScope parent, String parameter) {
		this(inputFile, name, parent);
		parameters.add(parameter);
	}

	public void addParameter(String parameter) {
		parameters.add(parameter);
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope) {
		if (parameters.contains(name)) {
			return new QualifiedName<IdentifierName>(PARAMETER_SCOPE_NAME, name, this);
		}
		if (getParent() != null) {
			return getParent().resolveIdentifier(pos, name, currentScope);
		}
		return null;
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope) {

		if (getParent() != null) {
			return getParent().resolveMethod(pos, name, currentScope);
		}
		return null;
	}

	@Override
	public String toString() {
		return "ParameterScope [parameters=" + parameters + ", getChildren()=" + getChildren() + "]";
	}

	@Override
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope) {
		if (getParent() != null) {
			return getParent().resolveType(pos, name, currentScope);
		}
		return null;
	}
}
