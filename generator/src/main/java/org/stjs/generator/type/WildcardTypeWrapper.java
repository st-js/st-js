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
