package org.stjs.generator.type;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.Preconditions;

@Immutable
public class TypeVariableImpl<D extends GenericDeclaration> implements TypeVariable<D> {
	private final D genericDeclaration;
	private final Type[] bounds;
	private final String name;

	public TypeVariableImpl(D genericDeclaration, @Nonnull Type[] bounds, String name) {
		// Preconditions.checkNotNull(genericDeclaration);
		Preconditions.checkNotNull(bounds);
		Preconditions.checkNotNull(name);
		this.genericDeclaration = genericDeclaration;
		this.bounds = Arrays.copyOf(bounds, bounds.length);
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bounds);
		result = prime * result + ((genericDeclaration == null) ? 0 : genericDeclaration.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TypeVariableImpl<?> other = (TypeVariableImpl<?>) obj;
		if (!Arrays.equals(bounds, other.bounds)) {
			return false;
		}
		if (genericDeclaration == null) {
			if (other.genericDeclaration != null) {
				return false;
			}
		} else if (!genericDeclaration.equals(other.genericDeclaration)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public Type[] getBounds() {
		return Arrays.copyOf(bounds, bounds.length);
	}

	@Override
	public D getGenericDeclaration() {
		return genericDeclaration;
	}

	@Override
	public String getName() {
		return name;
	}

}
