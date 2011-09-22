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

import static japa.parser.ast.body.ModifierSet.isStatic;
import static org.stjs.generator.scope.path.QualifiedPath.withField;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;
import org.stjs.generator.scope.QualifiedName.NameTypes;
import org.stjs.generator.scope.classloader.ClassLoaderWrapper;
import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.generator.scope.path.QualifiedPath;
import org.stjs.generator.scope.path.QualifiedPath.QualifiedFieldPath;
import org.stjs.generator.scope.path.QualifiedPath.QualifiedMethodPath;
import org.stjs.generator.utils.Option;

/**
 * This scope tries to solve the methods inside the static imports and the identifiers (that can be the name of a class) in the imports, the
 * current package and the default packages.
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class ImportScope extends NameScope implements ClassResolver {
	private final Map<String, ClassWrapper> resolvedClasses = new HashMap<String, ClassWrapper>();
	private final ClassLoaderWrapper classLoader;

	private String currentPackage;
	private Set<String> exactImports = new LinkedHashSet<String>();
	private Set<String> staticExactImports = new LinkedHashSet<String>();
	private Set<String> starImports = new LinkedHashSet<String>();
	private Set<String> staticStarImports = new LinkedHashSet<String>();

	public ImportScope(File inputFile, NameScope parent, String currentPackage, ClassLoaderWrapper classLoader) {
		super(inputFile, "import", parent);
		this.currentPackage = currentPackage;
		this.classLoader = classLoader;
	}

	public void addImport(String importDecl, boolean isStatic, boolean isAsterisk) {
		if (isStatic) {
			if (isAsterisk) {
				this.staticStarImports.add(importDecl);
			} else {
				this.staticExactImports.add(importDecl);
			}
		} else {
			if (isAsterisk) {
				this.starImports.add(importDecl);
			} else {
				this.exactImports.add(importDecl);
			}
		}

	}

	private String findImport(Collection<String> imports, String name) {
		for (String imp : imports) {
			if (name.equals(imp) || imp.endsWith("." + name)) {
				return imp;
			}
		}
		return null;
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope, NameResolverVisitor visitor) {
		String imp = findImport(staticExactImports, name);
		if (imp != null) {
			QualifiedMethodPath path = QualifiedPath.withMethod(imp, this, visitor);
			Option<QualifiedName<MethodName>> qName = findMethodFromClassLoader(path);
			if (qName.isDefined()) {
				visitor.getResolvedImports().add(path.getClassQualifiedName());
				return qName.getOrThrow();
			}
		}

		for (String staticImport : this.staticStarImports) {
			QualifiedMethodPath path = QualifiedPath.withMethod(staticImport, this, visitor);
			Option<QualifiedName<MethodName>> qName = findMethodFromClassLoader(path);
			if (qName.isDefined()) {
				visitor.getResolvedImports().add(path.getClassQualifiedName());
				return qName.getOrThrow();
			}
		}

		String maybeReceiverClass = QualifiedPath.beforeFirstDot(name);
		if (maybeReceiverClass != null) {
			QualifiedName<IdentifierName> receiverField = resolveIdentifier(pos, maybeReceiverClass, this, visitor);
			if (receiverField != null) {
				// the expression can be field.subfield.subfield.
				// unless we want to do the full resolution, let's return something basic here and assume fields and
				// method exist
				boolean isStatic = false; // it is a non static method class (on a static field!)
				for (ClassWrapper loadedClass : classLoader.loadClass(maybeReceiverClass)) {
					visitor.getResolvedImports().add(loadedClass.getName());
				}

				return new QualifiedName<NameType.MethodName>(this, isStatic, false, NameTypes.METHOD, receiverField.getDefinitionPoint()
						.getOrNull(), receiverField.isGlobal(), false);
			}
		}

		if (getParent() != null) {
			return getParent().resolveMethod(pos, name, currentScope, visitor);
		}
		return null;
	}

	private Option<QualifiedName<MethodName>> findMethodFromClassLoader(QualifiedMethodPath path) {
		for (ClassWrapper clazz : classLoader.loadClass(path)) {
			for (Method m : clazz.getDeclaredMethods()) {
				if (m.getName().equals(path.getMethodName())) {
					return Option.some(new QualifiedName<NameType.MethodName>(this, true, false, NameTypes.METHOD, new JavaTypeName(path),
							isGlobal(clazz), isMockType(clazz)));
				}
			}
		}
		return Option.none();
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope,
			NameResolverVisitor visitor) {
		String imp = findImport(staticExactImports, name);
		if (imp != null) {
			QualifiedFieldPath path = withField(imp);
			for (ClassWrapper clazz : classLoader.loadClass(path.getClassQualifiedName())) {
				if (clazz.hasDeclaredField(path.getFieldName())) {
					return new QualifiedName<NameType.IdentifierName>(this, true, false, NameTypes.FIELD, new JavaTypeName(path),
							isGlobal(clazz), isMockType(clazz));
				}
			}
		}

		for (String staticImport : this.staticStarImports) {
			for (ClassWrapper clazz : classLoader.loadClass(staticImport)) {
				if (clazz.hasDeclaredField(name)) {
					return new QualifiedName<NameType.IdentifierName>(this, true, false, NameTypes.FIELD, new JavaTypeName(clazz),
							isGlobal(clazz), isMockType(clazz));
				}
			}
		}

		// XXX: don't do anything more as it means it's a class - shall i resolve types as well!?
		// search for classes in the classpath for the star paths
		// search in the current package
		// search in java.lang

		if (getParent() != null) {
			return getParent().resolveIdentifier(pos, name, currentScope, visitor);
		}
		return null;
	}

	private boolean isGlobal(ClassWrapper clazz) {
		// TODO : cache?
		for (Annotation annote : clazz.getAnnotations()) {
			if (annote.annotationType().getName().equals("org.stjs.javascript.GlobalScope")) {
				return true;
			}
		}
		return false;
	}

	private boolean isMockType(ClassWrapper clazz) {
		// TODO : cache?
		for (Annotation annote : clazz.getAnnotations()) {
			if (annote.annotationType().getName().equals("org.stjs.javascript.MockType")) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope, NameResolverVisitor visitor) {
		for (ClassWrapper clazz : resolveClass(name, visitor)) {
			return new QualifiedName<TypeName>(this, isStatic(clazz.getModifiers()), false, NameTypes.CLASS, new JavaTypeName(clazz), false,
					isMockType(clazz));
		}
		return null;
	}

	@Override
	public String toString() {
		return "ImportScope [getChildren()=" + getChildren() + "]";
	}

	/**
	 * The name of the class is as it may appear in a source file (i.e. relative to one of the imports or absolute)
	 * @param parentClassName
	 * @return
	 */
	public Option<ClassWrapper> resolveClass(String className, NameResolverVisitor visitor) {
		{
			// check in cache
			ClassWrapper resolvedClass = resolvedClasses.get(className);
			if (resolvedClass != null) {
				return Option.some(resolvedClass);
			}
		}

		// 1. try first full qualified
		for (ClassWrapper resolvedClass : classLoader.loadClass(className)) {
			resolvedClasses.put(className, resolvedClass);
			visitor.getResolvedImports().add(resolvedClass.getName());
			return Option.some(resolvedClass);
		}

		if (className.contains(".")) {
			// it's rather Class.InnerClass so the replace "." with "$"
			String parentClassName = QualifiedPath.beforeFirstDot(className);
			Option<ClassWrapper> parentClass = resolveClass(parentClassName, visitor);
			if (parentClass.isEmpty()) {
				return Option.none();
			}
			String searchedClassName = parentClass.getOrThrow().getName() + "$" + QualifiedPath.afterFirstDot(className).replace('.', '$');
			return resolveClass(searchedClassName, visitor);
		}

		// 2. try in the current package
		if ((currentPackage != null) && (currentPackage.length() > 0)) {
			for (ClassWrapper resolvedClass : classLoader.loadClass(currentPackage + "." + className)) {
				resolvedClasses.put(className, resolvedClass);
				visitor.getResolvedImports().add(resolvedClass.getName());
				return Option.some(resolvedClass);
			}
		}

		// 3. scan all the exact imports (TODO order may be important between star and not star!!)
		for (String imp : exactImports) {
			if (imp.endsWith("." + className)) {
				for (ClassWrapper resolvedClass : classLoader.loadClass(imp)) {
					resolvedClasses.put(className, resolvedClass);
					visitor.getResolvedImports().add(resolvedClass.getName());
					return Option.some(resolvedClass);
				}
			}
		}
		// 4. scan all the star imports (TODO order may be important between star and not star!!)
		for (String imp : starImports) {
			for (ClassWrapper resolvedClass : classLoader.loadClass(imp + "." + className)) {
				resolvedClasses.put(className, resolvedClass);
				visitor.getResolvedImports().add(resolvedClass.getName());
				return Option.some(resolvedClass);
			}
		}

		// 5. try java.lang
		for (ClassWrapper resolvedClass : classLoader.loadClass("java.lang." + className)) {
			resolvedClasses.put(className, resolvedClass);
			visitor.getResolvedImports().add(resolvedClass.getName());
			return Option.some(resolvedClass);
		}
		return Option.none();
	}

	@Override
	public <T> T visit(NameScopeVisitor<T> visitor) {
		return visitor.caseImportScope(this);
	}

	@Override
	public void visit(VoidNameScopeVisitor visitor) {
		visitor.caseImportScope(this);
	}
}
