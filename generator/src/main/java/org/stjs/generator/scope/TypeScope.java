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

import static org.stjs.generator.handlers.utils.Sets.transform;
import static org.stjs.generator.handlers.utils.Sets.union;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.handlers.utils.Function;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;

/**
 * This scope is for a class definition. It contains the name of the fields and methods
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class TypeScope extends NameScope {

	public static final String THIS_SCOPE = "this";

	public static final String OUTER_SCOPE = "outer";

	public static final String STATIC_SCOPE = "static";

	private static final String TYPE_SCOPE = null;

	private final Set<String> staticFields = new HashSet<String>();
	private final Set<String> staticMethods = new HashSet<String>();
	private final Set<String> instanceFields = new HashSet<String>();
	private final Set<String> instanceMethods = new HashSet<String>();
	private final Set<String> innerTypes = new HashSet<String>();
	
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
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope) {
		if (instanceMethods.contains(name)) {
			if (isInCurrentTypeScope(this, currentScope)) {
				return new QualifiedName<MethodName>(THIS_SCOPE, name, this);
			}
			return new QualifiedName<MethodName>(OUTER_SCOPE, name, this);
		}
		if (staticMethods.contains(name)) {
			return new QualifiedName<MethodName>(STATIC_SCOPE, name, this);
		}
		if (getParent() != null) {
			return getParent().resolveMethod(pos, name, currentScope);
		}
		return null;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope) {
		if (instanceFields.contains(name)) {
			if (isInCurrentTypeScope(this, currentScope)) {
				return new QualifiedName<IdentifierName>(THIS_SCOPE, name, this);
			}
			return new QualifiedName<IdentifierName>(OUTER_SCOPE, name, this);
		}
		if (staticFields.contains(name)) {
			return new QualifiedName<IdentifierName>(STATIC_SCOPE, name, this);
		}
		if (innerTypes.contains(name)) {
			return new QualifiedName<IdentifierName>(TYPE_SCOPE, name, this);
		}
		if (getParent() != null) {
			return getParent().resolveIdentifier(pos, name, currentScope);
		}
		return null;
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

	public void addInnerType(String name) {
		innerTypes.add(name);
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
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope) {
		if (getParent() != null) {
			return getParent().resolveType(pos, name, currentScope);
		}
		return null;
	}

	@Override
	public String toString() {
		return "TypeScope [staticFields=" + staticFields + ", staticMethods=" + staticMethods + ", instanceFields="
				+ instanceFields + ", instanceMethods=" + instanceMethods + ", getChildren()=" + getChildren() + "]";
	}

  public JavaTypeName getDeclaredTypeName() {
    return declaredTypeName;
  }

}
