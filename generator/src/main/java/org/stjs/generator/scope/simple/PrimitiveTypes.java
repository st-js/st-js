package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.classloader.ClassWrapper;

import japa.parser.ast.type.PrimitiveType;

public class PrimitiveTypes {

	static ClassWrapper primitiveReflectionType(PrimitiveType type) {
		switch(type.getType()) {
			case Boolean:
				return new ClassWrapper(boolean.class);
			case Char:
				return new ClassWrapper( char.class);
			case Byte:
				return new ClassWrapper( byte.class);
			case Short:
				return new ClassWrapper( short.class);
			case Int:
				return new ClassWrapper( int.class);
			case Long:
				return new ClassWrapper( long.class);
			case Float:
				return new ClassWrapper( float.class);
			case Double:
				return new ClassWrapper( double.class);
			default :
				throw new RuntimeException("Fuck java switches");
		}
	}
}
