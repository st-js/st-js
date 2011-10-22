package org.stjs.generator.scope.classloader;

import org.stjs.generator.scope.simple.Variable;

/**
 * This class wrapps a class field to use the type wrappers.
 * 
 * @author acraciun
 * 
 */
public class FieldWrapper implements Variable {
	private final String name;
	private final TypeWrapper type;
	private final TypeWrapper ownerType;
	private final int modifiers;
	private final boolean declared;

	public FieldWrapper(String name, TypeWrapper type, int modifiers, TypeWrapper ownerType, boolean declared) {
		this.name = name;
		this.type = type;
		this.ownerType = ownerType;
		this.modifiers = modifiers;
		this.declared = declared;
	}

	public String getName() {
		return name;
	}

	public TypeWrapper getType() {
		return type;
	}

	public TypeWrapper getOwnerType() {
		return ownerType;
	}

	public int getModifiers() {
		return modifiers;
	}

	/**
	 * 
	 * @return true if the field was declared in the owner class, false if it was inherited
	 */
	public boolean isDeclared() {
		return declared;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((ownerType == null) ? 0 : ownerType.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FieldWrapper other = (FieldWrapper) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (ownerType == null) {
			if (other.ownerType != null) {
				return false;
			}
		} else if (!ownerType.equals(other.ownerType)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return name + ":" + type;
	}

}
