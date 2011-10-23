/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.stjs.generator.type;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

/**
 * The {@link WildcardTypeImpl} is an implementation of the {@link WildcardType} interface.
 * <p>
 * This class aims to be compatible with the default implementation of {@link WildcardType} that is available in the Sun
 * JDK, in particular the {@link #equals(Object)} and {@link #hashCode()} methods.
 */
public final class WildcardTypeImpl implements WildcardType {
	private final Type[] lowerBounds;
	private final Type[] upperBounds;

	/**
	 * Creates a new {@link WildcardTypeImpl}.
	 * 
	 * @param lowerBounds
	 *            the lower bounds
	 * @param upperBounds
	 *            the upper bounds
	 * 
	 * @throws NullPointerException
	 *             if any argument is {@code null}
	 */
	public WildcardTypeImpl(final Type[] lowerBounds, final Type[] upperBounds) {
		this.lowerBounds = lowerBounds;
		this.upperBounds = upperBounds;
	}

	@Override
	public Type[] getLowerBounds() {
		return lowerBounds;
	}

	@Override
	public Type[] getUpperBounds() {
		return upperBounds;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof WildcardType)) {
			return false;
		}

		final WildcardType other = (WildcardType) o;
		if (!Arrays.equals(lowerBounds, other.getLowerBounds())) {
			return false;
		}
		if (!Arrays.equals(upperBounds, other.getUpperBounds())) {
			return false;
		}

		return true; // o is equal!
	}

	@Override
	public int hashCode() {
		// WARNING: Do not edit!! This might break compatibility!!!
		return Arrays.hashCode(lowerBounds) ^ Arrays.hashCode(upperBounds);
	}

	@Override
	public String toString() {
		if (lowerBounds.length > 0) {
			return boundsToString("? super ", lowerBounds);
		}
		if ((upperBounds.length > 0) && !upperBounds[0].equals(Object.class)) {
			return boundsToString("? extends ", upperBounds);
		}

		return "?"; // Unbounded wildcard type
	}

	private static String boundsToString(final String prefix, final Type[] bounds) {
		final StringBuilder sb = new StringBuilder();
		sb.append(prefix);
		sb.append(bounds[0]);

		for (int i = 1; i < bounds.length; i++) {
			sb.append(" & ");
			sb.append(bounds[i]);
		}
		return sb.toString();
	}
}
