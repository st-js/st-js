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
package org.stjs.generator.scope;

import static org.stjs.generator.utils.PreConditions.checkNotNull;
import static org.stjs.generator.utils.PreConditions.checkState;

import org.stjs.generator.utils.Option;

public class QualifiedName<T extends NameType> {
	private final NameScope scope;
	private final boolean isStatic; // use an int to encode modifiers a la java.lang.reflect?
	private final boolean accessingOuterScope;
	private final NameTypes type;
	private final Option<JavaTypeName> definitionPoint; // Local variables and parameters don't have definition points
	private final boolean isGlobalScope;
	private final boolean isMockType;
	private final boolean isDataType;

	public enum NameTypes {
		METHOD, FIELD, VARIABLE, CLASS, GENERIC_TYPE, INNER_CLASS
	}

	public QualifiedName(NameScope scope, boolean isStatic, NameTypes type) {
		this(scope, isStatic, type, null);
	}

	public QualifiedName(NameScope scope, boolean isStatic, NameTypes type, JavaTypeName definitionPoint) {
		this(scope, isStatic, false, type, definitionPoint);
	}

	public QualifiedName(NameScope scope, boolean isStatic, boolean accessingOuterScope, NameTypes type,
			JavaTypeName definitionPoint) {
		this(scope, isStatic, accessingOuterScope, type, definitionPoint, false, false, false);
	}

	public QualifiedName(NameScope scope, boolean isStatic, boolean accessingOuterScope, NameTypes type,
			JavaTypeName definitionPoint, boolean isGlobalScope, boolean isMockType, boolean isDataType) {
		this.scope = checkNotNull(scope);
		this.isStatic = isStatic;
		this.accessingOuterScope = accessingOuterScope;
		this.type = type;
		this.definitionPoint = Option.of(definitionPoint);
		this.isGlobalScope = isGlobalScope;
		this.isMockType = isMockType;
		this.isDataType = isDataType;
		if (!this.definitionPoint.isDefined()) {
			// variable may or may not be resolved
			checkState((type == NameTypes.VARIABLE) || (type == NameTypes.GENERIC_TYPE),
					"Methods, fields and classes must have a definition point");
		}

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
		if (definitionPoint.isDefined()) {
			Option<String> fullyQualifiedString = definitionPoint.getOrThrow().getFullName(true);
			if (fullyQualifiedString.isDefined()) {
				s.append(fullyQualifiedString.getOrThrow()).append(".");
			}
		}
		s.append("[" + scope.getPath() + "]");
		return s.toString();
	}

	public boolean isStatic() {
		return isStatic;
	}

	public boolean isAccessingOuterScope() {
		return accessingOuterScope;
	}

	public static <T extends NameType> QualifiedName<T> outerScope(NameScope scope, boolean isStatic, NameTypes type,
			JavaTypeName definitionPoint) {
		return new QualifiedName<T>(scope, isStatic, true, type, definitionPoint);
	}

	public NameTypes getType() {
		return type;
	}

	/**
	 * Indicates that a variable, field or method can be accessed through the global scope
	 * 
	 * @return
	 */
	public boolean isGlobal() {
		return isGlobalScope;
	}

	public Option<JavaTypeName> getDefinitionPoint() {
		return definitionPoint;
	}

	public boolean isMockType() {
		return isMockType;
	}

	public boolean isDataType() {
		return isDataType;
	}

	public boolean isInnerClass() {
		return NameTypes.INNER_CLASS == type;
	}

}
