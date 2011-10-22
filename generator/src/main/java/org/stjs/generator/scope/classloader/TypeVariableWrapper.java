package org.stjs.generator.scope.classloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

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
	public Option<FieldWrapper> findField(String name) {
		for (TypeWrapper bound : boundsWrappers) {
			Option<FieldWrapper> field = bound.findField(name);
			if (field.isDefined()) {
				return field;
			}
		}

		return Option.none();
	}

	@Override
	public Option<MethodWrapper> findMethod(String name, TypeWrapper... paramTypes) {
		for (TypeWrapper bound : boundsWrappers) {
			Option<MethodWrapper> method = bound.findMethod(name, paramTypes);
			if (method.isDefined()) {
				return method;
			}
		}

		return Option.none();
	}

	@Override
	public Type getType() {
		return typeVariable;
	}

	@Override
	public String getSimpleName() {
		return typeVariable.getName();
	}

	@Override
	public String getName() {
		return typeVariable.getName();
	}

	@Override
	public String getExternalName() {
		return typeVariable.getName();
	}

	@Override
	public boolean isImportable() {
		return false;
	}

	@Override
	public boolean isInnerType() {
		return false;
	}

	@Override
	public boolean isParentClassOf(ClassWrapper clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAnnotation(Class<? extends Annotation> a) {
		for (TypeWrapper bound : boundsWrappers) {
			if (bound.hasAnnotation(a)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isAssignableFrom(TypeWrapper typeWrapper) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TypeWrapper getComponentType() {
		// TODO should this be something else !?
		return null;
	}

}
