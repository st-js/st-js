package org.stjs.generator.scope.classloader;

import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.utils.Option;

/**
 * this is a wrapper around a {@link TypeVariable} to allow the discovery of a field or a method
 * 
 * @author acraciun
 * 
 */
public class TypeVariableWrapper<D extends GenericDeclaration> implements TypeWrapper {
	private final TypeVariable<D> typeVariable;
	private final TypeWrapper[] boundsWrappers;

	public TypeVariableWrapper(TypeVariable<D> typeVariable) {
		this.typeVariable = typeVariable;
		boundsWrappers = new TypeWrapper[typeVariable.getBounds().length];
		for (int i = 0; i < typeVariable.getBounds().length; ++i) {
			boundsWrappers[i] = TypeWrappers.wrap(typeVariable.getBounds()[i]);
		}
	}

	@Override
	public Option<Field> getDeclaredField(String name) {
		for (TypeWrapper bound : boundsWrappers) {
			Option<Field> field = bound.getDeclaredField(name);
			if (field.isDefined()) {
				return field;
			}
		}

		return Option.none();
	}

	@Override
	public List<Method> getDeclaredMethods(String name) {
		List<Method> methods = new ArrayList<Method>();
		for (TypeWrapper bound : boundsWrappers) {
			// TODO it could be an interface or a class is found several times, so remove duplicates
			methods.addAll(bound.getDeclaredMethods(name));
		}
		return methods;
	}

	@Override
	public Type getType() {
		return typeVariable;
	}

}
