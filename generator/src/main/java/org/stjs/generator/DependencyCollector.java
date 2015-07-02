/**
 * Copyright 2011 Alexandru Craciun, Eyal Kaspi
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.stjs.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * This class is used to arrange the list of all dependencies coming from the root classes, such that classes depend on
 * other classes found in the dependency list before they appear. This can be used for example to build the include list
 * of the scripts for a given html page. As in Java cyclic dependency is possible the list may be incorrect.
 *
 * @author acraciun
 */
public class DependencyCollector {

	private final DependencyComparator dependencyComparator = new DependencyComparator();
	private final ClassWithJavascriptComparator classWithJsComparator = new ClassWithJavascriptComparator(dependencyComparator);

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

	public List<ClassWithJavascript> orderAllDependencies(ClassWithJavascript root) {
		return orderAllDependencies(Collections.singletonList(root));
	}

	public List<ClassWithJavascript> orderAllDependencies(List<ClassWithJavascript> roots) {
		List<ClassWithJavascript> deps = new ArrayList<>();
		Set<ClassWithJavascript> visited = new HashSet<>();
		for (ClassWithJavascript root : roots) {
			visit(visited, new LinkedHashSet<ClassWithJavascript>(), deps, root);
		}

		List<ClassWithJavascript> orderedDeps = new ArrayList<>();
		while (!deps.isEmpty()) {
			// add to orderedDeps only the classes that have no "extends" dependency to any of the other from the
			// remaining list.
			// i.e. the result of the comparison is <= 0
			for (int i = 0; i < deps.size(); ++i) {
				if (compareDeps(i, deps, classWithJsComparator) <= 0) {
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

	@SuppressWarnings(//
			value = "SE_COMPARATOR_SHOULD_BE_SERIALIZABLE", //
			justification = "This comparator will not be used with Serializable lists" //
	)
	public static class DependencyComparator implements Comparator<Class<?>> {

		/**
		 * Calling getDeclaredClasses repeatedly is very expensive. We'll just cache the result of this
		 * for the lifetime of this DependencyComparator instance. Don't create static instances of this class
		 * or you will be leaking ClassLoaders.
		 */
		private final ConcurrentHashMap<Class<?>, Class<?>[]> declaredClasses = new ConcurrentHashMap<>();

		/**
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
				for (Class<?> child : getDeclaredClasses(a)) {
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
			for (Class<?> child : getDeclaredClasses(b)) {
				if (isAssignableFromTypeOrChildTypes(a, child)) {
					return true;
				}
			}
			return false;

		}

		private Class<?>[] getDeclaredClasses(Class<?> clazz) {
			Class<?>[] classes = declaredClasses.get(clazz);
			if (classes == null) {
				classes = clazz.getDeclaredClasses();
				Class<?>[] original = declaredClasses.putIfAbsent(clazz, classes);
				if (original != null) {
					return original;
				}
			}
			return classes;
		}
	}

	@SuppressWarnings(//
			value = "SE_COMPARATOR_SHOULD_BE_SERIALIZABLE", //
			justification = "This comparator will not be used with Serializable lists" //
	)
	static class ClassWithJavascriptComparator implements Comparator<ClassWithJavascript> {

		private final DependencyComparator dependencyComparator;

		ClassWithJavascriptComparator(DependencyComparator dependencyComparator) {
			this.dependencyComparator = dependencyComparator;
		}

		@Override
		public int compare(ClassWithJavascript o1, ClassWithJavascript o2) {
			return dependencyComparator.compare(o1.getJavaClass(), o2.getJavaClass());
		}
	}
}
