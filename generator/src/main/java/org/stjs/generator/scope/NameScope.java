package org.stjs.generator.scope;

import static java.util.Collections.emptySet;
import static org.stjs.generator.handlers.utils.Sets.union;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;

/**
 * This class contains all the names defined in a given scope. If a name is search in the given scope and is not found
 * the name is searched in the parent scope.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
abstract public class NameScope {
	private final NameScope parent;

	private final List<NameScope> children = new ArrayList<NameScope>();

	private final String name;

	public NameScope(String name, NameScope parent) {
		this.name = name;
		this.parent = parent;
		if (this.parent != null) {
			this.parent.children.add(this);
		}
	}

	/**
	 * this is the name of the scope that will help identify the scope among its siblings
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the full name from the root in a dot-notation.
	 */
	public String getPath() {
		if (getParent() != null) {
			return getParent().getPath() + "." + name;
		}
		return name;
	}

	public QualifiedName<MethodName> resolveMethod(String name) {
		return resolveMethod(name, this);
	}

	public QualifiedName<IdentifierName> resolveIdentifier(String name) {
		return resolveIdentifier(name, this);
	}

	/**
	 * The subclasses should implement it.
	 * 
	 * @param name
	 * @return
	 */
	abstract protected QualifiedName<MethodName> resolveMethod(String name, NameScope currentScope);

	abstract protected QualifiedName<IdentifierName> resolveIdentifier(String name, NameScope currentScope);

	Set<QualifiedName<MethodName>> getOwnMethods() {
		return emptySet();
	}

	Set<QualifiedName<IdentifierName>> getOwnIdentifiers() {
		return emptySet();
	}

	public Set<QualifiedName<MethodName>> getMethods() {
		if (parent != null) {
			return union(parent.getMethods(), getOwnMethods());
		}
		return getOwnMethods();
	}

	public Set<QualifiedName<IdentifierName>> getIdentifiers() {
		if (parent != null) {
			return union(parent.getIdentifiers(), getOwnIdentifiers());
		}
		return getOwnIdentifiers();
	}

	public NameScope getParent() {
		return parent;
	}

	public List<NameScope> getChildren() {
		return children;
	}

	public void dump(String indent) {
		System.out.print(indent);
		System.out.println(getName());
		for (NameScope child : children) {
			child.dump(indent + "  ");
		}
	}

}
