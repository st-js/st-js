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
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * This class is used to arrange the list of all dependencies coming from the root classes, such that classes depend on
 * other classes found in the dependency list before they appear. This can be used for example to build the include list
 * of the scripts for a given html page. As in Java cyclic dependency is possible the list may be incorrect.
 * 
 * @author acraciun
 */
public class DependencyCollection {
	private final List<ClassWithJavascript> roots;

	public static final Comparator<Class<?>> DEPENDENCY_COMPARATOR = new Comparator<Class<?>>() {

		/**
		 * -1: if "b" or any child type of "b" (at any level) extends from "a" or any of "a"'s child type <br>
		 * 1: the other way around<br>
		 * 0: if none of the cases
		 * 
		 * @param a
		 * @param b
		 * @return
		 * @throws if
		 *             there is a situation where the method cannot decide weather it should return -1 or 1
		 */
		@Override
		public int compare(Class<?> a, Class<?> b) {
			return tryCompare(a, b, true);
		}

		private int tryCompare(Class<?> a, Class<?> b, boolean checkInverse) {
			if (a.equals(b)) {
				return 0;
			}
			int direct = getAssignationDirection(a, b);

			if (!checkInverse) {
				return direct;
			}
			// check both directions to see if there is any contradiction
			int inverse = tryCompare(b, a, false);

			if (direct == -1 && inverse == -1) {
				throw new IllegalArgumentException("Cannot decide the dependency order between the types:" + a + " and " + b);
			}
			if (direct != 0) {
				return direct;
			}
			return -inverse;
		}

		/**
		 * @return -1 of a (or any of the a declared classes is assignable from b), or 0 otherwise
		 */
		private int getAssignationDirection(Class<?> a, Class<?> b) {
			int direct = 0;
			if (isAssignableFromTypeOrChildTypes(a, b)) {
				direct = -1;
			} else {
				for (Class<?> child : a.getDeclaredClasses()) {
					if (isAssignableFromTypeOrChildTypes(child, b)) {
						direct = -1;
						break;
					}
				}
			}
			return direct;
		}

		private boolean isAssignableFromTypeOrChildTypes(Class<?> a, Class<?> b) {
			if (a.isAssignableFrom(b)) {
				return true;
			}
			for (Class<?> child : b.getDeclaredClasses()) {
				if (isAssignableFromTypeOrChildTypes(a, child)) {
					return true;
				}
			}
			return false;

		}
	};

	public DependencyCollection(List<ClassWithJavascript> roots) {
		this.roots = ImmutableList.copyOf(roots);
	}

	public DependencyCollection(ClassWithJavascript root) {
		this.roots = ImmutableList.of(root);
	}

	private int compareDeps(int withIndex, List<ClassWithJavascript> deps, Comparator<ClassWithJavascript> comparator) {
		for (int j = 0; j < deps.size(); ++j) {
			if (withIndex != j) {
				int cmp = comparator.compare(deps.get(withIndex), deps.get(j));
				if (cmp > 0) {
					return cmp;
				}
			}
		}
		return -1;
	}

	public List<ClassWithJavascript> orderAllDependencies(ClassLoader classLoader) {
		List<ClassWithJavascript> deps = new ArrayList<ClassWithJavascript>();
		Set<ClassWithJavascript> visited = new HashSet<ClassWithJavascript>();
		for (ClassWithJavascript root : roots) {
			visit(visited, new LinkedHashSet<ClassWithJavascript>(), deps, root);
		}

		Comparator<ClassWithJavascript> comparator = new ClassWithJavascriptComparator(classLoader);
		List<ClassWithJavascript> orderedDeps = new ArrayList<ClassWithJavascript>();
		while (!deps.isEmpty()) {
			// add to orderedDeps only the classes that have no "extends" dependency to any of the other from the
			// remaining list.
			// i.e. the result of the comparison is <= 0
			for (int i = 0; i < deps.size(); ++i) {
				if (compareDeps(i, deps, comparator) <= 0) {
					orderedDeps.add(deps.remove(i));
					i--;
				}
			}
		}

		return orderedDeps;
	}

	/**
	 * use topological sort to find the order of processing cells
	 */
	private void visit(Set<ClassWithJavascript> visited, Set<ClassWithJavascript> path, List<ClassWithJavascript> deps, ClassWithJavascript cj) {
		if (path.contains(cj)) {
			// cyclic dependency here
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

	@SuppressWarnings(
			value = "SE_COMPARATOR_SHOULD_BE_SERIALIZABLE", justification = "This comparator will not be used with Serializable lists")
	private static class ClassWithJavascriptComparator implements Comparator<ClassWithJavascript> {

		private final ClassLoader classLoader;

		public ClassWithJavascriptComparator(ClassLoader classLoader) {
			this.classLoader = classLoader;
		}

		@Override
		public int compare(ClassWithJavascript o1, ClassWithJavascript o2) {

			try {
				Class<?> c1 = classLoader.loadClass(o1.getClassName());
				Class<?> c2 = classLoader.loadClass(o2.getClassName());
				return DEPENDENCY_COMPARATOR.compare(c1, c2);
			}
			catch (ClassNotFoundException e) {
				throw new STJSRuntimeException(e);
			}
		}

	}
}
