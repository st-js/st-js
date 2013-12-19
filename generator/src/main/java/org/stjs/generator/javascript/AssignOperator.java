package org.stjs.generator.javascript;

import java.util.EnumMap;
import java.util.Map;

import org.mozilla.javascript.Token;

import com.sun.source.tree.Tree.Kind;

/**
 * this is the list of the JavaScript operators and how they map on Java AST element Kind and Rhino token IDs
 * @author acraciun
 */
public enum AssignOperator {
	//simple
	ASSIGN(Kind.ASSIGNMENT, Token.ASSIGN),
	//math
	MULTIPLY_ASSIGNMENT(Kind.MULTIPLY_ASSIGNMENT, Token.ASSIGN_MUL), DIVIDE_ASSIGNMENT(Kind.DIVIDE_ASSIGNMENT, Token.ASSIGN_DIV),
	REMAINDER_ASSIGNMENT(Kind.REMAINDER_ASSIGNMENT, Token.ASSIGN_MOD), PLUS_ASSIGNMENT(Kind.PLUS_ASSIGNMENT, Token.ASSIGN_ADD),
	MINUS_ASSIGNMENT(Kind.MINUS_ASSIGNMENT, Token.ASSIGN_SUB),
	//bit
	LEFT_SHIFT_ASSIGNMENT(Kind.LEFT_SHIFT_ASSIGNMENT, Token.ASSIGN_LSH), RIGHT_SHIFT_ASSIGNMENT(Kind.RIGHT_SHIFT_ASSIGNMENT, Token.ASSIGN_RSH),
	UNSIGNED_RIGHT_SHIFT_ASSIGNMENT(Kind.UNSIGNED_RIGHT_SHIFT_ASSIGNMENT, Token.ASSIGN_URSH),
	//logical
	AND_ASSIGNMENT(Kind.AND_ASSIGNMENT, Token.ASSIGN_BITAND), XOR_ASSIGNMENT(Kind.XOR_ASSIGNMENT, Token.ASSIGN_BITXOR), OR_ASSIGNMENT(
			Kind.OR_ASSIGNMENT, Token.ASSIGN_BITOR);

	private final Kind java;
	private final int javaScript;

	private AssignOperator(Kind java, int javaScript) {
		this.java = java;
		this.javaScript = javaScript;
	}

	public Kind getJava() {
		return java;
	}

	public int getJavaScript() {
		return javaScript;
	}

	private static final Map<Kind, AssignOperator> byJavaOperator = new EnumMap<Kind, AssignOperator>(Kind.class);
	static {
		for (AssignOperator op : values()) {
			byJavaOperator.put(op.getJava(), op);
		}
	}

	public static AssignOperator valueOf(Kind javaOperator) {
		return byJavaOperator.get(javaOperator);
	}
}
