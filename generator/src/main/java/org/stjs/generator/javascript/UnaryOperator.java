package org.stjs.generator.javascript;

import java.util.EnumMap;
import java.util.Map;

import org.mozilla.javascript.Token;

import com.sun.source.tree.Tree.Kind;

/**
 * this is the list of the JavaScript operators and how they map on Java AST element Kind and Rhino token IDs. Some
 * JavaScript operators do not have their Java counter part: like delete or typeof
 *
 * @author acraciun
 * @version $Id: $Id
 */
public enum UnaryOperator {
	// increments
	POSTFIX_INCREMENT(Kind.POSTFIX_INCREMENT, true, Token.INC), POSTFIX_DECREMENT(Kind.POSTFIX_DECREMENT, true, Token.DEC), PREFIX_INCREMENT(
			Kind.PREFIX_INCREMENT, false, Token.INC), PREFIX_DECREMENT(Kind.PREFIX_DECREMENT, false, Token.DEC),

	//
	UNARY_PLUS(Kind.UNARY_PLUS, false, Token.ADD), UNARY_MINUS(Kind.UNARY_MINUS, false, Token.SUB), BITWISE_COMPLEMENT(Kind.BITWISE_COMPLEMENT,
			false, Token.BITNOT), LOGICAL_COMPLEMENT(Kind.LOGICAL_COMPLEMENT, false, Token.NOT),
	// specific java script
	TYPEOF(null, false, Token.TYPEOF), DELETE_PROPERTY(null, false, Token.DELPROP);

	private final Kind java;
	private final boolean postfix;
	private final int javaScript;

	private UnaryOperator(Kind java, boolean postfix, int operator) {
		this.java = java;
		this.postfix = postfix;
		this.javaScript = operator;
	}

	/**
	 * <p>isPostfix.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isPostfix() {
		return postfix;
	}

	/**
	 * <p>Getter for the field <code>java</code>.</p>
	 *
	 * @return a {@link com.sun.source.tree.Tree.Kind} object.
	 */
	public Kind getJava() {
		return java;
	}

	/**
	 * <p>Getter for the field <code>javaScript</code>.</p>
	 *
	 * @return a int.
	 */
	public int getJavaScript() {
		return javaScript;
	}

	private static final Map<Kind, UnaryOperator> BY_JAVA_OPERATOR = new EnumMap<Kind, UnaryOperator>(Kind.class);
	static {
		for (UnaryOperator op : values()) {
			if (op.getJava() != null) {
				BY_JAVA_OPERATOR.put(op.getJava(), op);
			}
		}
	}

	/**
	 * <p>valueOf.</p>
	 *
	 * @param javaOperator a {@link com.sun.source.tree.Tree.Kind} object.
	 * @return a {@link org.stjs.generator.javascript.UnaryOperator} object.
	 */
	public static UnaryOperator valueOf(Kind javaOperator) {
		return BY_JAVA_OPERATOR.get(javaOperator);
	}
}
