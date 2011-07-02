package org.stjs.generator.scope;

import static org.stjs.generator.handlers.utils.Sets.transform;
import static org.stjs.generator.handlers.utils.Sets.union;

import java.util.HashSet;
import java.util.Set;

import org.stjs.generator.handlers.utils.Function;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;

/**
 * This scope is for a class definition. It contains the name of the fields and methods
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class TypeScope extends NameScope {

	public static final String THIS_SCOPE = "this";

	private static final String OUTER_SCOPE = "outer";

	private static final String STATIC_SCOPE = null;

	private final Set<String> staticFields = new HashSet<String>();
	private final Set<String> staticMethods = new HashSet<String>();
	private final Set<String> instanceFields = new HashSet<String>();
	private final Set<String> instanceMethods = new HashSet<String>();

	public TypeScope(String name, NameScope parent) {
		super(name, parent);
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
	protected QualifiedName<MethodName> resolveMethod(String name, NameScope currentScope) {
		if (instanceMethods.contains(name)) {
			if (isInCurrentTypeScope(currentScope)) {
				return new QualifiedName<MethodName>(THIS_SCOPE, name, this);
			}
			return new QualifiedName<MethodName>(OUTER_SCOPE, name, this);
		}
		if (staticMethods.contains(name)) {
			return new QualifiedName<MethodName>(STATIC_SCOPE, name, this);
		}
		if (getParent() != null) {
			return getParent().resolveMethod(name, currentScope);
		}
		return null;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(String name, NameScope currentScope) {
		if (instanceFields.contains(name)) {
			if (isInCurrentTypeScope(currentScope)) {
				return new QualifiedName<IdentifierName>(THIS_SCOPE, name, this);
			}
			return new QualifiedName<IdentifierName>(OUTER_SCOPE, name, this);
		}
		if (staticFields.contains(name)) {
			return new QualifiedName<IdentifierName>(STATIC_SCOPE, name, this);
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
	public Set<QualifiedName<IdentifierName>> getOwnIdentifiers() {
		return transform(union(staticFields, instanceFields), new ToQualifiedName<IdentifierName>());
	}

	private class ToQualifiedName<T extends NameType> implements Function<String, QualifiedName<T>> {

		@Override
		public QualifiedName<T> apply(String name) {
			return new QualifiedName<T>(null, name, TypeScope.this);
		}

	}

	@Override
	public Set<QualifiedName<MethodName>> getOwnMethods() {
		return transform(union(staticMethods, instanceMethods), new ToQualifiedName<MethodName>());
	}

	@Override
	public String toString() {
		return "TypeScope [staticFields=" + staticFields + ", staticMethods=" + staticMethods + ", instanceFields="
				+ instanceFields + ", instanceMethods=" + instanceMethods + ", getChildren()=" + getChildren() + "]";
	}

}
