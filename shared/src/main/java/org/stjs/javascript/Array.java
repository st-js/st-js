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
package org.stjs.javascript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.stjs.javascript.annotation.Template;
import org.stjs.javascript.functions.Callback1;

/**
 * This interface represents an array from Javascript.The value may be typed. The iteration is done on the indexes to
 * have the javascript equivalent of <br>
 * <b>for(var key in array)</b> <br>
 * The methods are prefixed with $ to let the generator know that is should generate braket access instead, i.e <br>
 * array.$get(key) => array[key] <br>
 * array.$set(key, value) => array[key]=value
 * 
 * It is generally a bad idea for code written in ST-JS to create subclasses of Array, as that will not be translated
 * properly. However, it may be useful for some bridges.
 * 
 * @author acraciun
 */
public class Array<V> implements Iterable<String> {
	private final List<V> array = new ArrayList<V>();

	public Array() {
	}

	public Array(Number size) {
		this.$length(size.intValue());
	}

	@SuppressWarnings("unchecked")
	public Array(V first, V second, V... others) {
		this.push(first);
		this.push(second);
		this.push(others);
	}

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

	@Template("get")
	public V $get(int index) {
		if ((index < 0) || (index >= array.size())) {
			return null;
		}
		return array.get(index);
	}

	@Template("get")
	public V $get(String index) {
		return $get(Integer.valueOf(index));
	}

	@Template("set")
	public void $set(int index, V value) {
		if (index < 0) {
			return;
		}
		if (index >= array.size()) {
			$length(index + 1);
		}
		array.set(index, value);
	}

	@Template("set")
	public void $set(String index, V value) {
		$set(Integer.valueOf(index), value);
	}

	@Template("toProperty")
	public int $length() {
		return array.size();
	}

	@Template("toProperty")
	public void $length(int newLength) {
		if (newLength < array.size()) {
			splice(newLength, array.size() - newLength);
		} else {
			while (array.size() < newLength) {
				array.add(null);
			}
		}

	}

	public Array<V> concat(Array<V>... arrays) {
		Array<V> ret = new Array<V>();
		ret.array.addAll(this.array);
		for (Array<V> a : arrays) {
			for (int i = 0; i < a.$length(); ++i) {
				ret.array.add(a.$get(i));
			}
		}
		return ret;
	}

	public int indexOf(V element) {
		return array.indexOf(element);
	}

	public int indexOf(V element, int start) {
		int pos = array.subList(start, array.size()).indexOf(element);
		if (pos < 0) {
			return pos;
		}
		return pos + start;
	}

	public String join() {
		return join(",");
	}

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

	public V pop() {
		if (array.size() == 0) {
			return null;
		}
		return array.remove(array.size() - 1);
	}

	public int push(V... values) {
		for (V value : values) {
			array.add(value);
		}
		return array.size();
	}

	public void reverse() {
		Collections.reverse(array);
	}

	public V shift() {
		if (array.size() == 0) {
			return null;
		}
		return array.remove(0);
	}

	public Array<V> slice(int start) {
		if (start < 0) {
			return slice(java.lang.Math.max(0, array.size() - start), array.size());
		}
		return slice(start, array.size());
	}

	public Array<V> slice(int start, int end) {
		int s = start < 0 ? java.lang.Math.max(0, array.size() - start) : start;
		s = java.lang.Math.min(s, array.size());
		int e = java.lang.Math.min(end, array.size());
		if (s <= e) {
			return new Array<V>();
		}
		Array<V> ret = new Array<V>();
		ret.array.addAll(array.subList(s, e));
		return ret;
	}

	public Array<V> splice(int start) {
		return splice(start, array.size() - start);
	}

	public Array<V> splice(int start, int howMany) {
		Array<V> ret = new Array<V>();
		for (int i = 0; i < howMany; ++i) {
			if (start >= array.size()) {
				break;
			}
			ret.array.add(array.remove(start));
		}
		return ret;
	}

	public Array<V> splice(int start, int howMany, V... values) {
		Array<V> removed = splice(start, howMany);
		array.addAll(start, Arrays.asList(values));
		return removed;
	}

	public void sort(final SortFunction<V> function) {
		Collections.sort(array, new Comparator<V>() {
			@Override
			public int compare(V a, V b) {
				return function.$invoke(a, b);
			}
		});
	}

	public int unshift(V... values) {
		array.addAll(0, Arrays.asList(values));
		return array.size();
	}

	public void forEach(Callback1<V> callback) {
		for (V value : array) {
			callback.$invoke(value);
		}
	}

	public String toString() {
		// ArrayList.toString() look like this "[1, 2, 3, 4, 5]" (with spaces
		// between elements and enclosed in []
		// but in Array.toString() must actually look like "1,2,3,4,5" (no spaces,
		// no [])
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (V item : array) {
			if (!first) {
				builder.append(",");
			} else {
				first = false;
			}
			if (item == null) {
				builder.append("null");
			} else {
				builder.append(item.toString());
			}
		}
		return builder.toString();
	}
}
