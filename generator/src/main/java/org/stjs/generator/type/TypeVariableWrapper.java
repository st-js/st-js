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
		if (typeWrapper == null) {
			return true;
		}
		// it's different from what happens when the given typeWrapper corresponds to a variable assignment inside the
		// method/class or is the parameter of the method i.e
		// public static <T> void method(T t){}
		// in the method i cannot to t=1;
		// by i can call method(1)

		// outside the method
		if (typeVariable.getBounds().length == 0) {
			return true;
		}

		for (TypeWrapper bound : boundsWrappers) {
			if (bound.isAssignableFrom(typeWrapper)) {
				return true;
			}
		}
		return false;

		// inside the method
		/**
		 * <pre>
		 * if (!(typeWrapper instanceof TypeVariableWrapper)) {
		 * 	return false;
		 * }
		 * 
		 * TypeVariable&lt;?&gt; otherType = ((TypeVariableWrapper&lt;?&gt;) typeWrapper).typeVariable;
		 * if (otherType == typeVariable) {
		 * 	return true;
		 * }
		 * for (Type bound : otherType.getBounds()) {
		 * 	if (typeVariable == bound) {
		 * 		return true;
		 * 	}
		 * }
		 * return false;
		 * </pre>
		 */
	}

	@Override
	public TypeWrapper getComponentType() {
		// TODO should this be something else !?
		return null;
	}

}
