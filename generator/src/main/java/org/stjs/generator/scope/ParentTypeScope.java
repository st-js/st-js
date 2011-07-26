package org.stjs.generator.scope;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;

/**
 * This scope is for fields and methods inherited from a parent type.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ParentTypeScope extends NameScope {
	private Class<?> parentClass;
	private final String parentClassName;

	public ParentTypeScope(File inputFile, NameScope parent, String parentClassName) {
		super(inputFile, "parent-" + parentClassName, parent);
		this.parentClassName = parentClassName;
	}

	private ImportScope getImportScope(SourcePosition pos) {
		NameScope s = getParent();
		while (s != null) {
			if (s instanceof ImportScope) {
				return ((ImportScope) s);
			}
			s = s.getParent();
		}
		throw new JavascriptGenerationException(getInputFile(), pos,
				"No import scope found in the hierarchy of parent scope");
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope) {
		if (parentClass == null) {
			parentClass = getImportScope(pos).resolveClass(pos, parentClassName);
		}
		Method method = getAccesibleMethod(parentClass, name);
		if (method != null) {
			if (TypeScope.isInCurrentTypeScope(this, currentScope)) {
				return new QualifiedName<MethodName>(TypeScope.THIS_SCOPE, name, this);
			}
			return new QualifiedName<MethodName>(TypeScope.OUTER_SCOPE, name, this);
		}
		if (getParent() != null) {
			return getParent().resolveMethod(pos, name, currentScope);
		}
		return null;
	}

	/**
	 * looks for a method with a given name (no full method signature given) in the given class the can be safely
	 * accessed from a child class
	 * 
	 * @param parentClass2
	 * @param name
	 * @return
	 */
	private Method getAccesibleMethod(Class<?> parentClass, String name) {
		for (Method m : parentClass.getDeclaredMethods()) {
			if (m.getName().equals(name) && !Modifier.isPrivate(m.getModifiers())) {
				return m;
			}
		}
		if (parentClass.getSuperclass() != null) {
			return getAccesibleMethod(parentClass.getSuperclass(), name);
		}
		return null;
	}

	@Override
	protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name, NameScope currentScope) {
		if (parentClass == null) {
			parentClass = getImportScope(pos).resolveClass(pos, parentClassName);
		}
		Field field = getAccesibleField(parentClass, name);
		if (field != null) {
			if (TypeScope.isInCurrentTypeScope(this, currentScope)) {
				return new QualifiedName<IdentifierName>(TypeScope.THIS_SCOPE, name, this);
			}
			return new QualifiedName<IdentifierName>(TypeScope.OUTER_SCOPE, name, this);
		}
		if (getParent() != null) {
			return getParent().resolveIdentifier(pos, name, currentScope);
		}
		return null;
	}

	/**
	 * looks for a method with a given name (no full method signature given) in the given class the can be safely
	 * accessed from a child class
	 * 
	 * @param parentClass2
	 * @param name
	 * @return
	 */
	private Field getAccesibleField(Class<?> parentClass, String name) {
		try {
			Field f = parentClass.getDeclaredField(name);
			if (!Modifier.isPrivate(f.getModifiers())) {
				return f;
			}
		} catch (Exception e) {
			// do nothing - search further on
		}

		if (parentClass.getSuperclass() != null) {
			return getAccesibleField(parentClass.getSuperclass(), name);
		}
		return null;
	}

	@Override
	protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope) {
		if (getParent() != null) {
			return getParent().resolveType(pos, name, currentScope);
		}
		return null;
	}

	@Override
	public String toString() {
		return "ParentTypeScope [className=" + parentClass.getName() + ", getChildren()=" + getChildren() + "]";
	}
}
