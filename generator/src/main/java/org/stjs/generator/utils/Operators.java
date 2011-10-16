package org.stjs.generator.utils;

import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BinaryExpr.Operator;

import java.util.EnumSet;

/**
 * This class is a helper for the {@link BinaryExpr.Operator} class
 * 
 * @author acraciun
 * 
 */
public class Operators {
	private static final EnumSet<Operator> logicalOperators = EnumSet.of(Operator.and, Operator.notEquals,
			Operator.equals, Operator.greater, Operator.greaterEquals, Operator.less, Operator.lessEquals, Operator.or,
			Operator.xor);

	public static boolean isLogical(Operator op) {
		return logicalOperators.contains(op);
	}
}
