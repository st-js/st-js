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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;

/**
 * This scope tries to resolve only fully qualified names by looking in the classpath for the corresponding type.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class FullyQualifiedScope extends NameScope {
	// special marker for classes not found - to not search again with the same name
	private static final Class<?> NOT_FOUND_CLASS = new Object() {
	}.getClass();

	private final Map<String, Class<?>> resolvedClasses = new HashMap<String, Class<?>>();
	private final ClassLoader classLoader;

	public FullyQualifiedScope(File inputFile, ClassLoader classLoader) {
		super(inputFile, "root", null);
		this.classLoader = classLoader;
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope) {
		return null;
	}

	private String getClassName(String field) {
		int pos = field.lastIndexOf('.');
		if (pos >= 0) {
			return field.substring(0, pos);
		}
		return field;
	}

	private String getFieldName(String field) {
		int pos = field.lastIndexOf('.');
		if (pos >= 0) {
			return field.substring(pos + 1);
		}
		return field;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope) {
		String className = getClassName(name);
		String fieldName = getFieldName(name);
		Class<?> clazz = resolveClass(className);
		if (clazz != null) {
			return new QualifiedName<NameType.IdentifierName>(className, fieldName, this);
		}
		return null;
	}

	private Class<?> resolveClass(String className) {
		Class<?> resolvedClass = resolvedClasses.get(className);
		if (resolvedClass != null) {
			return resolvedClass != NOT_FOUND_CLASS ? resolvedClass : null;
		}
		try {
			resolvedClass = classLoader.loadClass(className);
			resolvedClasses.put(className, resolvedClass);
			return resolvedClass;
		} catch (ClassNotFoundException e) {
			// next
			resolvedClasses.put(className, NOT_FOUND_CLASS);
			return null;
		}
	}

	@Override
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope) {
		Class<?> clazz = resolveClass(name);
		if (clazz != null) {
			return new QualifiedName<NameType.TypeName>(null, clazz.getName(), this);
		}
		return null;
	}

}
