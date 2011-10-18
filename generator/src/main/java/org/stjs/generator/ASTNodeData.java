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
package org.stjs.generator;

import japa.parser.ast.Node;

import java.lang.reflect.Method;

import org.stjs.generator.scope.classloader.TypeWrapper;
import org.stjs.generator.scope.simple.Scope;
import org.stjs.generator.scope.simple.Variable;

public class ASTNodeData {
	private Scope scope;
	private Node parent;
	/**
	 * this is the type of the current node - if it an expression
	 */
	private TypeWrapper expressionType;

	/**
	 * this is the method resolved only for a methodCall
	 */
	private Method resolvedMethod;

	private Variable resolvedVariable;

	private TypeWrapper resolvedType;

	public ASTNodeData() {

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

	public TypeWrapper getExpressionType() {
		return expressionType;
	}

	public void setExpressionType(TypeWrapper expressionType) {
		this.expressionType = expressionType;
	}

	public Method getResolvedMethod() {
		return resolvedMethod;
	}

	public void setResolvedMethod(Method resolvedMethod) {
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

	public static Method resolvedMethod(Node n) {
		return ((ASTNodeData) n.getData()).getResolvedMethod();
	}

	public static void resolvedMethod(Node n, Method m) {
		((ASTNodeData) n.getData()).setResolvedMethod(m);
	}

	public static Variable resolvedVariable(Node n) {
		return ((ASTNodeData) n.getData()).getResolvedVariable();
	}

	public static void resolvedVariable(Node n, Variable v) {
		((ASTNodeData) n.getData()).setResolvedVariable(v);
	}

	public static TypeWrapper resolvedType(Node n) {
		return ((ASTNodeData) n.getData()).getResolvedType();
	}

	public static void resolvedType(Node n, TypeWrapper t) {
		((ASTNodeData) n.getData()).setResolvedType(t);
	}

	public static TypeWrapper expressionType(Node n) {
		return ((ASTNodeData) n.getData()).getExpressionType();
	}

	public static void expressionType(Node n, TypeWrapper type) {
		((ASTNodeData) n.getData()).setExpressionType(type);
	}

	public static Node parent(Node n, int upLevel) {
		Node p = n;
		for (int i = 0; (i < upLevel) && (p != null); ++i) {
			p = parent(p);
		}
		return p;
	}

	public static Node checkParent(Node n, Class<?> clazz) {
		Node parent = parent(n);
		if (parent == null) {
			return null;
		}
		return (clazz.isAssignableFrom(parent.getClass())) ? parent : null;
	}

}
