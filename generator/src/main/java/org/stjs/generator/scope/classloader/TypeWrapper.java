package org.stjs.generator.scope.classloader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import org.stjs.generator.utils.Option;

public interface TypeWrapper {
	public Type getType();

	public Option<Field> getDeclaredField(String name);

	public List<Method> getDeclaredMethods(String name);
}
