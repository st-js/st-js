package org.stjs.generator.scope;

import org.stjs.generator.handlers.utils.PreConditions;

public class QualifiedName<T extends NameType> {
	private final String scopeName;
	private final String name;
	private final NameScope scope;

	public QualifiedName(String scopeName, String name, NameScope scope) {
		this.scopeName = scopeName;
		this.name = name;
		this.scope = scope;
		PreConditions.checkNotNull(scope);
	}

	/**
	 * the scope name can be null for parameters and variables, can be "this" for immediate fields, FullClass.this for
	 * references to outer class FullClass for static references.
	 * 
	 * @return
	 */
	public String getScopeName() {
		return scopeName;
	}

	public String getName() {
		return name;
	}

	/**
	 * This is the name scope in which the name was found
	 * 
	 * @return
	 */
	public NameScope getScope() {
		return scope;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		if (scopeName != null) {
			s.append(scopeName).append(".");
		}
		s.append(name);
		s.append("[" + scope.getPath() + "]");
		return s.toString();
	}

}
