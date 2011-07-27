package org.stjs.generator.scope;

import java.io.File;
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
			// TODO check it's a method and not a field
			return new QualifiedName<NameType.MethodName>(imp, name, this);
		}

		for (String staticImport : this.staticStarImports) {
			try {
				Class<?> clazz = classLoader.loadClass(staticImport);
				for (Method m : clazz.getMethods()) {
					if (m.getName().equals(name)) {
						return new QualifiedName<NameType.MethodName>(imp, name, this);
					}
				}
			} catch (Exception e) {
				// not found
			}
		}

		if (getParent() != null) {
			return getParent().resolveMethod(pos, name, currentScope);
		}
		return null;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope) {
		String imp = findImport(staticExactImports, name);
		if (imp != null) {
			// TODO check it's a field and not a method
			return new QualifiedName<NameType.IdentifierName>(imp, name, this);
		}

		for (String staticImport : this.staticStarImports) {
			try {
				Class<?> clazz = classLoader.loadClass(staticImport);
				if (clazz.getField(name) != null) {
					return new QualifiedName<NameType.IdentifierName>(imp, name, this);
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

	@Override
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope) {
		Class<?> clazz = resolveClass(pos, name);
		if (clazz != null) {
			return new QualifiedName<NameType.TypeName>(null, clazz.getName(), this);
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
}
