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
package org.stjs.generator.handlers.utils;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Sets {

	public static <T, U> Set<U> transform(final Set<T> set, final Function<? super T, ? extends U> function) {
		return new AbstractSet<U>() {

			@Override
			public int size() {
				return set.size();
			}

			@Override
			public Iterator<U> iterator() {
				return new Iterator<U>() {

					Iterator<T> it = set.iterator();

					@Override
					public boolean hasNext() {
						return it.hasNext();
					}

					@Override
					public U next() {
						return function.apply(it.next());
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	public static <T> Set<T> union(final Set<T> set1, final Set<T> set2) {
		return new AbstractSet<T>() {

			@Override
			public Iterator<T> iterator() {
				return new Iterator<T>() {

					Iterator<T> it1 = set1.iterator();
					Iterator<T> it2 = set2.iterator();

					@Override
					public boolean hasNext() {
						return it1.hasNext() || it2.hasNext();
					}

					@Override
					public T next() {
						if (it1.hasNext()) {
							return it1.next();
						}
						return it2.next();
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

			@Override
			public int size() {
				return set1.size() + set2.size();
			}

		};
	}

	public static <T> Set<T> newHashSet(T... elems) {
		return new HashSet<T>(Arrays.asList(elems));
	}
}
