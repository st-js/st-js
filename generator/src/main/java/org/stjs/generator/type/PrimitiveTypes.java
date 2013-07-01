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
import japa.parser.ast.type.PrimitiveType.Primitive;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.stjs.generator.STJSRuntimeException;
import org.stjs.generator.utils.ClassUtils;

import com.google.common.primitives.Primitives;

@SuppressWarnings("PMD.AvoidUsingShortType")
public final class PrimitiveTypes {
	private static final int BYTE_ORDER = 1;
	private static final int SHORT_ORDER = 2;
	private static final int INT_ORDER = 3;
	private static final int LONG_ORDER = 4;
	private static final int FLOAT_ORDER = 5;
	private static final int DOUBLE_ORDER = 6;

	private final static Map<Class<?>, Integer> COERCION_ORDER = new HashMap<Class<?>, Integer>();
	private final static Map<Primitive, Class<?>> PRIMITIVE_TYPES_BY_CODE = new EnumMap<Primitive, Class<?>>(
			Primitive.class);

	static {
		// coercion order
		COERCION_ORDER.put(char.class, BYTE_ORDER);
		// but char and byte are incompatible !
		COERCION_ORDER.put(byte.class, BYTE_ORDER);
		COERCION_ORDER.put(short.class, SHORT_ORDER);
		COERCION_ORDER.put(int.class, INT_ORDER);
		COERCION_ORDER.put(long.class, LONG_ORDER);
		COERCION_ORDER.put(float.class, FLOAT_ORDER);
		COERCION_ORDER.put(double.class, DOUBLE_ORDER);
		COERCION_ORDER.put(String.class, DOUBLE_ORDER);

		// primitive types by code
		PRIMITIVE_TYPES_BY_CODE.put(Primitive.Boolean, boolean.class);
		PRIMITIVE_TYPES_BY_CODE.put(Primitive.Char, char.class);
		PRIMITIVE_TYPES_BY_CODE.put(Primitive.Byte, byte.class);
		PRIMITIVE_TYPES_BY_CODE.put(Primitive.Short, short.class);
		PRIMITIVE_TYPES_BY_CODE.put(Primitive.Int, int.class);
		PRIMITIVE_TYPES_BY_CODE.put(Primitive.Long, long.class);
		PRIMITIVE_TYPES_BY_CODE.put(Primitive.Float, float.class);
		PRIMITIVE_TYPES_BY_CODE.put(Primitive.Double, double.class);
	}

	private PrimitiveTypes() {
		//
	}

	public static ClassWrapper primitiveReflectionType(PrimitiveType type) {
		Class<?> primitiveClass = PRIMITIVE_TYPES_BY_CODE.get(type.getType());
		if (primitiveClass != null) {
			return new ClassWrapper(primitiveClass);
		}

		throw new STJSRuntimeException("Strange primitive type:" + type.getType());

	}

	public static Type expressionResultType(Type left, Type right) {
		if (left == null || right == null) {
			return String.class;
		}
		Integer orderLeft = COERCION_ORDER.get(Primitives.unwrap(ClassUtils.getRawClazz(left)));
		if (orderLeft == null) {
			return String.class;
		}
		Integer orderRight = COERCION_ORDER.get(Primitives.unwrap(ClassUtils.getRawClazz(right)));
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
	public static boolean isAssignableFrom(Type t1, Type t2) {
		Integer order1 = t1 == null ? null : COERCION_ORDER.get(Primitives.unwrap(ClassUtils.getRawClazz(t1)));
		if (order1 == null) {
			return false;
		}
		Integer order2 = t2 == null ? null : COERCION_ORDER.get(Primitives.unwrap(ClassUtils.getRawClazz(t2)));
		if (order2 == null) {
			return false;
		}
		return order1 >= order2;
	}
}
