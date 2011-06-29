package org.stjs.generator.scope;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;

/**
 * This scope tries to solve the methods inside the static imports and the identifiers (that can be the name of a class)
 * in the imports, the current package and the default packages.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ImportScope extends NameScope {
	private String currentPackage;
	private Set<String> exactImports = new LinkedHashSet<String>();
	private Set<String> staticExactImports = new LinkedHashSet<String>();
	private Set<String> starImports = new LinkedHashSet<String>();
	private Set<String> staticStarImports = new LinkedHashSet<String>();

	public ImportScope(NameScope parent, String currentPackage) {
		super("import", parent);
		this.currentPackage = currentPackage;
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
			if (name.equals(imp) || name.endsWith("." + name)) {
				return imp;
			}
		}
		return null;
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(String name, NameScope currentScope) {
		String imp = findImport(exactImports, name);
		if (imp != null) {
			return new QualifiedName<NameType.MethodName>(imp, name, this);
		}
		imp = findImport(staticExactImports, name);
		if (imp != null) {
			return new QualifiedName<NameType.MethodName>(imp, name, this);
		}
		// search for classes in the classpath for the star paths

		// search in the current package

		// search in java.lang
		if (getParent() != null) {
			return getParent().resolveMethod(name, currentScope);
		}
		return null;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(String name, NameScope currentScope) {
		if (getParent() != null) {
			return getParent().resolveIdentifier(name, currentScope);
		}
		return null;
	}

	@Override
	public String toString() {
		return "ImportScope [getChildren()=" + getChildren() + "]";
	}

}
