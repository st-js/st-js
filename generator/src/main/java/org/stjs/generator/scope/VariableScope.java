package org.stjs.generator.scope;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;

/**
 * This scope is for the variables defined within a block
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class VariableScope extends NameScope {
	private static final String VARIABLE_SCOPE_NAME = null;

	private final Set<String> variables = new HashSet<String>();

	public VariableScope(File inputFile, String name, NameScope parent) {
		super(inputFile, name, parent);
	}

	public void addVariable(String var) {
		variables.add(var);
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope) {
		if (variables.contains(name)) {
			return new QualifiedName<IdentifierName>(VARIABLE_SCOPE_NAME, name, this);
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
		return "VariableScope [variables=" + variables + ", getChildren()=" + getChildren() + "]";
	}
}
