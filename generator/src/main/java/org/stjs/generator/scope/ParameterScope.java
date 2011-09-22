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

	private final Map<String, Type> parameters = new HashMap<String, Type>();
	private final Set<String> typeParameters = new HashSet<String>();

	public ParameterScope(File inputFile, String name, NameScope parent) {
		super(inputFile, name, parent);
	}

	public ParameterScope(File inputFile, String name, NameScope parent, String parameter, Type type) {
		this(inputFile, name, parent);
		parameters.put(parameter, type);
	}

	public void addParameter(String parameter, Type type) {
		parameters.put(parameter, type);
	}

	public void addTypeParameter(String typeParameter) {
		typeParameters.add(typeParameter);
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope,
			NameResolverVisitor visitor) {
		if (parameters.containsKey(name)) {
			// XXX: ACRACIUN: copied from the VariableScope
			Type type = parameters.get(name);
			QualifiedName<TypeName> resolvedType = super.resolveType(pos, type.toString(), visitor);
			if (resolvedType != null) {
				// TODO : this should be done in other scopes too (for parameters, fields, ....)
				// this is not at all specific to variables
				if ((resolvedType.getType() == NameTypes.INNER_CLASS) && resolvedType.getDefinitionPoint().isDefined()) {

					// TODO: Something is broken in the data type here.
					// we're using the definition point of the member to build a fake definition point...
					// since the type has already been resolved, the following code is pointless.
					// the QualifiedName itself should be smart enough
					// JavaTypeName should really be deleted.
					for (JavaTypeName enclosingNameType : resolvedType.getDefinitionPoint()) {
						QualifiedPath qualifiedPath = enclosingNameType.getClassPath().getOrThrow();
						JavaTypeName javaTypeName = new JavaTypeName(QualifiedPath.withClassName(type.toString()),
								qualifiedPath);
						return new QualifiedName<IdentifierName>(this, false, NameTypes.VARIABLE, javaTypeName);
					}
				}
				return new QualifiedName<IdentifierName>(this, false, NameTypes.VARIABLE, resolvedType
						.getDefinitionPoint().getOrNull());
			}
			return new QualifiedName<IdentifierName>(this, false, NameTypes.VARIABLE);
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
		return "ParameterScope [parameters=" + parameters + ", getChildren()=" + getChildren() + "]";
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
