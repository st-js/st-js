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
package org.stjs.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.stjs.javascript.Array;
import org.stjs.javascript.SortFunction;
import org.stjs.javascript.functions.Callback1;

/**
 * This class implements the {@link Array} interface to be used on the server side.
 * @author acraciun
 * @param <V>
 */
public class ArrayImpl<V> implements Array<V> {
	private final List<V> array = new ArrayList<V>();

	@Override
	public Iterator<String> iterator() {
		return new Iterator<String>() {
			private int current = 0;

			@Override
			public boolean hasNext() {
				return current < array.size();
			}

			@Override
			public String next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return Integer.toString(current++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public V $get(int index) {
		if ((index < 0) || (index >= array.size())) {
			return null;
		}
		return array.get(index);
	}

	@Override
	public V $get(String index) {
		return $get(Integer.valueOf(index));
	}

	@Override
	public void $set(int index, V value) {
		if (index < 0) {
			return;
		}
		if (index >= array.size()) {
			$length(index + 1);
		}
		array.set(index, value);
	}

	public void $set(String index, V value) {
		$set(Integer.valueOf(index), value);
	}

	@Override
	public int $length() {
		return array.size();
	}

	@Override
	public void $length(int newLength) {
		if (newLength < array.size()) {
			splice(newLength, array.size() - newLength);
		} else {
			while (array.size() < newLength) {
				array.add(null);
			}
		}

	}

	@Override
	public Array<V> concat(Array<V>... arrays) {
		ArrayImpl<V> ret = new ArrayImpl<V>();
		for (Array<V> a : arrays) {
			for (int i = 0; i < a.$length(); ++i) {
				ret.array.add(a.$get(i));
			}
		}
		return ret;
	}

	@Override
	public int indexOf(V element) {
		return array.indexOf(element);
	}

	@Override
	public int indexOf(V element, int start) {
		int pos = array.subList(start, array.size()).indexOf(element);
		if (pos < 0) {
			return pos;
		}
		return pos + start;
	}

	@Override
	public String join() {
		return join(",");
	}

	@Override
	public String join(String separator) {
		StringBuilder sb = new StringBuilder();
		for (V value : array) {
			if (sb.length() != 0) {
				sb.append(separator);
			}
			sb.append(value != null ? value.toString() : "");
		}
		return sb.toString();
	}

	@Override
	public V pop() {
		if (array.size() == 0) {
			return null;
		}
		return array.remove(array.size() - 1);
	}

	@Override
	public int push(V... values) {
		for (V value : values) {
			array.add(value);
		}
		return array.size();
	}

	@Override
	public void reverse() {
		Collections.reverse(array);
	}

	@Override
	public V shift() {
		if (array.size() == 0) {
			return null;
		}
		return array.remove(0);
	}

	@Override
	public Array<V> slice(int start) {
		if (start < 0) {
			return slice(java.lang.Math.max(0, array.size() - start), array.size());
		}
		return slice(start, array.size());
	}

	@Override
	public Array<V> slice(int start, int end) {
		int s = start < 0 ? java.lang.Math.max(0, array.size() - start) : start;
		s = java.lang.Math.min(s, array.size());
		int e = java.lang.Math.min(end, array.size());
		if (s <= e) {
			return new ArrayImpl<V>();
		}
		ArrayImpl<V> ret = new ArrayImpl<V>();
		ret.array.addAll(array.subList(s, e));
		return ret;
	}

	@Override
	public Array<V> splice(int start) {
		return splice(start, 1);
	}

	@Override
	public Array<V> splice(int start, int howMany) {
		ArrayImpl<V> ret = new ArrayImpl<V>();
		for (int i = 0; i < howMany; ++i) {
			if (start >= array.size()) {
				break;
			}
			ret.array.add(array.remove(start));
		}
		return ret;
	}

	@Override
	public Array<V> splice(int start, int howMany, V... values) {
		Array<V> removed = splice(start, howMany);
		array.addAll(start, Arrays.asList(values));
		return removed;
	}

	@Override
	public void sort(final SortFunction<V> function) {
		Collections.sort(array, new Comparator<V>() {
			@Override
			public int compare(V a, V b) {
				return function.$invoke(a, b);
			}
		});
	}

	@Override
	public int unshift(V... values) {
		array.addAll(0, Arrays.asList(values));
		return array.size();
	}

	@Override
	public void forEach(Callback1<V> callback) {
		for (V value : array) {
			callback.$invoke(value);
		}
	}

}
