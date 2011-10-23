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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * simple implementation of GenericArrayType
 * 
 * @author acraciun
 */
public final class GenericArrayTypeImpl implements GenericArrayType {

	private final Type genericComponentType;

	public GenericArrayTypeImpl(Type genericComponentType) {
		this.genericComponentType = genericComponentType;
	}

	public Type getGenericComponentType() {
		return genericComponentType;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GenericArrayType)) {
			return false;
		}
		GenericArrayType that = (GenericArrayType) o;
		Type thatComponentType = that.getGenericComponentType();
		return genericComponentType == null ? thatComponentType == null : genericComponentType
				.equals(thatComponentType);
	}

	@Override
	public int hashCode() {
		return (genericComponentType == null) ? 0 : genericComponentType.hashCode();
	}
}
