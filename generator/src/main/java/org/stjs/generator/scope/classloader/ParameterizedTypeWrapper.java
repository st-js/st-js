package org.stjs.generator.scope.classloader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
