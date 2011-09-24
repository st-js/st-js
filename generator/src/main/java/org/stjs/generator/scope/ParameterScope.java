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

import japa.parser.ast.type.Type;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;
import org.stjs.generator.scope.QualifiedName.NameTypes;
import org.stjs.generator.scope.path.QualifiedPath;

/**
 * This scope is for the a method's parameters.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class ParameterScope extends NameScope {

	private final VariableDeclarationRegistry registry;
	private final Set<String> typeParameters = new HashSet<String>();

	public ParameterScope(File inputFile, String name, NameScope parent) {
		super(inputFile, name, parent);
		registry = new VariableDeclarationRegistry(this);
	}

	public ParameterScope(File inputFile, String name, NameScope parent, String parameter, Type type) {
		this(inputFile, name, parent);
		registry.addVariable(type, parameter);
	}

	public void addParameter(String parameter, Type type) {
		registry.addVariable(type, parameter);
	}

	public void addTypeParameter(String typeParameter) {
		typeParameters.add(typeParameter);
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope,
			NameResolverVisitor visitor) {
		for (QualifiedName<IdentifierName> qName : registry.resolveIdentifier(name)) {
			return qName;
		}
		if (getParent() != null) {
			return getParent().resolveIdentifier(pos, name, currentScope, visitor);
		}
		return null;
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope,
			NameResolverVisitor visitor) {

		if (getParent() != null) {
			return getParent().resolveMethod(pos, name, currentScope, visitor);
		}
		return null;
	}

	@Override
	public String toString() {
		return "ParameterScope [parameters=" + registry.getVariables() + ", getChildren()=" + getChildren() + "]";
	}

	@Override
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope,
			NameResolverVisitor visitor) {
		if (typeParameters.contains(name)) {
			return new QualifiedName<TypeName>(this, false, NameTypes.GENERIC_TYPE);
		}
		if (getParent() != null) {
			return getParent().resolveType(pos, name, currentScope, visitor);
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
