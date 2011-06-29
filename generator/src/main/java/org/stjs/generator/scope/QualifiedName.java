package org.stjs.generator.scope;

public class QualifiedName<T extends NameType> {
	private final String scopeName;
	private final String name;

	public QualifiedName(String scopeName, String name) {
		this.scopeName = scopeName;
		this.name = name;
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
	
	

}
