package org.stjs.generator.scope;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;

/**
 * This scope is for fields and methods inherited from a parent type.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class ParentTypeScope extends NameScope {
	private Class<?> parentClass;
	private final String parentClassName;

	public ParentTypeScope(NameScope parent, String parentClassName) {
		super("parent-" + parentClassName, parent);
		this.parentClassName = parentClassName;
	}

	private ImportScope getImportScope() {
		NameScope s = getParent();
		while (s != null) {
			if (s instanceof ImportScope) {
				return ((ImportScope) s);
			}
			s = s.getParent();
		}
		throw new JavascriptGenerationException(null, "No import scope found in the hierarchy of parent scope");
	}

	@Override
	protected QualifiedName<MethodName> resolveMethod(String name, NameScope currentScope) {
		if (parentClass == null) {
			parentClass = getImportScope().resolveClass(parentClassName);
		}
		Method method = getAccesibleMethod(parentClass, name);
		if (method != null) {
			return new QualifiedName<NameType.MethodName>(TypeScope.THIS_SCOPE, name, this);
		}
		if (getParent() != null) {
			return getParent().resolveMethod(name, currentScope);
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
	protected QualifiedName<IdentifierName> resolveIdentifier(String name, NameScope currentScope) {
		if (parentClass == null) {
			parentClass = getImportScope().resolveClass(parentClassName);
		}
		Field method = getAccesibleField(parentClass, name);
		if (method != null) {
			return new QualifiedName<NameType.IdentifierName>(TypeScope.THIS_SCOPE, name, this);
		}
		if (getParent() != null) {
			return getParent().resolveIdentifier(name, currentScope);
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
	public String toString() {
		return "ParentTypeScope [className=" + parentClass.getName() + ", getChildren()=" + getChildren() + "]";
	}
}
