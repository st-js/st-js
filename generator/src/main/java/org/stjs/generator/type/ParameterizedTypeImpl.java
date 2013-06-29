package org.stjs.generator.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.Preconditions;

@Immutable
public final class ParameterizedTypeImpl implements ParameterizedType {

	private final Type rawType;
	private final Type[] actualTypeArguments;
	private final Type owner;

	public ParameterizedTypeImpl(@Nonnull Type rawType, @Nonnull Type[] actualTypeArguments, Type owner) {
		Preconditions.checkNotNull(rawType);
		Preconditions.checkNotNull(actualTypeArguments);
		// Preconditions.checkNotNull(owner);
		this.rawType = rawType;
		this.actualTypeArguments = Arrays.copyOf(actualTypeArguments, actualTypeArguments.length);
		this.owner = owner;
	}

	public Type getRawType() {
		return rawType;
	}

	public Type[] getActualTypeArguments() {
		return Arrays.copyOf(actualTypeArguments, actualTypeArguments.length);
	}

	public Type getOwnerType() {
		return owner;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ParameterizedType)) {
			return false;
		}
		// Check that information is equivalent
		ParameterizedType that = (ParameterizedType) o;
		if (this == that) {
			return true;
		}
		Type thatOwner = that.getOwnerType();
		Type thatRawType = that.getRawType();

		return (owner == null ? thatOwner == null : owner.equals(thatOwner))
				&& (rawType == null ? thatRawType == null : rawType.equals(thatRawType))
				&& Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(actualTypeArguments) ^ (owner == null ? 0 : owner.hashCode())
				^ (rawType == null ? 0 : rawType.hashCode());
	}
}