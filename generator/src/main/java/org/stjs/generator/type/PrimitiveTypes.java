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

public class PrimitiveTypes {

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
}
