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
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.SourcePosition;
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

	private final Set<String> staticFields = new HashSet<String>();
	private final Set<String> staticMethods = new HashSet<String>();
	private final Set<String> staticInnerTypes = new HashSet<String>();
	private final Set<String> instanceFields = new HashSet<String>();
	private final Set<String> instanceMethods = new HashSet<String>();
	private final Set<String> instanceInnerTypes = new HashSet<String>();
	
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
	  String declaringClassName = declaredTypeName.getFullyQualifiedString().getOrNull();
		if (instanceMethods.contains(name)) {
			if (isInCurrentTypeScope(this, currentScope)) {
        return new QualifiedName<MethodName>(declaringClassName, name, this, false);
			}
			return QualifiedName.<MethodName>outerScope(declaringClassName, name, this, false);
		}
		if (staticMethods.contains(name)) {
			 if (isInCurrentTypeScope(this, currentScope)) {
        return new QualifiedName<MethodName>(declaringClassName, name, this, true);
      }
      return QualifiedName.<MethodName>outerScope(declaringClassName, name, this, true);
		}
		if (getParent() != null) {
			return getParent().resolveMethod(pos, name, currentScope);
		}
		return null;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope) {
	  // TODO avoid duplicated code with resolveMethod
	  String declaringClassName = declaredTypeName.getFullyQualifiedString().getOrNull();
	  if (instanceFields.contains(name)) {
			if (isInCurrentTypeScope(this, currentScope)) {
				return new QualifiedName<IdentifierName>(declaringClassName, name, this, false);
			}
			return QualifiedName.<IdentifierName>outerScope(declaringClassName, name, this, false);
		}
		if (staticFields.contains(name)) {
		  if (isInCurrentTypeScope(this, currentScope)) {
        return new QualifiedName<IdentifierName>(declaringClassName, name, this, true);
      }
      return QualifiedName.<IdentifierName>outerScope(declaringClassName, name, this, true);
		}
		
// TODO : there might be a collision between field names and inner types names. I don't
// understand why we resolve types as identifiers?
		
		//		if (instanceInnerTypes.contains(name)) {
//		  return new QualifiedName<IdentifierName>(TYPE_SCOPE, name, this, true);
//		}
//		if (staticInnerTypes.contains(name)) {
//			return new QualifiedName<IdentifierName>(TYPE_SCOPE, name, this, true);
//		}
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

  public void addstaticInnerType(String name) {
    staticInnerTypes.add(name);
  }

  public void addInstanceInnerType(String name) {
    instanceInnerTypes.add(name);
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

  @Override
  public <T> T visit(NameScopeVisitor<T> visitor) {
    return visitor.caseTypeScope(this);
  }
  
  @Override
  public void visit(VoidNameScopeVisitor visitor) {
    visitor.caseTypeScope(this);
  }

}
