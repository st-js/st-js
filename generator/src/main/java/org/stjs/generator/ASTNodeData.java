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
import java.lang.reflect.Type;

import org.stjs.generator.scope.simple.Scope;

public class ASTNodeData {
	private Scope scope;
	private Node parent;
	/**
	 * this is the type of the current node - if it an expression
	 */
	private Type expressionType;

	/**
	 * this is the method resolved only for a methodCall
	 */
	private Method method;

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

	public Type getExpressionType() {
		return expressionType;
	}

	public void setExpressionType(Type expressionType) {
		this.expressionType = expressionType;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public static Scope scope(Node n) {
		return ((ASTNodeData) n.getData()).getScope();
	}

	public static Node parent(Node n) {
		return ((ASTNodeData) n.getData()).getParent();
	}

	public static Method method(Node n) {
		return ((ASTNodeData) n.getData()).getMethod();
	}

	public static java.lang.reflect.Type expressionType(Node n) {
		return ((ASTNodeData) n.getData()).getExpressionType();
	}

	public static void expressionType(Node n, java.lang.reflect.Type type) {
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
