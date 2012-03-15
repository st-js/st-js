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
package org.stjs.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;

/**
 * This class is used to arrange the list of all dependencies coming from the root classes, such that classes depend on
 * other classes found in the dependency list before they appear. This can be used for example to build the include list
 * of the scripts for a given html page. As in Java cyclic dependency is possible the list may be incorrect.
 * 
 * @author acraciun
 * 
 */
public class DependencyCollection {
	private final List<ClassWithJavascript> roots;

	public DependencyCollection(List<ClassWithJavascript> roots) {
		this.roots = ImmutableList.copyOf(roots);
	}

	public DependencyCollection(ClassWithJavascript root) {
		this.roots = ImmutableList.of(root);
	}

	public List<ClassWithJavascript> orderAllDependencies() {
		List<ClassWithJavascript> deps = new ArrayList<ClassWithJavascript>();
		Set<ClassWithJavascript> visited = new HashSet<ClassWithJavascript>();
		for (ClassWithJavascript root : roots) {
			visit(visited, new LinkedHashSet<ClassWithJavascript>(), deps, root);
		}

		return deps;
	}

	/**
	 * use topological sort to find the order of processing cells
	 */
	private void visit(Set<ClassWithJavascript> visited, Set<ClassWithJavascript> path, List<ClassWithJavascript> deps,
			ClassWithJavascript cj) {
		if (path.contains(cj)) {
			// cyclic dependency here - XXX how to solve it
			System.out.println("WARN: cyclic dependency " + cj + " already visited in path:" + path);
			System.out.println("VISITED:" + deps);
			return;
		}
		if (!visited.contains(cj)) {
			visited.add(cj);
			path.add(cj);

			for (ClassWithJavascript dep : cj.getDirectDependencies()) {
				visit(visited, path, deps, dep);
				path.remove(dep);
			}

			deps.add(cj);

		}
	}
}
