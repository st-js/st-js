package org.stjs.generator.javascript;

import java.util.EnumMap;
import java.util.Map;

import org.mozilla.javascript.Token;

import com.sun.source.tree.Tree.Kind;

/**
 * this is the list of the JavaScript operators and how they map on Java AST element Kind and Rhino token IDs
 *
 * @author acraciun
 * @version $Id: $Id
 */
public enum AssignOperator {
	// simple
	ASSIGN(Kind.ASSIGNMENT, Token.ASSIGN, null),
	// math
	MULTIPLY_ASSIGNMENT(Kind.MULTIPLY_ASSIGNMENT, Token.ASSIGN_MUL, BinaryOperator.MULTIPLY), DIVIDE_ASSIGNMENT(Kind.DIVIDE_ASSIGNMENT,
			Token.ASSIGN_DIV, BinaryOperator.DIVIDE), //
	REMAINDER_ASSIGNMENT(Kind.REMAINDER_ASSIGNMENT, Token.ASSIGN_MOD, BinaryOperator.REMAINDER), PLUS_ASSIGNMENT(Kind.PLUS_ASSIGNMENT,
			Token.ASSIGN_ADD, BinaryOperator.PLUS), //
	MINUS_ASSIGNMENT(Kind.MINUS_ASSIGNMENT, Token.ASSIGN_SUB, BinaryOperator.MINUS),
	// bit
	LEFT_SHIFT_ASSIGNMENT(Kind.LEFT_SHIFT_ASSIGNMENT, Token.ASSIGN_LSH, BinaryOperator.LEFT_SHIFT), //
	RIGHT_SHIFT_ASSIGNMENT(Kind.RIGHT_SHIFT_ASSIGNMENT, Token.ASSIGN_RSH, BinaryOperator.RIGHT_SHIFT), //
	UNSIGNED_RIGHT_SHIFT_ASSIGNMENT(Kind.UNSIGNED_RIGHT_SHIFT_ASSIGNMENT, Token.ASSIGN_URSH, BinaryOperator.UNSIGNED_RIGHT_SHIFT),
	// logical
	AND_ASSIGNMENT(Kind.AND_ASSIGNMENT, Token.ASSIGN_BITAND, BinaryOperator.AND), XOR_ASSIGNMENT(Kind.XOR_ASSIGNMENT, Token.ASSIGN_BITXOR,
			BinaryOperator.XOR), OR_ASSIGNMENT(Kind.OR_ASSIGNMENT, Token.ASSIGN_BITOR, BinaryOperator.OR);

	private final Kind java;
	private final int javaScript;
	private final BinaryOperator binaryOperator;

	private AssignOperator(Kind java, int javaScript, BinaryOperator op) {
		this.java = java;
		this.javaScript = javaScript;
		this.binaryOperator = op;
	}

	/**
	 * <p>
	 * Getter for the field <code>binaryOperator</code>.
	 * </p>
	 *
	 * @return a {@link org.stjs.generator.javascript.BinaryOperator} object.
	 */
	public BinaryOperator getBinaryOperator() {
		return binaryOperator;
	}

	/**
	 * <p>
	 * Getter for the field <code>java</code>.
	 * </p>
	 *
	 * @return a {@link com.sun.source.tree.Tree.Kind} object.
	 */
	public Kind getJava() {
		return java;
	}

	/**
	 * <p>
	 * Getter for the field <code>javaScript</code>.
	 * </p>
	 *
	 * @return a int.
	 */
	public int getJavaScript() {
		return javaScript;
	}

	private static final Map<Kind, AssignOperator> BY_JAVA_OPERATOR = new EnumMap<Kind, AssignOperator>(Kind.class);
	static {
		for (AssignOperator op : values()) {
			BY_JAVA_OPERATOR.put(op.getJava(), op);
		}
	}

	/**
	 * <p>
	 * valueOf.
	 * </p>
	 *
	 * @param javaOperator
	 *            a {@link com.sun.source.tree.Tree.Kind} object.
	 * @return a {@link org.stjs.generator.javascript.AssignOperator} object.
	 */
	public static AssignOperator valueOf(Kind javaOperator) {
		return BY_JAVA_OPERATOR.get(javaOperator);
	}
}
