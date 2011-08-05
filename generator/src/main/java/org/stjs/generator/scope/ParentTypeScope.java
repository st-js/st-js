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

import static japa.parser.ast.body.ModifierSet.isStatic;
import static org.stjs.generator.handlers.utils.Lists.getOnlyElement;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;
import org.stjs.generator.scope.QualifiedName.NameTypes;

/**
 * This scope is for fields and methods inherited from a parent type.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ParentTypeScope extends NameScope {
	private Class<?> parentClass;
	private final String parentClassName;

	public ParentTypeScope(File inputFile, NameScope parent, String parentClassName) {
		super(inputFile, "parent-" + parentClassName, parent);
		this.parentClassName = parentClassName;
	}

	private ImportScope getImportScope(SourcePosition pos) {
		NameScope s = getParent();
		while (s != null) {
			if (s instanceof ImportScope) {
				return ((ImportScope) s);
			}
			s = s.getParent();
		}
		throw new JavascriptGenerationException(getInputFile(), pos,
				"No import scope found in the hierarchy of parent scope");
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope) {
		if (parentClass == null) {
			parentClass = getImportScope(pos).resolveClass(pos, parentClassName);
			if (parentClass == null){
        throw new JavascriptGenerationException(getInputFile(), pos, "Cannot load class:" + parentClassName);
      }
		}
		
		String declaredClassName = getDeclaredTypeScope().getDeclaredTypeName().getFullName(false).getOrNull();
		Method method = getAccesibleMethod(parentClass, name);
		if (method != null) {
			boolean isStatic = isStatic(method.getModifiers());
		  if (TypeScope.isInCurrentTypeScope(this, currentScope)) {
				return new QualifiedName<MethodName>(this, isStatic, NameTypes.METHOD, getDeclaredTypeScope().getDeclaredTypeName());
			}
			return QualifiedName.<MethodName>outerScope(this, isStatic, NameTypes.METHOD, getDeclaredTypeScope().getDeclaredTypeName());
		}
		if (getParent() != null) {
			return getParent().resolveMethod(pos, name, currentScope);
		}
		return null;
	}

	/**
	 * looks for a method with a given name (no full method signature given) in the given class the can be safely
	 * accessed from a child class
	 * 
	 * @param parentClass2
	 * @param name
	 * @return
	 */
	private Method getAccesibleMethod(Class<?> parentClass, String name) {
		for (Method m : parentClass.getDeclaredMethods()) {
		  // TODO : this works only under the assumption that method names are unique (which is not true in java)
		  // Is that assumption checked before calling this method?
			if (m.getName().equals(name) && !Modifier.isPrivate(m.getModifiers())) {
				return m;
			}
		}
		if (parentClass.getSuperclass() != null) {
			return getAccesibleMethod(parentClass.getSuperclass(), name);
		}
		return null;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope) {
		if (parentClass == null) {
			parentClass = getImportScope(pos).resolveClass(pos, parentClassName);
			if (parentClass == null){
			  throw new JavascriptGenerationException(getInputFile(), pos, "Cannot load class:" + parentClassName);
			}
		}
		Field field = getAccesibleField(parentClass, name);
		if (field != null) {
		  boolean isStatic = isStatic(field.getModifiers());
			if (TypeScope.isInCurrentTypeScope(this, currentScope)) {
				return new QualifiedName<IdentifierName>(this, isStatic, NameTypes.FIELD, getDeclaredTypeScope().getDeclaredTypeName());
			}
			return QualifiedName.<IdentifierName>outerScope(this, isStatic, NameTypes.FIELD, getDeclaredTypeScope().getDeclaredTypeName());
		}
		if (getParent() != null) {
			return getParent().resolveIdentifier(pos, name, currentScope);
		}
		return null;
	}

	/**
	 * looks for a method with a given name (no full method signature given) in the given class the can be safely
	 * accessed from a child class
	 * 
	 * @param parentClass2
	 * @param name
	 * @return
	 */
	private Field getAccesibleField(Class<?> parentClass, String name) {
		try {
			Field f = parentClass.getDeclaredField(name);
			if (!Modifier.isPrivate(f.getModifiers())) {
				return f;
			}
		} catch (Exception e) {
			// do nothing - search further on
		}

		if (parentClass.getSuperclass() != null) {
			return getAccesibleField(parentClass.getSuperclass(), name);
		}
		return null;
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
		return "ParentTypeScope [className=" + parentClass.getName() + ", getChildren()=" + getChildren() + "]";
	}

  @Override
  public <T> T visit(NameScopeVisitor<T> visitor) {
    return visitor.caseParentTypeScope(this);
  }
  
  @Override
  public void visit(VoidNameScopeVisitor visitor) {
    visitor.caseParentTypeScope(this);
  }

  public TypeScope getDeclaredTypeScope() {
   return (TypeScope) getOnlyElement(getChildren());
  }
}
