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
package org.stjs.generator.scope;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.type.ClassWrapper;
import org.stjs.generator.type.FieldWrapper;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.TypeWrapper;

public class ClassScope extends AbstractScope {

	private final ClassWrapper clazz;

	ClassScope(ClassWrapper clazz, Scope parent, GenerationContext context) {
		super(parent, context);
		this.clazz = clazz;
		addClassElements(clazz);
	}

	private void addClassElements(ClassWrapper c) {
		for (TypeWrapper typeParam : c.getTypeParameters()) {
			addType(typeParam);
		}
		for (FieldWrapper field : c.getDeclaredFields()) {
			addField(field);
		}
		for (MethodWrapper method : c.getDeclaredMethods()) {
			addMethod(method);
		}
		for (TypeWrapper innerClass : c.getDeclaredClasses()) {
			addType(innerClass);
		}
	}

	@Override
	public <T> T apply(ScopeVisitor<T> visitor) {
		return visitor.apply(this);
	}

	public ClassWrapper getClazz() {
		return clazz;
	}

}
