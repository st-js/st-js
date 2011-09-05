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
package org.stjs.generator.utils;

import static org.stjs.generator.utils.PreConditions.checkState;

import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Lists {

	public static <T> T getOnlyElement(Iterable<T> elems) {
		if (elems instanceof List<?>) {
			List<T> list = (List<T>) elems;
			checkState(list.size() == 1, "elems must contain exactly one element");
			return list.get(0);
		}
		Iterator<T> iterator = elems.iterator();
		checkState(iterator.hasNext(), "elems must not be empty");
		T obj = iterator.next();
		checkState(!iterator.hasNext(), "elems must contain exactly one element");
		return obj;
	}

	public static <T> List<T> union(final List<T> list1, final List<T> list2) {
		return new AbstractList<T>() {

			@Override
			public T get(int i) {
				if (i < list1.size()) {
					return list1.get(i);
				}
				return list2.get(i - list1.size());
			}

			@Override
			public int size() {
				return list1.size() + list2.size();
			}
		};
	}

	public static <T> List<T> append(List<T> coll, T appendee) {
		return union(coll, Collections.singletonList(appendee));
	}
}
