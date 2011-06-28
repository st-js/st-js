package org.stjs.generator.scope;

import java.util.HashSet;
import java.util.Set;

/**
 * This scope is for a class definition. It contains the name of the fields and methods
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class TypeScope extends NameScope {

	private static final String THIS_SCOPE = "this";

	private static final String OUTER_SCOPE = "outer";

	private static final String STATIC_SCOPE = null;

	private final Set<String> staticFields = new HashSet<String>();
	private final Set<String> staticMethods = new HashSet<String>();
	private final Set<String> instanceFields = new HashSet<String>();
	private final Set<String> instanceMethods = new HashSet<String>();

	public TypeScope(NameScope parent) {
		super(parent);
	}

	private boolean isInCurrentTypeScope(NameScope scope) {
		if (scope == null) {
			return true;
		}
		if (scope == this) {
			return true;
		}
		if (scope instanceof TypeScope) {
			return false;
		}
		if (scope.getParent() == null) {
			return false;
		}
		return isInCurrentTypeScope(scope.getParent());
	}

	@Override
	public QualifiedName resolveMethod(String name, NameScope currentScope) {
		if (instanceMethods.contains(name)) {
			if (isInCurrentTypeScope(currentScope)) {
				return new QualifiedName(THIS_SCOPE, name);
			}
			return new QualifiedName(OUTER_SCOPE, name);
		}
		if (staticMethods.contains(name)) {
			return new QualifiedName(STATIC_SCOPE, name);
		}
		if (getParent() != null) {
			return getParent().resolveMethod(name, currentScope);
		}
		return null;
	}

	@Override
	public QualifiedName resolveIdentifier(String name, NameScope currentScope) {
		if (instanceFields.contains(name)) {
			if (isInCurrentTypeScope(currentScope)) {
				return new QualifiedName(THIS_SCOPE, name);
			}
			return new QualifiedName(OUTER_SCOPE, name);
		}
		if (staticFields.contains(name)) {
			return new QualifiedName(STATIC_SCOPE, name);
		}
		if (getParent() != null) {
			return getParent().resolveIdentifier(name, currentScope);
		}
		return null;
	}

	public void addStaticField(String name) {
		staticFields.add(name);
	}

	public void addStaticMethod(String name) {
		staticMethods.add(name);
	}

	public void addInstanceField(String name) {
		instanceFields.add(name);
	}

	public void addInstanceMethod(String name) {
		instanceMethods.add(name);
	}

	@Override
	public String toString() {
		return "TypeScope [staticFields=" + staticFields + ", staticMethods=" + staticMethods + ", instanceFields="
				+ instanceFields + ", instanceMethods=" + instanceMethods + ", getChildren()=" + getChildren() + "]";
	}

}
