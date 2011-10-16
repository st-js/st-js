package org.stjs.generator.scope.classloader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.stjs.generator.utils.Option;

public class ParameterizedTypeWrapper implements TypeWrapper {
	private final ParameterizedType type;

	public ParameterizedTypeWrapper(ParameterizedType type) {
		this.type = type;
	}

	@Override
	public Option<Field> getDeclaredField(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Method> getDeclaredMethods(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		return type;
	}

}
