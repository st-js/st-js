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

import static java.util.Collections.emptySet;
import static org.stjs.generator.handlers.utils.Sets.union;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;

/**
 * This class contains all the names defined in a given scope. If a name is search in the given scope and is not found
 * the name is searched in the parent scope.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
abstract public class NameScope {
	private final File inputFile;
	private final NameScope parent;

	private final List<NameScope> children = new ArrayList<NameScope>();

	private final String name;

	public NameScope(File inputFile, String name, NameScope parent) {
		this.name = name;
		this.parent = parent;
		if (this.parent != null) {
			this.parent.children.add(this);
		}
		this.inputFile = inputFile;
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

	public QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name) {
		return resolveMethod(pos, name, this);
	}

	public QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name) {
		return resolveIdentifier(pos, name, this);
	}

	public QualifiedName<TypeName> resolveType(SourcePosition pos, String name) {
		return resolveType(pos, name, this);
	}

	/**
	 * The subclasses should implement it.
	 * 
	 * @param name
	 * @return
	 */
	abstract protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name, NameScope currentScope);

	abstract protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name,
			NameScope currentScope);

	abstract protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name, NameScope currentScope);

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

	public File getInputFile() {
		return inputFile;
	}

	public void dump(String indent) {
		System.out.print(indent);
		System.out.println(getName());
		for (NameScope child : children) {
			child.dump(indent + "  ");
		}
	}

}
