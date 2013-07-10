/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http,//www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator.utils;

import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

/**
 * This class is a helper for the {@link japa.parser.ast.expr.BinaryExpr.Operator} class
 * 
 * @author acraciun
 */
public final class Operators {

	private static final EnumSet<BinaryExpr.Operator> LOGICAL_OPERATORS = EnumSet.of(BinaryExpr.Operator.and,
			BinaryExpr.Operator.notEquals, BinaryExpr.Operator.equals, BinaryExpr.Operator.greater,
			BinaryExpr.Operator.greaterEquals, BinaryExpr.Operator.less, BinaryExpr.Operator.lessEquals,
			BinaryExpr.Operator.or, BinaryExpr.Operator.xor);

	private static final Map<BinaryExpr.Operator, String> BINARY_OPERATOR_DISPLAY = new EnumMap<BinaryExpr.Operator, String>(
			BinaryExpr.Operator.class);
	private static final Map<AssignExpr.Operator, String> ASSIGN_OPERATOR_DISPLAY = new EnumMap<AssignExpr.Operator, String>(
			AssignExpr.Operator.class);

	static {
		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.or, "||");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.and, "&&");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.binOr, "|");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.binAnd, "&");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.xor, "^");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.equals, "==");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.notEquals, "!=");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.less, "<");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.greater, ">");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.lessEquals, "<=");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.greaterEquals, ">=");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.lShift, "<<");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.rSignedShift, ">>");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.rUnsignedShift, ">>>");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.plus, "+");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.minus, "-");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.times, "*");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.divide, "/");

		BINARY_OPERATOR_DISPLAY.put(BinaryExpr.Operator.remainder, "%");

		// assign
		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.assign, "=");

		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.and, "&=");

		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.or, "|=");

		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.xor, "^=");

		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.plus, "+=");

		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.minus, "-=");

		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.rem, "%=");

		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.slash, "/=");

		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.star, "*=");

		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.lShift, "<<=");

		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.rSignedShift, ">>=");

		ASSIGN_OPERATOR_DISPLAY.put(AssignExpr.Operator.rUnsignedShift, ">>>=");

	}

	private Operators() {
		//
	}

	public static boolean isLogical(BinaryExpr.Operator op) {
		return LOGICAL_OPERATORS.contains(op);
	}

	public static String getDisplay(BinaryExpr.Operator op) {
		return BINARY_OPERATOR_DISPLAY.get(op);
	}

	public static String getDisplay(AssignExpr.Operator op) {
		return ASSIGN_OPERATOR_DISPLAY.get(op);
	}
}
