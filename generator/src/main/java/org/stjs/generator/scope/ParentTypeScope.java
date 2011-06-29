package org.stjs.generator.scope;

import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;

/**
 * This scope is for fields and methods inherited from a parent type.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ParentTypeScope extends NameScope {
	private final String className;

	public ParentTypeScope(NameScope parent, String className) {
		super("parent-" + className, parent);
		this.className = className;
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(String name, NameScope currentScope) {
		if (getParent() != null) {
			return getParent().resolveMethod(name, currentScope);
		}
		return null;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(String name, NameScope currentScope) {
		if (getParent() != null) {
			return getParent().resolveIdentifier(name, currentScope);
		}
		return null;
	}

	@Override
	public String toString() {
		return "ParentTypeScope [className=" + className + ", getChildren()=" + getChildren() + "]";
	}
}
