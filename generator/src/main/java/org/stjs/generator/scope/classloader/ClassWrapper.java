package org.stjs.generator.scope.classloader;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.STATIC;
import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.stjs.generator.utils.Option;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class ClassWrapper {

	private static final Function<Class<?>, ClassWrapper> WrapClass = new Function<Class<?>, ClassWrapper>() {
		@Override
		public ClassWrapper apply(Class<?> clazz) {
			return new ClassWrapper(clazz);
		}
	};
	
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
		return declaringClass != null ? Option.some(new ClassWrapper(
				declaringClass)) : Option.<ClassWrapper> none();
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

	public <T> Option<ClassWrapper> getDeclaredClass(String name) {
		try {
			for (Class<?> innerClass : clazz.getDeclaredClasses()) {
				if (innerClass.getSimpleName().equals(name)) {
					return Option.some(new ClassWrapper(innerClass));
				}
			}
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
		return Option.none();
	}

	public List<Method> getDeclaredMethods(final String name) {
		return ImmutableList.copyOf(Iterables.filter(
				getDeclaredMethods(), new Predicate<Method>() {
					@Override
					public boolean apply(Method method) {
						return method.getName().equals(name);
					}
				}));
	}

	public List<Method> getDeclaredMethods() {
		return asList(clazz.getDeclaredMethods());
	}

	public boolean hasDeclaredField(String fieldName) {
		return getDeclaredField(fieldName).isDefined();
	}

	public Option<ClassWrapper> getSuperclass() {
		Class<?> superClass = clazz.getSuperclass();
		return superClass != null ? Option.some(new ClassWrapper(superClass))
				: Option.<ClassWrapper> none();
	}

	public String getName() {
		return clazz.getName();
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public List<Field> getDeclaredNonPrivateStaticFields() {
		return ImmutableList.copyOf(filter(
				asList(clazz.getDeclaredFields()), 
				new Predicate<Field>() {

					@Override
					public boolean apply(Field field) {
						return isStaticButNotPrivate(field.getModifiers());
					}
				}));
	}
    
	private boolean isStaticButNotPrivate(int modifiers) {
		return (modifiers & (PRIVATE | STATIC)) == STATIC;
	}
	
	public List<Method> getDeclaredNonPrivateStaticMethods() {
		return ImmutableList.copyOf(filter(
				asList(clazz.getDeclaredMethods()), 
				new Predicate<Method>() {

					@Override
					public boolean apply(Method method) {
						return isStaticButNotPrivate(method.getModifiers());
					}
				}));
	}
	
	public List<ClassWrapper> getDeclaredNonPrivateStaticClasses() {

		return ImmutableList.copyOf(
				transform(
					filter(
						asList(clazz.getDeclaredClasses()), 
						new Predicate<Class<?>>() {
							@Override
							public boolean apply(Class<?> clazz) {
								return isStaticButNotPrivate(clazz.getModifiers());
							}
						}),
				WrapClass
			));
	}

	public List<ClassWrapper> getDeclaredClasses() {
		return ImmutableList.copyOf(
				transform(asList(clazz.getDeclaredClasses()),
						WrapClass));
	}

	public List<Field> getDeclaredFields() {
		return asList(clazz.getDeclaredFields());
	}

}