/*
 * Copyright (c) 2010, Netherlands Forensic Institute
 * Copyright (c) 2010, Ewald Snel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this list of
 *    conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, this list
 *    of conditions and the following disclaimer in the documentation and/or other materials
 *    provided with the distribution.
 *  * Neither the name of the Netherlands Forensic Institute nor the names of its
 *    contributors may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.stjs.generator.scope.classloader;

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
