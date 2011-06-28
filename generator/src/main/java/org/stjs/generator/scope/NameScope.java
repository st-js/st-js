package org.stjs.generator.scope;

import java.util.ArrayList;
import java.util.List;

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
	abstract public QualifiedName resolveMethod(String name, NameScope currentScope);

	abstract public QualifiedName resolveIdentifier(String name, NameScope currentScope);

	public NameScope getParent() {
		return parent;
	}

	public List<NameScope> getChildren() {
		return children;
	}

}
