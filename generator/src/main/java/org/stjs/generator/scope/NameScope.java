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

	public NameScope(NameScope parent) {
		this.parent = parent;
		if (this.parent != null) {
			this.parent.children.add(this);
		}
	}

	/**
	 * The subclasses should implement it.
	 * 
	 * @param name
	 * @return
	 */
	abstract public QualifiedName<MethodName> resolveMethod(String name, NameScope currentScope);

	abstract public QualifiedName<IdentifierName> resolveIdentifier(String name, NameScope currentScope);
	
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

	

}
