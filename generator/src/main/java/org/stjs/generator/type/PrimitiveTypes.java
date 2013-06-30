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
package org.stjs.generator.type;

import japa.parser.ast.type.PrimitiveType;

import java.util.HashMap;
import java.util.Map;

import org.stjs.generator.utils.ClassUtils;

import com.google.common.primitives.Primitives;

public final class PrimitiveTypes {
	private static final int BYTE_ORDER = 1;
	private static final int SHORT_ORDER = 2;
	private static final int INT_ORDER = 3;
	private static final int LONG_ORDER = 4;
	private static final int FLOAT_ORDER = 5;
	private static final int DOUBLE_ORDER = 6;

	private PrimitiveTypes() {
		//
	}

	private final static Map<Class<?>, Integer> COERCION_ORDER = new HashMap<Class<?>, Integer>();
	static {
		COERCION_ORDER.put(char.class, BYTE_ORDER);
		// but char and byte are incompatible !
		COERCION_ORDER.put(byte.class, BYTE_ORDER);
		COERCION_ORDER.put(short.class, SHORT_ORDER);
		COERCION_ORDER.put(int.class, INT_ORDER);
		COERCION_ORDER.put(long.class, LONG_ORDER);
		COERCION_ORDER.put(float.class, FLOAT_ORDER);
		COERCION_ORDER.put(double.class, DOUBLE_ORDER);
		COERCION_ORDER.put(String.class, DOUBLE_ORDER);
	}

	public static ClassWrapper primitiveReflectionType(PrimitiveType type) {
		switch (type.getType()) {
		case Boolean:
			return new ClassWrapper(boolean.class);
		case Char:
			return new ClassWrapper(char.class);
		case Byte:
			return new ClassWrapper(byte.class);
		case Short:
			return new ClassWrapper(short.class);
		case Int:
			return new ClassWrapper(int.class);
		case Long:
			return new ClassWrapper(long.class);
		case Float:
			return new ClassWrapper(float.class);
		case Double:
			return new ClassWrapper(double.class);
		default:
			throw new RuntimeException("Fuck java switches");
		}
	}

	public static java.lang.reflect.Type expressionResultType(java.lang.reflect.Type left, java.lang.reflect.Type right) {
		Integer orderLeft = left != null ? COERCION_ORDER.get(Primitives.unwrap(ClassUtils.getRawClazz(left))) : null;
		if (orderLeft == null) {
			return String.class;
		}
		Integer orderRight = right != null ? COERCION_ORDER.get(Primitives.unwrap(ClassUtils.getRawClazz(right)))
				: null;
		if (orderRight == null) {
			return String.class;
		}
		return orderLeft > orderRight ? left : right;
	}

	/**
	 * it only takes into consideration the coercion order!
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static boolean isAssignableFrom(java.lang.reflect.Type t1, java.lang.reflect.Type t2) {
		Integer order1 = t1 != null ? COERCION_ORDER.get(Primitives.unwrap(ClassUtils.getRawClazz(t1))) : null;
		if (order1 == null) {
			return false;
		}
		Integer order2 = t2 != null ? COERCION_ORDER.get(Primitives.unwrap(ClassUtils.getRawClazz(t2))) : null;
		if (order2 == null) {
			return false;
		}
		return order1 >= order2;
	}
}
