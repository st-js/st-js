package org.stjs.generator.scope;

import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;

/**
 * This scope tries to resolve only fully qualified names by looking in the classpath for the corresponding type.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class FullyQualifiedScope extends NameScope {

	public FullyQualifiedScope() {
		super("root", null);
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(String name, NameScope currentScope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(String name, NameScope currentScope) {
		// TODO Auto-generated method stub
		return null;
	}

}
