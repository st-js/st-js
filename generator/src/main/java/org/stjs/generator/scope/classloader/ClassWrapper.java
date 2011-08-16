package org.stjs.generator.scope.classloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.stjs.generator.handlers.utils.Option;

public class ClassWrapper {

	private final Class<?> clazz;

	public ClassWrapper(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getPackageName() {
		return clazz.getPackage().getName();
	}

	public String getSimpleName() {
		return clazz.getSimpleName();
	}

	public Option<ClassWrapper> getDeclaringClass() {
		Class<?> declaringClass = clazz.getDeclaringClass();
		return declaringClass != null ? Option.some(new ClassWrapper(declaringClass)) : Option.<ClassWrapper> none();
	}

	public int getModifiers() {
		return clazz.getModifiers();
	}

	public Annotation[] getAnnotations() {
		return clazz.getAnnotations();
	}

	public Option<Field> getDeclaredField(String name) {
		try {
			return Option.some(clazz.getDeclaredField(name));
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			return Option.none();
		}
	}

	public Method[] getDeclaredMethods() {
		return clazz.getDeclaredMethods();
	}

	public boolean hasDeclaredField(String fieldName) {
		return getDeclaredField(fieldName).isDefined();
	}

	public Option<ClassWrapper> getSuperclass() {
		Class<?> superClass = clazz.getSuperclass();
		return superClass != null ? Option.some(new ClassWrapper(superClass)) : Option.<ClassWrapper> none();
	}

	public String getName() {
		return clazz.getName();
	}

}
