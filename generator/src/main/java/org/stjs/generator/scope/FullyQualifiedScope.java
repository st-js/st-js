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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.handlers.utils.Option;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;
import org.stjs.generator.scope.QualifiedName.NameTypes;
import org.stjs.generator.scope.classloader.ClassLoaderWrapper;
import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.generator.scope.path.QualifiedPath;
import org.stjs.generator.scope.path.QualifiedPath.QualifiedFieldPath;
import org.stjs.generator.scope.path.QualifiedPath.QualifiedMethodPath;

/**
 * This scope tries to resolve only fully qualified names by looking in the classpath for the corresponding type.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class FullyQualifiedScope extends NameScope implements ClassResolver {
	// TODO : Use MapMaker for caching
	private final Map<String, Option<ClassWrapper>> resolvedClasses = new HashMap<String, Option<ClassWrapper>>();
	private final ClassLoaderWrapper classLoader;

	public FullyQualifiedScope(File inputFile, ClassLoaderWrapper classLoader) {
		super(inputFile, "root", null);
		this.classLoader = classLoader;
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope) {
		if (name.contains(".")) {
			QualifiedMethodPath path = QualifiedPath.withMethod(name, this);
			if (path != null) {
				for (ClassWrapper clazz : resolveClass(path.getClassQualifiedName())) {
					for (Method method : clazz.getDeclaredMethods()) {
						if (method.getName().equals(path.getMethodName())) {
							return new QualifiedName<NameType.MethodName>(this, true, NameTypes.METHOD,
									new JavaTypeName(clazz));
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope) {
		QualifiedFieldPath path = QualifiedPath.withField(name);
		String className = path.getClassQualifiedName();
		if (className != null) {
			for (ClassWrapper clazz : resolveClass(className)) {
				if (clazz.hasDeclaredField(path.getFieldName())) {
					return new QualifiedName<NameType.IdentifierName>(this, true, NameTypes.FIELD, new JavaTypeName(
							clazz));
				}
			}
		}
		return null;
	}

	@Override
	public Option<ClassWrapper> resolveClass(String className) {
		{
			Option<ClassWrapper> resolvedClass = resolvedClasses.get(className);
			if (resolvedClass != null) {
				return resolvedClass;
			}
		}
		Option<ClassWrapper> resolvedClass = classLoader.loadClass(className);
		resolvedClasses.put(className, resolvedClass);
		return resolvedClass;
	}

	@Override
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope) {
		for (ClassWrapper clazz : resolveClass(name)) {
			return new QualifiedName<TypeName>(this, true, NameTypes.CLASS, new JavaTypeName(clazz));
		}
		return null;
	}

	@Override
	public <T> T visit(NameScopeVisitor<T> visitor) {
		return visitor.caseFullyQualifiedScope(this);
	}

	@Override
	public void visit(VoidNameScopeVisitor visitor) {
		visitor.caseFullyQualifiedScope(this);
	}

}
