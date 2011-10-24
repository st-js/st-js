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

public class PrimitiveTypes {
	private final static Map<Class<?>, Integer> coercionOrder = new HashMap<Class<?>, Integer>();
	static {
		coercionOrder.put(char.class, 1);
		coercionOrder.put(byte.class, 1);// but char and byte are incompatible !
		coercionOrder.put(short.class, 2);
		coercionOrder.put(int.class, 3);
		coercionOrder.put(long.class, 4);
		coercionOrder.put(float.class, 5);
		coercionOrder.put(double.class, 6);
		coercionOrder.put(String.class, 6);
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
		Integer orderLeft = left != null ? coercionOrder.get(Primitives.unwrap(ClassUtils.getRawClazz(left))) : null;
		if (orderLeft == null) {
			return String.class;
		}
		Integer orderRight = right != null ? coercionOrder.get(Primitives.unwrap(ClassUtils.getRawClazz(right))) : null;
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
		Integer order1 = t1 != null ? coercionOrder.get(Primitives.unwrap(ClassUtils.getRawClazz(t1))) : null;
		if (order1 == null) {
			return false;
		}
		Integer order2 = t2 != null ? coercionOrder.get(Primitives.unwrap(ClassUtils.getRawClazz(t2))) : null;
		if (order2 == null) {
			return false;
		}
		return order1 >= order2;
	}
}
