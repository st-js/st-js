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
import java.util.Map;

import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;
import org.stjs.generator.scope.QualifiedName.NameTypes;
import org.stjs.generator.scope.path.QualifiedPath;

/**
 * This scope is for the variables defined within a block
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class VariableScope extends NameScope {

	private final Map<String, Type> variables = new HashMap<String, Type>();

	public VariableScope(File inputFile, String name, NameScope parent) {
		super(inputFile, name, parent);
	}

	public void addVariable(Type type, String var) {
		variables.put(var, type);
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope, NameResolverVisitor visitor) {
		if (variables.containsKey(name)) {
			Type type = variables.get(name);
			QualifiedName<TypeName> resolvedType = super.resolveType(pos, type.toString(), visitor);
			if (resolvedType != null) {
				// TODO : this should be done in other scopes too (for parameters, fields, ....)
				// this is not at all specific to variables
				if (resolvedType.getType() == NameTypes.INNER_CLASS && resolvedType.getDefinitionPoint().isDefined()) {
					
					// TODO: Something is broken in the data type here.
					// we're using the definition point of the member to build a fake definition point...
					// since the type has already been resolved, the following code is pointless.
					// the QualifiedName itself should be smart enough
					// JavaTypeName should really be deleted.
					for (JavaTypeName enclosingNameType : resolvedType.getDefinitionPoint()) {
						QualifiedPath qualifiedPath = enclosingNameType.getClassPath().getOrThrow();
						JavaTypeName javaTypeName = new JavaTypeName(QualifiedPath.withClassName(type.toString()), qualifiedPath);
						return new QualifiedName<IdentifierName>(this, false, NameTypes.VARIABLE, javaTypeName);
					}
				} else {
					return new QualifiedName<IdentifierName>(this, false, NameTypes.VARIABLE, resolvedType.getDefinitionPoint().getOrNull());
				}
			}
			return new QualifiedName<IdentifierName>(this, false, NameTypes.VARIABLE);
		}
		if (getParent() != null) {
			return getParent().resolveIdentifier(pos, name, currentScope, visitor);
		}
		return null;
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope, NameResolverVisitor visitor) {
		if (name.contains(".")) {
			String receiverOrStaticClass = QualifiedPath.beforeFirstDot(name);
			// it is ok to check even without knowing if the receiver is a class or a variable
			// since variables have precedence if there is a collision
			if (variables.containsKey(receiverOrStaticClass)) {
				return new QualifiedName<NameType.MethodName>(this, false, NameTypes.VARIABLE);
			}
		}
		if (getParent() != null) {
			return getParent().resolveMethod(pos, name, currentScope, visitor);
		}
		return null;
	}

	@Override
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope, NameResolverVisitor visitor) {
		if (getParent() != null) {
			return getParent().resolveType(pos, name, currentScope, visitor);
		}
		return null;
	}

	@Override
	public String toString() {
		return "VariableScope [variables=" + variables + ", getChildren()=" + getChildren() + "]";
	}

	@Override
	public <T> T visit(NameScopeVisitor<T> visitor) {
		return visitor.caseVariableScope(this);
	}

	@Override
	public void visit(VoidNameScopeVisitor visitor) {
		visitor.caseVariableScope(this);
	}
}
