package org.stjs.generator.scope;

/**
 * This scope is for fields and methods inherited from a parent type.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ParentTypeScope extends NameScope {
	private final String className;

	public ParentTypeScope(NameScope parent, String className) {
		super(parent);
		this.className = className;
	}

	@Override
	public QualifiedName resolveMethod(String name, NameScope currentScope) {
		if (getParent() != null) {
			return getParent().resolveMethod(name, currentScope);
		}
		return null;
	}

	@Override
	public QualifiedName resolveIdentifier(String name, NameScope currentScope) {
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
