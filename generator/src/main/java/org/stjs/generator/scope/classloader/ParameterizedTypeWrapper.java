package org.stjs.generator.scope.classloader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This is a wrapper around a parameterized type. Basically it was the same behavior as a regular class
 * 
 * @author acraciun
 * 
 */
public class ParameterizedTypeWrapper extends ClassWrapper {
	private final ParameterizedType type;

	public ParameterizedTypeWrapper(ParameterizedType type) {
		super(getRawClazz(type.getRawType()));
		this.type = type;
	}

	@Override
	public Type getType() {
		return type;
	}

}
