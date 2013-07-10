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

import org.stjs.generator.utils.Option;

public interface TypeWrapper {
	Type getType();

	Option<FieldWrapper> findField(String name);

	Option<MethodWrapper> findMethod(String name, TypeWrapper... paramTypes);

	/**
	 * @return the last part of the type's name (ex: the Class name)
	 */
	String getSimpleName();

	/**
	 * @return the full name of a class (package and outer classes included)
	 */
	String getName();

	/**
	 * @return the last part of the type's name (but outer classes simple name included - if any)
	 */
	String getExternalName();

	/**
	 * @return is this type can be used in an import declaration
	 */
	boolean isImportable();

	boolean isInnerType();

	boolean hasAnnotation(Class<? extends Annotation> class1);

	boolean isAssignableFrom(TypeWrapper typeWrapper);

	TypeWrapper getComponentType();

	TypeWrapper getSuperClass();

}
