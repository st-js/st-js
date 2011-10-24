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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.stjs.generator.utils.ClassUtils;

/**
 * This is a wrapper around a parameterized type. Basically it was the same behavior as a regular class
 * 
 * @author acraciun
 * 
 */
public class ParameterizedTypeWrapper extends ClassWrapper {
	private final ParameterizedType type;

	public ParameterizedTypeWrapper(ParameterizedType type) {
		super(ClassUtils.getRawClazz(type.getRawType()));
		this.type = type;
	}

	@Override
	public Type getType() {
		return type;
	}

	public TypeWrapper[] getActualTypeArguments() {
		return TypeWrappers.wrap(type.getActualTypeArguments());
	}

}
