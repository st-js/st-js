package org.stjs.generator.javascript;

import java.util.EnumMap;
import java.util.Map;

import org.mozilla.javascript.Token;

import com.sun.source.tree.Tree.Kind;

/**
 * this is the list of the JavaScript operators and how they map on Java AST element Kind and Rhino token IDs
 * 
 * @author acraciun
 */
public enum BinaryOperator {
	// math
	MULTIPLY(Kind.MULTIPLY, Token.MUL), DIVIDE(Kind.DIVIDE, Token.DIV), REMAINDER(Kind.REMAINDER, Token.MOD), //
	PLUS(Kind.PLUS, Token.ADD), MINUS(Kind.MINUS, Token.SUB),

	// bit
	LEFT_SHIFT(Kind.LEFT_SHIFT, Token.LSH), RIGHT_SHIFT(Kind.RIGHT_SHIFT, Token.RSH), //
	UNSIGNED_RIGHT_SHIFT(Kind.UNSIGNED_RIGHT_SHIFT, Token.URSH), AND(Kind.AND, Token.BITAND), //
	XOR(Kind.XOR, Token.BITXOR), OR(Kind.OR, Token.BITOR),

	// comparator
	LESS_THAN(Kind.LESS_THAN, Token.LT), LESS_THAN_EQUAL(Kind.LESS_THAN_EQUAL, Token.LE), //
	GREATER_THAN(Kind.GREATER_THAN, Token.GT), GREATER_THAN_EQUAL(Kind.GREATER_THAN_EQUAL, Token.GE), //
	EQUAL_TO(Kind.EQUAL_TO, Token.EQ), NOT_EQUAL_TO(Kind.NOT_EQUAL_TO, Token.NE),

	// logical
	CONDITIONAL_AND(Kind.CONDITIONAL_AND, Token.AND), CONDITIONAL_OR(Kind.CONDITIONAL_OR, Token.OR),

	// javascript only
	COMMA(null, Token.COMMA);

	private final Kind java;
	private final int javaScript;

	private BinaryOperator(Kind java, int javaScript) {
		this.java = java;
		this.javaScript = javaScript;
	}

	public Kind getJava() {
		return java;
	}

	public int getJavaScript() {
		return javaScript;
	}

	private static final Map<Kind, BinaryOperator> BY_JAVA_OPERATOR = new EnumMap<Kind, BinaryOperator>(Kind.class);
	static {
		for (BinaryOperator op : values()) {
			if (op.getJava() != null) {
				BY_JAVA_OPERATOR.put(op.getJava(), op);
			}
		}
	}

	public static BinaryOperator valueOf(Kind javaOperator) {
		return BY_JAVA_OPERATOR.get(javaOperator);
	}
}
