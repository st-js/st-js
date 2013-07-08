/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator.type;

import org.stjs.generator.variable.Variable;

/**
 * This class wrapps a class field to use the type wrappers.
 * 
 * @author acraciun
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

	@Override
	public String getName() {
		return name;
	}

	@Override
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
	 * @return true if the field was declared in the owner class, false if it was inherited
	 */
	public boolean isDeclared() {
		return declared;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (ownerType == null ? 0 : ownerType.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.NPathComplexity" })
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
