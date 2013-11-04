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
package org.stjs.generator.ast;

import japa.parser.ast.Node;

import org.stjs.generator.scope.Scope;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.variable.Variable;

/**
 * this is the data associated to a node in the AST
 * 
 * @author acraciun
 */
public class ASTNodeData {
	private Scope scope;
	private Node parent;
	/**
	 * this is the type of the current node - if it an expression, or the associated type if it's a type node
	 */
	private TypeWrapper resolvedType;

	/**
	 * this is the method resolved only for a methodCall. If the node is a ConstructorDeclaration, this method
	 * corresponds to a constructor
	 */
	private MethodWrapper resolvedMethod;

	private Variable resolvedVariable;

	/**
	 * the scope where the variable was resolved
	 */
	private Scope resolvedVariableScope;

	public ASTNodeData() {
		//
	}

	public ASTNodeData(Node parent) {
		this.parent = parent;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public MethodWrapper getResolvedMethod() {
		return resolvedMethod;
	}

	public void setResolvedMethod(MethodWrapper resolvedMethod) {
		this.resolvedMethod = resolvedMethod;
	}

	public Variable getResolvedVariable() {
		return resolvedVariable;
	}

	public void setResolvedVariable(Variable resolvedVariable) {
		this.resolvedVariable = resolvedVariable;
	}

	public TypeWrapper getResolvedType() {
		return resolvedType;
	}

	public void setResolvedType(TypeWrapper resolvedType) {
		this.resolvedType = resolvedType;
	}

	public Scope getResolvedVariableScope() {
		return resolvedVariableScope;
	}

	public void setResolvedVariableScope(Scope resolvedVariableScope) {
		this.resolvedVariableScope = resolvedVariableScope;
	}

	public static Scope scope(Node n) {
		return ((ASTNodeData) n.getData()).getScope();
	}

	public static void scope(Node n, Scope s) {
		((ASTNodeData) n.getData()).setScope(s);
	}

	public static Node parent(Node n) {
		return ((ASTNodeData) n.getData()).getParent();
	}

	public static void parent(Node n, Node p) {
		((ASTNodeData) n.getData()).setParent(p);
	}

	public static MethodWrapper resolvedMethod(Node n) {
		return ((ASTNodeData) n.getData()).getResolvedMethod();
	}

	public static void resolvedMethod(Node n, MethodWrapper m) {
		((ASTNodeData) n.getData()).setResolvedMethod(m);
	}

	public static Variable resolvedVariable(Node n) {
		return ((ASTNodeData) n.getData()).getResolvedVariable();
	}

	public static void resolvedVariable(Node n, Variable v) {
		((ASTNodeData) n.getData()).setResolvedVariable(v);
	}

	public static Scope resolvedVariableScope(Node n) {
		return ((ASTNodeData) n.getData()).getResolvedVariableScope();
	}

	public static void resolvedVariableScope(Node n, Scope v) {
		((ASTNodeData) n.getData()).setResolvedVariableScope(v);
	}

	public static TypeWrapper resolvedType(Node n) {
		return ((ASTNodeData) n.getData()).getResolvedType();
	}

	public static void resolvedType(Node n, TypeWrapper t) {
		((ASTNodeData) n.getData()).setResolvedType(t);
	}

	public static Node parent(Node n, int upLevel) {
		Node p = n;
		for (int i = 0; (i < upLevel) && (p != null); ++i) {
			p = parent(p);
		}
		return p;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Node> T parent(Node n, Class<T> clazz) {
		for (Node p = n; p != null; p = parent(p)) {
			if (clazz.isAssignableFrom(p.getClass())) {
				return (T) p;
			}
		}
		return null;
	}

	public static Node parent(Node n, Class<? extends Node>... clazz) {
		for (Node p = n; p != null; p = parent(p)) {
			for (Class<? extends Node> c : clazz) {
				if (c.isAssignableFrom(p.getClass())) {
					return p;
				}
			}
		}
		return null;
	}

	public static Node checkParent(Node n, Class<?> clazz) {
		Node parent = parent(n);
		if (parent == null) {
			return null;
		}
		return (clazz.isAssignableFrom(parent.getClass())) ? parent : null;
	}

}
