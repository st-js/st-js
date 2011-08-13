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
import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;
import org.stjs.generator.scope.QualifiedName.NameTypes;

/**
 * This scope is for the a method's parameters.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ParameterScope extends NameScope {

	private final Set<String> parameters = new HashSet<String>();
	private final Set<String> typeParameters = new HashSet<String>();

	public ParameterScope(File inputFile, String name, NameScope parent) {
		super(inputFile, name, parent);
	}

	public ParameterScope(File inputFile, String name, NameScope parent, String parameter) {
		this(inputFile, name, parent);
		parameters.add(parameter);
	}

  public void addParameter(String parameter) {
    parameters.add(parameter);
  }

  public void addTypeParameter(String typeParameter) {
    typeParameters.add(typeParameter);
  }

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope) {
		if (parameters.contains(name)) {
			return new QualifiedName<IdentifierName>(this, false, NameTypes.VARIABLE);
		}
		if (getParent() != null) {
			return getParent().resolveIdentifier(pos, name, currentScope);
		}
		return null;
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope) {

		if (getParent() != null) {
			return getParent().resolveMethod(pos, name, currentScope);
		}
		return null;
	}

	@Override
	public String toString() {
		return "ParameterScope [parameters=" + parameters + ", getChildren()=" + getChildren() + "]";
	}

	@Override
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope) {
		if (typeParameters.contains(name)) {
		  return new QualifiedName<TypeName>(this, false, NameTypes.CLASS);
		}
	  if (getParent() != null) {
			return getParent().resolveType(pos, name, currentScope);
		}
		return null;
	}

  @Override
  public <T> T visit(NameScopeVisitor<T> visitor) {
    return visitor.caseParameterScope(this);
  }
  
  @Override
  public void visit(VoidNameScopeVisitor visitor) {
    visitor.caseParameterScope(this);
  }

}
