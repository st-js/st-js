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
package org.stjs.generator.utils;

import japa.parser.ast.expr.BinaryExpr.Operator;

import java.util.EnumSet;

/**
 * This class is a helper for the {@link japa.parser.ast.expr.BinaryExpr.BinaryExpr.Operator} class
 * 
 * @author acraciun
 * 
 */
public final class Operators {
	private Operators() {
		//
	}

	private static final EnumSet<Operator> LOGICAL_OPERATORS = EnumSet.of(Operator.and, Operator.notEquals,
			Operator.equals, Operator.greater, Operator.greaterEquals, Operator.less, Operator.lessEquals, Operator.or,
			Operator.xor);

	public static boolean isLogical(Operator op) {
		return LOGICAL_OPERATORS.contains(op);
	}
}
