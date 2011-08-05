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
import static org.stjs.generator.handlers.utils.Option.none;
import static org.stjs.generator.scope.path.QualifiedPath.withField;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.handlers.utils.Option;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;
import org.stjs.generator.scope.QualifiedName.NameTypes;
import org.stjs.generator.scope.path.QualifiedPath;
import org.stjs.generator.scope.path.QualifiedPath.QualifiedFieldPath;
import org.stjs.generator.scope.path.QualifiedPath.QualifiedMethodPath;

/**
 * This scope tries to solve the methods inside the static imports and the identifiers (that can be the name of a class)
 * in the imports, the current package and the default packages.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ImportScope extends NameScope {
	private final Map<String, Class<?>> resolvedClasses = new HashMap<String, Class<?>>();
	private final ClassLoader classLoader;

	private String currentPackage;
	private Set<String> exactImports = new LinkedHashSet<String>();
	private Set<String> staticExactImports = new LinkedHashSet<String>();
	private Set<String> starImports = new LinkedHashSet<String>();
	private Set<String> staticStarImports = new LinkedHashSet<String>();

	public ImportScope(File inputFile, NameScope parent, String currentPackage, ClassLoader classLoader) {
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
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope) {
	  String imp = findImport(staticExactImports, name);
		if (imp != null) {
		  Option<QualifiedName<MethodName>> qName = findMethodFromClassLoader(QualifiedPath.withMethod(imp));
      if (qName.isDefined()) {
        return qName.getOrThrow();
      }
		}
		

		for (String staticImport : this.staticStarImports) {
			Option<QualifiedName<MethodName>> qName = findMethodFromClassLoader(QualifiedPath.withMethod(staticImport));
			if (qName.isDefined()) {
			  return qName.getOrThrow();
			}
		}
		
		QualifiedName<IdentifierName> receiverField = resolveIdentifier(pos, QualifiedPath.beforeFirstDot(name));
		if (receiverField != null) {
		  // the expression can be field.subfield.subfield.
		  // unless we want to do the full resolution, let's return something basic here and assume fields and method exist
		  boolean isStatic = false; // it is a non static method class (on a static field!)
		  return new QualifiedName<NameType.MethodName>(this, isStatic, false, NameTypes.METHOD, receiverField.getDefinitionPoint().getOrNull(), receiverField.isGlobal());
		  
		}

		if (getParent() != null) {
			return getParent().resolveMethod(pos, name, currentScope);
		}
		return null;
	}
	
	private Option<QualifiedName<MethodName>> findMethodFromClassLoader(QualifiedMethodPath path) {
	  try {
      Class<?> clazz = classLoader.loadClass(path.getClassQualifiedName());
      for (Method m : clazz.getDeclaredMethods()) {
        if (m.getName().equals(path.getMethodName())) {
          return Option.some(new QualifiedName<NameType.MethodName>(this, true, false, NameTypes.METHOD, new JavaTypeName(path), isGlobal(clazz)));
        }
      }
    } catch (Exception e) {
      // not found
    }
    return none();
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope) {
		String imp = findImport(staticExactImports, name);
		if (imp != null) {
		  QualifiedFieldPath path = withField(imp);
		  try {
        Class<?> clazz = classLoader.loadClass(path.getClassQualifiedName());
        Field field = clazz.getDeclaredField(path.getFieldName());
        if (field != null) {
          return new QualifiedName<NameType.IdentifierName>(this, true, false, NameTypes.FIELD, new JavaTypeName(path), isGlobal(clazz));
        }
      } catch (Exception e) {
        // not found
      }
		}

		for (String staticImport : this.staticStarImports) {
			try {
				Class<?> clazz = classLoader.loadClass(staticImport);
				Field field = clazz.getField(name);
        if (field != null) {
					return new QualifiedName<NameType.IdentifierName>(this, true, false, NameTypes.FIELD, new JavaTypeName(clazz), isGlobal(clazz));
				}
			} catch (Exception e) {
				// not found
			}
		}

		// XXX: don't do anything more as it means it's a class - shall i resolve types as well!?
		// search for classes in the classpath for the star paths
		// search in the current package
		// search in java.lang

		if (getParent() != null) {
			return getParent().resolveIdentifier(pos, name, currentScope);
		}
		return null;
	}

  private boolean isGlobal(Class<?> clazz) {
    // TODO : cache?
    for (Annotation annote : clazz.getAnnotations()) {
      if (annote.annotationType().getName().equals("org.stjs.javascript.GlobalScope")) {
        return true;
      }
    }
    return false;
  }

	@Override
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope) {
		Class<?> clazz = resolveClass(pos, name);
		if (clazz != null) {
			return new QualifiedName<TypeName>(this, isStatic(clazz.getModifiers()), NameTypes.CLASS, new JavaTypeName(clazz));
		}
		return null;
	}

	@Override
	public String toString() {
		return "ImportScope [getChildren()=" + getChildren() + "]";
	}

	/**
	 * The name of the class is as it may appear in a source file (i.e. relative to one of the imports or absolute)
	 * 
	 * @param parentClassName
	 * @return
	 */
	public Class<?> resolveClass(SourcePosition pos, String className) {
		Class<?> resolvedClass = resolvedClasses.get(className);
		if (resolvedClass != null) {
			return resolvedClass;
		}

		// 1. try first full qualified
		try {
			resolvedClass = classLoader.loadClass(className);
			resolvedClasses.put(className, resolvedClass);
			return resolvedClass;
		} catch (ClassNotFoundException e) {
			// next
		}

		// 2. try in the current package
		if (currentPackage != null && currentPackage.length() > 0) {
			try {
				resolvedClass = classLoader.loadClass(currentPackage + "." + className);
				resolvedClasses.put(className, resolvedClass);
				return resolvedClass;
			} catch (ClassNotFoundException e) {
			  // ignore
			}
		}

		// 3. scan all the exact imports (TODO order may be important between star and not star!!)
		for (String imp : exactImports) {
			if (imp.endsWith("." + className)) {
				try {
					resolvedClass = classLoader.loadClass(imp);

					resolvedClasses.put(className, resolvedClass);
					return resolvedClass;
				} catch (ClassNotFoundException e) {
				  //ignore
				}
			}
		}
		// 4. scan all the star imports (TODO order may be important between star and not star!!)
		for (String imp : starImports) {
			try {
				resolvedClass = classLoader.loadClass(imp + "." + className);
				resolvedClasses.put(className, resolvedClass);
				return resolvedClass;
			} catch (ClassNotFoundException e) {
			  //ignore
			}
		}

		// 5. try java.lang
		try {
			resolvedClass = classLoader.loadClass("java.lang." + className);
			resolvedClasses.put(className, resolvedClass);
			return resolvedClass;
		} catch (ClassNotFoundException e) {
			// next
		}
		return null;
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
