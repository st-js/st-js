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

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

public class GenericArrayTypeWrapper extends ClassWrapper {
	private final GenericArrayType type;
	private final TypeWrapper componentType;

	public GenericArrayTypeWrapper(GenericArrayType type) {
		super(Object[].class);
		this.type = type;
		componentType = TypeWrappers.wrap(type.getGenericComponentType());
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public String getSimpleName() {
		return componentType.getSimpleName() + "[]";
	}

	@Override
	public String getName() {
		// XXX: not sure about this
		return componentType.getName() + "[]";
	}

	@Override
	public String getExternalName() {
		return componentType.getExternalName() + "[]";
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
	public boolean hasAnnotation(Class<? extends Annotation> class1) {
		return false;
	}

	@Override
	public boolean isAssignableFrom(TypeWrapper typeWrapper) {
		if (typeWrapper.getComponentType() == null) {
			return false;
		}
		return componentType.isAssignableFrom(typeWrapper.getComponentType());
	}

	@Override
	public TypeWrapper getComponentType() {
		return componentType;
	}

}
