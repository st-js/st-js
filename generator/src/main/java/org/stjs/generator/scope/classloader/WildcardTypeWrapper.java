package org.stjs.generator.scope.classloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import org.stjs.generator.utils.Option;

/**
 * 
 * @author acraciun
 * 
 */
public class WildcardTypeWrapper implements TypeWrapper {
	private final WildcardType type;
	private final TypeWrapper[] boundsWrappers;

	public WildcardTypeWrapper(WildcardType type) {
		this.type = type;
		boundsWrappers = new TypeWrapper[type.getUpperBounds().length];
		for (int i = 0; i < type.getUpperBounds().length; ++i) {
			boundsWrappers[i] = TypeWrappers.wrap(type.getUpperBounds()[i]);
		}
	}

	@Override
	public Type getType() {
		return type;
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
	public String getSimpleName() {
		return "?";
	}

	@Override
	public String getName() {
		return "?";
	}

	@Override
	public String getExternalName() {
		return "?";
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
		// wildcards are only as type parameters for a parameterized type
		return true;
	}

	@Override
	public TypeWrapper getComponentType() {
		// TODO should this be something else !?
		return null;
	}
}
