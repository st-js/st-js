/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.stjs.generator.javascript.rhino;

import org.mozilla.javascript.Node;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * This is a collection of statements. Its like a block without the curly braces
 * 
 * <pre>
 * <i>Statements</i> :
 *     Statement*
 * </pre>
 */
class Statements extends AstNode {

	/**
	 * <p>Constructor for Statements.</p>
	 */
	public Statements() {
		this(0, 0);
	}

	/**
	 * <p>Constructor for Statements.</p>
	 *
	 * @param pos a int.
	 */
	public Statements(int pos) {
		this(pos, 0);
	}

	/**
	 * <p>Constructor for Statements.</p>
	 *
	 * @param pos a int.
	 * @param len a int.
	 */
	public Statements(int pos, int len) {
		super(pos, len);
		this.type = Token.LAST_TOKEN + 1;
	}

	/**
	 * Alias for {@link #addChild}.
	 *
	 * @param statement a {@link org.mozilla.javascript.ast.AstNode} object.
	 */
	public void addStatement(AstNode statement) {
		addChild(statement);
	}

	@java.lang.SuppressWarnings("unused")
	@SuppressWarnings(
			justification = "No problem with this cast", value = "BC_UNCONFIRMED_CAST")
	private AstNode cast(Node n) {
		return (AstNode) n;
	}

	/** {@inheritDoc} */
	@Override
	public String toSource(int depth) {
		StringBuilder sb = new StringBuilder();
		sb.append(makeIndent(depth));
		for (Node kid : this) {
			sb.append(cast(kid).toSource(depth + 1));
		}
		sb.append(makeIndent(depth));
		return sb.toString();
	}

	/** {@inheritDoc} */
	@Override
	public void visit(NodeVisitor v) {
		if (v.visit(this)) {
			for (Node kid : this) {
				cast(kid).visit(v);
			}
		}
	}
}
