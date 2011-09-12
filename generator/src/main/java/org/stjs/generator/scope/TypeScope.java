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

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;
import org.stjs.generator.scope.QualifiedName.NameTypes;
import org.stjs.generator.scope.path.QualifiedPath;

/**
 * This scope is for a class definition. It contains the name of the fields and methods
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class TypeScope extends NameScope {
	private final Set<String> staticFields = new HashSet<String>();
	private final Set<String> staticMethods = new HashSet<String>();
	private final Set<String> staticInnerTypes = new HashSet<String>();
	private final Set<String> instanceFields = new HashSet<String>();
	private final Set<String> instanceMethods = new HashSet<String>();
	private final Set<String> instanceInnerTypes = new HashSet<String>();
	private final Set<String> typeParameters = new HashSet<String>();

	private final JavaTypeName declaredTypeName;

	public TypeScope(File inputFile, String name, JavaTypeName declaredTypeName, NameScope parent) {
		super(inputFile, name, parent);
		this.declaredTypeName = declaredTypeName;
	}

	/**
	 * 
	 * @param scope
	 * @return true if the given scope is in the current TypeScope, and false if is in some outer type
	 */
	public static boolean isInCurrentTypeScope(NameScope currentScope, NameScope scope) {
		if (scope == null) {
			return true;
		}
		if (scope == currentScope) {
			return true;
		}
		if (scope instanceof TypeScope) {
			if (currentScope instanceof ParentTypeScope && scope.getParent() == currentScope) {
				// this is the case for calls from a type to its parent type
				return true;
			}
			return false;
		}
		if (scope.getParent() == null) {
			return false;
		}
		return isInCurrentTypeScope(currentScope, scope.getParent());
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String fullName, NameScope currentScope,
			NameResolverVisitor visitor) {
		String name = QualifiedPath.afterLastDot(fullName);
		String scopePath = QualifiedPath.beforeLastDot(fullName);

		// if (SUPER.equals(scopePath) && isInCurrentTypeScope(this, currentScope)) {// go directly to the parent
		if (scopePath != null && !scopePath.isEmpty()) {
			if (getParent() != null) {
				return getParent().resolveMethod(pos, GeneratorConstants.SUPER.equals(scopePath) ? name : fullName,
						currentScope, visitor);
			}
			return null;
		}

		if (instanceMethods.contains(name)) {
			if (isInCurrentTypeScope(this, currentScope)) {
				return new QualifiedName<MethodName>(this, false, NameTypes.METHOD, getDeclaredTypeName());
			}
			return QualifiedName.<MethodName> outerScope(this, false, NameTypes.METHOD, getDeclaredTypeName());
		}
		if (staticMethods.contains(name)) {
			if (isInCurrentTypeScope(this, currentScope)) {
				return new QualifiedName<MethodName>(this, true, NameTypes.METHOD, getDeclaredTypeName());
			}
			return QualifiedName.<MethodName> outerScope(this, true, NameTypes.METHOD, getDeclaredTypeName());
		}
		if (getParent() != null) {
			return getParent().resolveMethod(pos, fullName, currentScope, visitor);
		}
		return null;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String fullName,
			NameScope currentScope, NameResolverVisitor visitor) {
		boolean accessingOuterScope = !isInCurrentTypeScope(this, currentScope);
		String name = QualifiedPath.afterLastDot(fullName);
		String scopePath = QualifiedPath.beforeLastDot(fullName);

		// if (SUPER.equals(scopePath) && isInCurrentTypeScope(this, currentScope)) {// go directly to the parent
		if (scopePath != null && !scopePath.isEmpty()) {
			if (getParent() != null) {
				return getParent().resolveIdentifier(pos, GeneratorConstants.SUPER.equals(scopePath) ? name : fullName,
						currentScope, visitor);
			}
			return null;
		}
		NameTypes type = null;
		boolean isStatic;

		if (instanceFields.contains(name)) {
			type = NameTypes.FIELD;
			isStatic = false;
		} else if (staticFields.contains(name)) {
			type = NameTypes.FIELD;
			isStatic = true;
		} else if (instanceInnerTypes.contains(name)) {
			// TODO It is not correct to return and Identifier Name, since this is a class.
			// but we need to differentiate nameExpressions on classes and variables then
			type = NameTypes.CLASS;
			isStatic = false;
		} else if (staticInnerTypes.contains(name)) {
			// TODO It is not correct to return and Identifier Name, since this is a class.
			// but we need to differentiate nameExpressions on classes and variables then
			type = NameTypes.CLASS;
			isStatic = true;
		} else {
			if (getParent() != null) {
				return getParent().resolveIdentifier(pos, name, currentScope, visitor);
			}
			return null;
		}
		return new QualifiedName<IdentifierName>(this, isStatic, accessingOuterScope, type, getDeclaredTypeName());
	}

	public void addStaticField(String name, SourcePosition sourcePosition) {
		staticFields.add(name);
	}

	public void addStaticMethod(String name, SourcePosition sourcePosition) {
		if (!staticMethods.add(name)) {
			throw new JavascriptGenerationException(getInputFile(), sourcePosition,
					"The type contains already a method called [" + name
							+ "] with a different signature. Javascript cannot distinguish methods with the same name");
		}
	}

	public void addInstanceField(String name, SourcePosition sourcePosition) {
		instanceFields.add(name);
	}

	public void addInstanceMethod(String name, SourcePosition sourcePosition) {
		if (!instanceMethods.add(name)) {
			throw new JavascriptGenerationException(getInputFile(), sourcePosition,
					"The type contains already a method called [" + name
							+ "] with a different signature. Javascript cannot distinguish methods with the same name");
		}
	}

	public void addStaticInnerType(String name) {
		staticInnerTypes.add(name);
	}

	public void addInstanceInnerType(String name) {
		instanceInnerTypes.add(name);
	}

	public void addTypeParameter(String typeParameter) {
		typeParameters.add(typeParameter);
	}

	@Override
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope,
			NameResolverVisitor visitor) {
		// TODO : do not check strings, but qualified names. Becaue OuterClass.InnerClass is === to InnerClass
		if (staticInnerTypes.contains(name)) {
			return createInnerTypeQualifiedName(pos, name, true);
		}
		if (instanceInnerTypes.contains(name)) {
			return createInnerTypeQualifiedName(pos, name, false);
		}
		if (typeParameters.contains(name)) {
			return new QualifiedName<TypeName>(this, false, NameTypes.GENERIC_TYPE);
		}
		if (getParent() != null) {
			return getParent().resolveType(pos, name, currentScope, visitor);
		}
		return null;
	}

	private QualifiedName<TypeName> createInnerTypeQualifiedName(SourcePosition pos, String name, boolean isStatic) {
		return new QualifiedName<TypeName>(this, isStatic, NameTypes.INNER_CLASS, declaredTypeName);
	}

	@Override
	public String toString() {
		return "TypeScope [staticFields=" + staticFields + ", staticMethods=" + staticMethods + ", instanceFields="
				+ instanceFields + ", instanceMethods=" + instanceMethods + ", getChildren()=" + getChildren() + "]";
	}

	public JavaTypeName getDeclaredTypeName() {
		return declaredTypeName;
	}

	@Override
	public <T> T visit(NameScopeVisitor<T> visitor) {
		return visitor.caseTypeScope(this);
	}

	@Override
	public void visit(VoidNameScopeVisitor visitor) {
		visitor.caseTypeScope(this);
	}

}
