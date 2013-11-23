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

import org.stjs.javascript.annotation.BrowserCompatibility;
import org.stjs.javascript.annotation.Template;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Callback3;
import org.stjs.javascript.functions.Function3;
import org.stjs.javascript.functions.Function4;

/**
 * This interface represents an array from Javascript.The value may be typed. The iteration is done on the indexes to
 * have the javascript equivalent of <br>
 * <b>for(var key in array)</b> <br>
 * The methods are prefixed with $ to let the generator know that is should generate braket access instead, i.e <br>
 * array.$get(key) => array[key] <br>
 * array.$set(key, value) => array[key]=value
 * 
 * The documentation of this class is mostly adapted from the ECMAScript 5.1 Specification:
 * http://www.ecma-international.org/ecma-262/5.1/ Browser compatibility information comes from:
 * http://kangax.github.io/es5-compat-table
 * 
 * @author acraciun, npiguet
 */
public class Array<V> implements Iterable<String> {

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

	/**
	 * Returns the element at the specified index in this <tt>Array</tt>.
	 * 
	 * @param index
	 *            the index
	 * @return the element a the specified index
	 */
	@Template("get")
	public V $get(int index) {
		if ((index < 0) || (index >= array.size())) {
			return null;
		}
		return array.get(index);
	}

	/**
	 * Returns the element at the specified index in this <tt>Array</tt>.
	 * 
	 * @param index
	 *            the index
	 * @return the element a the specified index
	 */
	@Template("get")
	public V $get(String index) {
		return $get(Integer.valueOf(index));
	}

	/**
	 * Sets the element at the specified index in this <tt>Array</tt> to the specified value.
	 * 
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 */
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

	/**
	 * Sets the element at the specified index in this <tt>Array</tt> to the specified value.
	 * 
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 */
	@Template("set")
	public void $set(String index, V value) {
		$set(Integer.valueOf(index), value);
	}

	/**
	 * Returns the length of this <tt>Array</tt>. The returned value is always greater than the highest index in this
	 * <tt>Array</tt>.
	 * 
	 * @return the length of this <tt>Array</tt>.
	 */
	@Template("toProperty")
	public int $length() {
		return array.size();
	}

	/**
	 * Sets the length of this <tt>Array</tt>.
	 * 
	 * <p>
	 * Attempting to set the length property of this <tt>Array</tt> to a value that is numerically less than or equal to
	 * the largest index contained within this <tt>Array</tt> will result in the length being set to a value that is one
	 * greater than that largest index contained within this <tt>Array</tt>.
	 * 
	 * @param newLength
	 *            the new length of this <tt>Array</tt>
	 */
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

	/**
	 * Appends all the elements of each of the specified <tt>Arrays</tt> in order to the elements of this <tt>Array</tt>
	 * . This method does not change the existing arrays, but returns a new array, containing the values of the joined
	 * arrays.
	 * 
	 * @param arrays
	 *            the <tt>Arrays</tt> to be concatenated to this <tt>Array</tt>
	 * @return a new <tt>Array</tt>, containing all the elements of the joined <tt>Arrays</tt>.
	 */
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

	/**
	 * Appends all the specified elements in the order they appear in the arguments list to the elements of this
	 * <tt>Array</tt>. This method does not change the existing arrays, but returns a new array, containing the values
	 * of the joined arrays.
	 * 
	 * @param values
	 *            the elements to be concatenated to this <tt>Array</tt>
	 * @return a new <tt>Array</tt>, containing all the elements of the joined <tt>Arrays</tt>.
	 */
	public Array<V> concat(V... values) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Searches this <tt>Array</tt> for the specified item, and returns its position. Items are compared for equality
	 * using the === operator.
	 * 
	 * <p>
	 * The search will start at the beginning and end at the end of the <tt>Array</tt>.
	 * 
	 * <p>
	 * Returns -1 if the item is not found.
	 * 
	 * <p>
	 * If the item is present more than once, <tt>indexOf</tt> returns the position of the first occurrence.
	 * 
	 * @param element
	 *            the item to search for
	 * @return the index at which the element was found, -1 if not found
	 */
	@BrowserCompatibility("IE:9+")
	public int indexOf(V element) {
		return array.indexOf(element);
	}

	/**
	 * Searches this <tt>Array</tt> for the specified item, and returns its position. Items are compared for equality
	 * using the === operator.
	 * 
	 * <p>
	 * The search will start at the specified position and end the search at the end of the <tt>Array</tt>.
	 * 
	 * <p>
	 * Returns -1 if the item is not found.
	 * 
	 * <p>
	 * If the specified position is greater than or equal to the length of this <tt>array</tt>, this <tt>Array</tt> is
	 * not searched and -1 is returned. If <tt>start</tt> is negative, it will be treated as <tt>length+start</tt>. If
	 * the computed starting element is less than 0, this whole <tt>Array</tt> will be searched.
	 * 
	 * <p>
	 * If the item is present more than once, <tt>indexOf</tt> method returns the position of the first occurrence.
	 * 
	 * @param element
	 *            the item to search for
	 * @param start
	 *            where to start the search. Negative values will start at the given position counting from the end
	 * @return the index at which the element was found, -1 if not found
	 */
	@BrowserCompatibility("IE:9+")
	public int indexOf(V element, int start) {
		int pos = array.subList(start, array.size()).indexOf(element);
		if (pos < 0) {
			return pos;
		}
		return pos + start;
	}

	/**
	 * Converts all of the elements of this <tt>Array</tt> to <tt>Strings</tt>, and concatenates these <tt>Strings</tt>
	 * using a single comma (",") as a separator.
	 * 
	 * <p>
	 * If this <tt>Array</tt> is empty, the empty string is returned.
	 * 
	 * @return the string representation of the values in this <tt>Array</tt>, separated by a comma.
	 */
	public String join() {
		return join(",");
	}

	/**
	 * Converts all of the elements of this <tt>Array</tt> to <tt>Strings</tt>, and concatenates these <tt>Strings</tt>
	 * using the specified separator.
	 * 
	 * <p>
	 * If this <tt>Array</tt> is empty, the empty string is returned.
	 * 
	 * @return the string representation of the values in this <tt>Array</tt>, separated by the specified separator.
	 */
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

	/**
	 * Removes the last element from this <tt>Array</tt> and returns it.
	 * 
	 * <p>
	 * If this <tt>Array</tt> is empty, <tt>undefined</tt> is returned.
	 * 
	 * @return the last element from this <tt>Array</tt>
	 */
	public V pop() {
		if (array.size() == 0) {
			return null;
		}
		return array.remove(array.size() - 1);
	}

	/**
	 * The specified elements are appended at the end of this <tt>Array</tt> in the order in which they appear in the
	 * arguments list and the new length of this <tt>Array</tt> is returned.
	 * 
	 * @param values
	 *            the values to be appended
	 * @return the new length of this <tt>Array</tt>
	 */
	public int push(V... values) {
		for (V value : values) {
			array.add(value);
		}
		return array.size();
	}

	/**
	 * The elements of this <tt>Array</tt> are rearranged so as to reverse their order, and this <tt>Array</tt> is
	 * returned.
	 * 
	 * @return this <tt>Array</tt>
	 */
	public Array<V> reverse() {
		Collections.reverse(array);
		return this;
	}

	/**
	 * Removes the first element from this <tt>Array</tt> and returns it.
	 * 
	 * <p>
	 * If this <tt>Array</tt> is empty, <tt>undefined</tt> is returned.
	 * 
	 * @return the first element of this <tt>Array</tt>.
	 */
	public V shift() {
		if (array.size() == 0) {
			return null;
		}
		return array.remove(0);
	}

	/**
	 * Returns a new <tt>Array</tt> containing all the elements of this <tt>Array</tt> starting from the given start
	 * index until the end of this <tt>Array</tt>.
	 * 
	 * <p>
	 * If <tt>start</tt> is negative, it is treated as <tt>length+start</tt>.
	 * 
	 * <p>
	 * If the selection range falls outside of this <tt>array</tt>, an empty <tt>Array</tt> is returned.
	 * 
	 * @param start
	 *            the index from which to start the selection. Use negative values to specified an index starting from
	 *            the end of this <tt>Array</tt>.
	 * @return a new <tt>Array</tt> containing all the elements of this <tt>Array</tt> starting from the given start
	 *         index until the end of the <tt>Array</tt>.
	 */
	public Array<V> slice(int start) {
		if (start < 0) {
			return slice(java.lang.Math.max(0, array.size() - start), array.size());
		}
		return slice(start, array.size());
	}

	/**
	 * Returns a new <tt>Array</tt> containing all the elements of this <tt>Array</tt> starting from the given start
	 * index and ending at (but not including) the given end index.
	 * 
	 * <p>
	 * If <tt>start</tt> is negative, it is treated as <tt>length+start</tt>. If <tt>end</tt> is negative, it is treated
	 * as <tt>length+end</tt>.
	 * 
	 * <p>
	 * If the selection range falls outside of this <tt>Array</tt>, an empty <tt>Array</tt> is returned.
	 * 
	 * @param start
	 *            the index from which to start the selection. Use negative values to specified an index starting from
	 *            the end
	 * @param end
	 *            the index at which to end the selection (the element at this index is excluded). Use negative values
	 *            to specified an index starting from the end
	 * @return a new <tt>Array</tt> containing all the elements of this <tt>Array</tt> starting from the given start
	 *         index and ending at the given end index
	 */
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

	/**
	 * Deletes the specified number of elements from this <tt>Array</tt> starting at specified start index, and returns
	 * a new <tt>Array</tt> containing the deleted elements (if any).
	 * 
	 * <p>
	 * If <tt>start</tt> is negative, it is treated as <tt>length+start</tt>.
	 * 
	 * @param start
	 *            the index at which to start deleting elements. Use negative values to specified an index starting from
	 *            the end
	 * @param deleteCount
	 *            the number of elements to be deleted
	 * @return a new <tt>Array</tt> containing the deleted elements (if any)
	 */
	public Array<V> splice(int start, int deleteCount) {
		Array<V> ret = new Array<V>();
		for (int i = 0; i < deleteCount; ++i) {
			if (start >= array.size()) {
				break;
			}
			ret.array.add(array.remove(start));
		}
		return ret;
	}

	/**
	 * Deletes the specified number of elements from this <tt>Array</tt> starting at specified start index, replaces the
	 * deleted elements with the specified values, and returns a new <tt>Array</tt> containing the deleted elements (if
	 * any).
	 * 
	 * <p>
	 * If <tt>start</tt> is negative, it is treated as <tt>length+start</tt>.
	 * 
	 * @param start
	 *            the index at which to start deleting elements. Use negative values to specify an index starting from
	 *            the end.
	 * @param deleteCount
	 *            the number of elements to be deleted
	 * @param values
	 *            the elements with which the deleted elements must be replaced
	 * @return a new <tt>Array</tt> containing the deleted elements (if any)
	 */
	public Array<V> splice(int start, int deleteCount, V... values) {
		Array<V> removed = splice(start, deleteCount);
		array.addAll(start, Arrays.asList(values));
		return removed;
	}

	/**
	 * Sorts the elements of this <tt>Array</tt> using the natural order of their string representations.
	 * 
	 * <p>
	 * The sort is not necessarily stable (that is, elements that compare equal do not necessarily remain in their
	 * original order).
	 * 
	 * <p>
	 * <tt>sort</tt> returns this <tt>Array</tt>.
	 * 
	 * <p>
	 * <strong>Note1:</strong> Because non-existent property values always compare greater than undefined property
	 * values, and undefined always compares greater than any other value, undefined property values always sort to the
	 * end of the result, followed by non-existent property values.
	 * 
	 * @return this array
	 */
	public Array<V> sort() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Sorts the elements of this <tt>Array</tt> using the specified SortFunction to determine ordering.
	 * 
	 * <p>
	 * The sort is not necessarily stable (that is, elements that compare equal do not necessarily remain in their
	 * original order).
	 * 
	 * <p>
	 * This methods return this <tt>Array</tt>.
	 * 
	 * <p>
	 * If the specified SortFunction is null, then this method is equivalent to <tt>sort()</tt>.
	 * 
	 * <p>
	 * If <tt>comparefn</tt> is not a consistent comparison function for the elements of this <tt>Array</tt>, the
	 * behaviour of sort is implementation-defined.
	 * 
	 * @param comparefn
	 *            a sort function that can compare elements of this <tt>Array</tt>.
	 * @return this <tt>Array</tt>
	 */
	public Array<V> sort(final SortFunction<V> comparefn) {
		Collections.sort(array, new Comparator<V>() {
			@Override
			public int compare(V a, V b) {
				return comparefn.$invoke(a, b);
			}
		});
		return this;
	}

	/**
	 * Prepends the specified values to the start of this <tt>Array</tt>, such that their order within this
	 * <tt>Array</tt> is the same as the order in which they appear in the argument list. Returns the new length of this
	 * <tt>Array</tt>.
	 * 
	 * @param values
	 *            the values to the prepended to the start of this <tt>Array</tt>
	 * @return the new length of this <tt>Array</tt>
	 */
	public int unshift(V... values) {
		array.addAll(0, Arrays.asList(values));
		return array.size();
	}

	/**
	 * Calls the specified callback function once for each element present in this <tt>Array</tt>, in ascending order.
	 * <tt>callbackfn</tt> is called only for elements of this <tt>Array</tt> which actually exist; it is not called for
	 * missing elements of this <tt>Array</tt>.
	 * 
	 * <p>
	 * <tt>callbackfn</tt> is called with the value of the element as its argument.
	 * 
	 * <p>
	 * <tt>forEach</tt> does not directly mutate this <tt>Array</tt> but this <tt>Array</tt> may be mutated by the calls
	 * to <tt>callbackfn</tt>.
	 * 
	 * <p>
	 * The range of elements processed by <tt>forEach</tt> is set before the first call to <tt>callbackfn</tt>. Elements
	 * which are appended to this <tt>Array</tt> after the call to <tt>forEach</tt> begins will not be visited by
	 * <tt>callbackfn</tt>. If existing elements of this <tt>Array</tt> are changed, their value as passed to callback
	 * will be the value at the time <tt>forEach</tt> visits them; elements that are deleted after the call to
	 * <tt>forEach</tt> begins and before being visited are not visited.
	 * 
	 * @param callbackfn
	 *            the callback function to be called for each element
	 */
	@BrowserCompatibility("IE:9+")
	public void forEach(Callback1<V> callbackfn) {
		for (V value : array) {
			callbackfn.$invoke(value);
		}
	}

	/**
	 * Calls the specified callback function once for each element present in this <tt>Array</tt>, in ascending order.
	 * <tt>callbackfn</tt> is called only for elements of this <tt>Array</tt> which actually exist; it is not called for
	 * missing elements of this <tt>Array</tt>.
	 * 
	 * <p>
	 * <tt>callbackfn</tt> is called with three arguments: the value of the element, the index of the element, and the
	 * <tt>Array</tt> being traversed (this <tt>Array</tt>).
	 * 
	 * <p>
	 * <tt>forEach</tt> does not directly mutate this <tt>Array</tt> but this <tt>Array</tt> may be mutated by the calls
	 * to <tt>callbackfn</tt>.
	 * 
	 * <p>
	 * The range of elements processed by <tt>forEach</tt> is set before the first call to <tt>callbackfn</tt>. Elements
	 * which are appended to this <tt>Array</tt> after the call to <tt>forEach</tt> begins will not be visited by
	 * <tt>callbackfn</tt>. If existing elements of this <tt>Array</tt> are changed, their value as passed to callback
	 * will be the value at the time <tt>forEach</tt> visits them; elements that are deleted after the call to
	 * <tt>forEach</tt> begins and before being visited are not visited.
	 * 
	 * @param callbackfn
	 *            the callback function to be called for each element
	 */
	@BrowserCompatibility("IE:9+")
	public void forEach(Callback3<V, Integer, Array<V>> callbackfn) {
		// TODO Auto-generated method stub

	}

	/**
	 * Converts all the elements of this <tt>Array</tt> to their String representation, concatenates them using a comma
	 * as a separator. Calling this method is equivalent to calling <tt>join()</tt>.
	 * 
	 * @return the string representation of the values in this <tt>Array</tt>, separated by a comma.
	 */
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

	/**
	 * Converts all the elements of this <tt>Array</tt> to Strings using their toLocaleString method, and concatenate
	 * the resulting Strings by a separator String that has been derived in an implementation-defined locale-specific
	 * way. The result of calling this function is intended to be analogous to the result of <tt>toString()</tt>, except
	 * that the result of this function is intended to be locale-specific.
	 * 
	 * @return a string representation of all the elements in this <tt>Array</tt>, separated by a locale-specific
	 *         separator
	 */
	public String toLocaleString() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Searches the elements of this <tt>Array</tt> in descending order for the specified element, and returns the index
	 * of the last position at which the element was found, or -1 if not found.
	 * 
	 * <p>
	 * Elements are compared for equality using the <tt>===</tt> operator.
	 * 
	 * <p>
	 * If there are multiple occurrences of the specified element in this <tt>Array</tt>, then the largest index among
	 * occurrences is returned.
	 * 
	 * @param searchElement
	 *            the element to search for
	 * @return the last index where this element is in this <tt>Array</tt>, or -1 if not found
	 */
	@BrowserCompatibility("IE:9+")
	public int lastIndexOf(V searchElement) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Searches the elements of this <tt>Array</tt> in descending order starting at the specified index for the
	 * specified element, and returns the index of the last index at which the element was found, or -1 if not found.
	 * 
	 * <p>
	 * Elements are compared for equality using the === operator.
	 * 
	 * <p>
	 * If there are multiple occurrences of the specified element in this <tt>Array</tt>, then the largest index among
	 * occurrences is returned.
	 * 
	 * <p>
	 * If <tt>fromIndex</tt> is greater than or equal to the length of this <tt>Array</tt>, the whole array will be
	 * searched. If <tt>fromIndex</tt> is negative, then it is treated as <tt>length+fromIndex</tt>. If the computed
	 * index is less than 0, -1 is returned.
	 * 
	 * @param searchElement
	 *            The element to search for
	 * @param fromIndex
	 *            the index from which to start searching
	 * @return the last index where this element is in this <tt>Array</tt>, or -1 if not found
	 */
	@BrowserCompatibility("IE:9+")
	public int lastIndexOf(V searchElement, int fromIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Returns <tt>true</tt> if the specified callback function returns <tt>true</tt> for every element in this
	 * <tt>Array</tt>.
	 * 
	 * <p>
	 * <tt>callbackfn</tt> is called on each element in ascending order until one of the calls returns <tt>false</tt>.
	 * Once <tt>callbackfn</tt> has returned false for the first time, the search in this <tt>Array</tt> is interrupted,
	 * <tt>callbackfn</tt> is never called on any more elements and <tt>every</tt> returns <tt>false</tt>. If
	 * <tt>callbackfn</tt> returned <tt>true</tt> for all the elements, then <tt>every</tt> returns <tt>true</tt>.
	 * <tt>callbackfn</tt> is called only for elements of this <tt>Array</tt> which actually exist; it is not called for
	 * missing elements of this <tt>Array</tt>.
	 * 
	 * <p>
	 * <tt>callbackfn</tt> has 3 arguments: the value of the element, the index of the element, and the <tt>Array</tt>
	 * being traversed (this <tt>Array</tt>).
	 * 
	 * <p>
	 * <tt>every</tt> does not directly mutate this <tt>Array</tt>, but this <tt>Array</tt> may be mutated by the calls
	 * to <tt>callbackfn</tt>.
	 * 
	 * <p>
	 * The range of elements processed by <tt>every</tt> is set before the first call to <tt>callbackfn</tt>. Elements
	 * which are appended to this <tt>Array</tt> after the call to <tt>every</tt> begins will not be visited by
	 * <tt>callbackfn</tt>. If existing elements of this <tt>Array</tt> are changed, their value as passed to
	 * <tt>callbackfn</tt> will be the value at the time <tt>every</tt> visits them; elements that are deleted after the
	 * call to <tt>every</tt> begins and before being visited are not visited. <tt>every</tt> acts like the "for all"
	 * quantifier in mathematics. In particular, for an empty <tt>Array</tt>, it returns <tt>true</tt>.
	 * 
	 * @param callbackfn
	 *            the callback function to call
	 * @return <tt>true</tt> if </tt>callbackfn</tt> returns <tt>true</tt> for ALL the elements in this <tt>Array</tt>,
	 *         <tt>false</tt> if not
	 */
	@BrowserCompatibility("IE:9+")
	public boolean every(Function3<V, Integer, Array<V>, Boolean> callbackfn) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Returns <tt>true</tt> if the specified callback function returns <tt>true</tt> for at least one element in this
	 * <tt>Array</tt>.
	 * 
	 * <p>
	 * <tt>some</tt> calls <tt>callbackfn</tt> once for each element present in this <tt>Array</tt>, in ascending order,
	 * until it finds one where <tt>callbackfn</tt> returns <tt>true</tt>. If such an element is found, <tt>some</tt>
	 * immediately returns <tt>true</tt>. Otherwise, <tt>some</tt> returns <tt>false</tt>. <tt>callbackfn</tt> is called
	 * only for elements of this <tt>Array</tt> which actually exist; it is not called for missing elements of this
	 * <tt>Array</tt>.
	 * 
	 * <p>
	 * <tt>callbackfn</tt> has three arguments: the value of the element, the index of the element, and the
	 * <tt>Array</tt> being traversed (this <tt>Array</tt>).
	 * 
	 * <tt>some</tt> does not directly mutate this <tt>Array</tt> but this <tt>Array</tt> may be mutated by the calls to
	 * <tt>callbackfn</tt>.
	 * 
	 * <p>
	 * The range of elements processed by <tt>some</tt> is set before the first call to <tt>callbackfn</tt>. Elements
	 * that are appended to this <tt>Array</tt> after the call to <tt>some</tt> begins will not be visited by
	 * <tt>callbackfn</tt>. If existing elements of this <tt>Array</tt> are changed, their value as passed to
	 * <tt>callbackfn</tt> will be the value at the time that <tt>some</tt> visits them; elements that are deleted after
	 * the call to <tt>some</tt> begins and before being visited are not visited. <tt>some</tt> acts like the "exists"
	 * quantifier in mathematics. In particular, for an empty <tt>Array</tt>, it returns <tt>false</tt>.
	 * 
	 * @param callbackfn
	 *            the callback function to call
	 * @return <tt>true</tt> if <tt>callbackfn</tt> returns <tt>true</tt> for at least one element in this
	 *         <tt>Array</tt>, <tt>false</tt> otherwise
	 */
	@BrowserCompatibility("IE:9+")
	public boolean some(Function3<V, Integer, Array<V>, Boolean> callbackfn) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Constructs a new <tt>Array</tt>, such that the value at each index in the new <tt>Array</tt> is the result of
	 * calling the specified callback function on the value at the corresponding index in this <tt>Array</tt>.
	 * 
	 * <p>
	 * <tt>map</tt> calls <tt>callbackfn</tt> once for each element in this <tt>Array</tt>, in ascending order, and
	 * constructs a new <tt>Array</tt> from the results. <tt>callbackfn</tt> is called only for elements of the array
	 * which actually exist; it is not called for missing elements of this <tt>Array</tt>.
	 * 
	 * <p>
	 * <tt>callbackfn</tt> has three arguments: the value of the element, the index of the element, and the
	 * <tt>Array</tt> being traversed (this <tt>Array</tt>).
	 * 
	 * <p>
	 * <tt>map</tt> does not directly mutate this <tt>Array</tt> but this <tt>Array</tt> may be mutated by the calls to
	 * <tt>callbackfn</tt>.
	 * 
	 * <p>
	 * The range of elements processed by <tt>map</tt> is set before the first call to <tt>callbackfn</tt>. Elements
	 * which are appended to this <tt>Array</tt> after the call to <tt>map</tt> begins will not be visited by
	 * <tt>callbackfn</tt>. If existing elements of this <tt>Array</tt> are changed, their value as passed to
	 * <tt>callbackfn</tt> will be the value at the time <tt>map</tt> visits them; elements that are deleted after the
	 * call to <tt>map</tt> begins and before being visited are not visited.
	 * 
	 * @param callbackfn
	 *            the callback used to create the elements of the new array
	 * @return a new <tt>Array</tt> containing new elements as returned by the specified callback function
	 */
	@BrowserCompatibility("IE:9+")
	public <T> Array<T> map(Function3<V, Integer, Array<V>, T> callbackfn) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Constructs a new <tt>Array</tt> containing only the elements of this <tt>Array</tt> for which the specified
	 * callback function returns <tt>true</tt>.
	 * 
	 * <p>
	 * <tt>filter</tt> calls <tt>callbackfn</tt> once for each element in this <tt>Array</tt>, in ascending order, and
	 * constructs a new <tt>Array</tt> of all the values for which <tt>callbackfn</tt> returns <tt>true</tt>.
	 * <tt>callbackfn</tt> is called only for elements of this <tt>Array</tt> which actually exist; it is not called for
	 * missing elements of this <tt>Array</tt>.
	 * 
	 * <p>
	 * <tt>callbackfn</tt> has three arguments: the value of the element, the index of the element, and the
	 * <tt>Array</tt> being traversed (this <tt>Array</tt>).
	 * 
	 * <p>
	 * <tt>filter</tt> does not directly mutate this <tt>Array</tt>, but this <tt>Array</tt> may be mutated by the calls
	 * to <tt>callbackfn</tt>.
	 * 
	 * <p>
	 * The range of elements processed by <tt>filter</tt> is set before the first call to <tt>callbackfn</tt>. Elements
	 * which are appended to this <tt>Array</tt> after the call to <tt>filter</tt> begins will not be visited by
	 * <tt>callbackfn</tt>. If existing elements of this <tt>Array</tt> are changed their value as passed to
	 * <tt>callbackfn</tt> will be the value at the time <tt>filter</tt> visits them; elements that are deleted after
	 * the call to <tt>filter</tt> begins and before being visited are not visited.
	 * 
	 * @param callbackfn
	 *            the callback function used to decide if an element should be appended to the new <tt>Array</tt>
	 * @return a new <tt>Array</tt> containing only the elements of this <tt>Array</tt> for which the specified callback
	 *         function returns <tt>true</tt>.
	 */
	@BrowserCompatibility("IE:9+")
	public Array<V> filter(Function3<V, Integer, Array<V>, Boolean> callbackfn) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Applies the specified function against an accumulator and each value of this <tt>Array</tt> omitting the first
	 * element (from left-to-right), as to reduce it to a single value.
	 * 
	 * <p>
	 * <tt>reduce</tt> calls the callback, as a function, once for each element present in this <tt>Array</tt> except
	 * for the first one, in ascending order.
	 * 
	 * <p>
	 * <tt>callbackfn</tt> has four arguments: the <tt>previousValue</tt> (or value from the previous call to
	 * <tt>callbackfn</tt>), the <tt>currentValue</tt> (value of the current element), the <tt>currentIndex</tt>, and
	 * the <tt>Array</tt> being traversed (this <tt>Array</tt>). The first time that callback is called (that is, on the
	 * second element element of this <tt>array</tt>) <tt>previousValue</tt> will be equal to the first value in this
	 * <tt>Array</tt> and <tt>currentValue</tt> will be equal to the second. It is a TypeError if this <tt>Array</tt>
	 * contains no elements.
	 * 
	 * <p>
	 * <tt>reduce</tt> does not directly mutate this <tt>Array</tt> but this <tt>Array</tt> may be mutated by the calls
	 * to <tt>callbackfn</tt>.
	 * 
	 * <p>
	 * The range of elements processed by <tt>reduce</tt> is set before the first call to <tt>callbackfn</tt>. Elements
	 * that are appended to this <tt>Array</tt> after the call to <tt>reduce</tt> begins will not be visited by
	 * <tt>callbackfn</tt>. If existing elements of this <tt>Array</tt> are changed, their value as passed to
	 * <tt>callbackfn</tt> will be the value at the time <tt>reduce</tt> visits them; elements that are deleted after
	 * the call to <tt>reduce</tt> begins and before being visited are not visited.
	 * 
	 * @param callbackfn
	 *            the function used to reduce the array to a single value
	 * @return a single value derived from calling the callback function on all the elements of this <tt>Array</tt>
	 */
	@BrowserCompatibility("IE:9+, Safari:4+, Opera:10.50+")
	public V reduce(Function4<V, V, Integer, Array<V>, V> callbackfn) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Applies the specified function against an accumulator and each value of this <tt>Array</tt> (from left-to-right)
	 * as to reduce it to a single value.
	 * 
	 * <p>
	 * <tt>reduce</tt> calls the callback, as a function, once for each element present in this <tt>Array</tt>, in
	 * ascending order.
	 * 
	 * <p>
	 * <tt>callbackfn</tt> has four arguments: the <tt>previousValue</tt> (or value from the previous call to
	 * <tt>callbackfn</tt>), the <tt>currentValue</tt> (value of the current element), the <tt>currentIndex</tt>, and
	 * the <tt>Array</tt> being traversed (this <tt>Array</tt>). The first time that callback is called
	 * <tt>previousValue</tt> will be equal to <tt>initialValue</tt> and <tt>currentValue</tt> will be equal to the
	 * first value in this <tt>Array</tt>.
	 * 
	 * <p>
	 * <tt>reduce</tt> does not directly mutate this <tt>Array</tt> but this <tt>Array</tt> may be mutated by the calls
	 * to <tt>callbackfn</tt>.
	 * 
	 * <p>
	 * The range of elements processed by <tt>reduce</tt> is set before the first call to <tt>callbackfn</tt>. Elements
	 * that are appended to this <tt>Array</tt> after the call to <tt>reduce</tt> begins will not be visited by
	 * <tt>callbackfn</tt>. If existing elements of this <tt>Array</tt> are changed, their value as passed to
	 * <tt>callbackfn</tt> will be the value at the time <tt>reduce</tt> visits them; elements that are deleted after
	 * the call to <tt>reduce</tt> begins and before being visited are not visited.
	 * 
	 * @param callbackfn
	 *            the function used to reduce this <tt>Array</tt> to a single value
	 * @return a single value derived from calling the callback function on all the elements of this <tt>Array</tt>
	 */
	@BrowserCompatibility("IE:9+, Safari:4+, Opera:10.50+")
	public <T> T reduce(Function4<T, V, Integer, Array<V>, T> callbackfn, T initialValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Applies the specified function against an accumulator and each value of this <tt>Array</tt> omitting the first
	 * element (from right-to-left), as to reduce it to a single value.
	 * 
	 * <p>
	 * <tt>reduceRight</tt> calls the callback, as a function, once for each element present in this <tt>Array</tt>
	 * except for the first one, in descending order.
	 * 
	 * <p>
	 * <tt>callbackfn</tt> has four arguments: the <tt>previousValue</tt> (or value from the previous call to
	 * <tt>callbackfn</tt>), the <tt>currentValue</tt> (value of the current element), the <tt>currentIndex</tt>, and
	 * the <tt>Array</tt> being traversed (this <tt>Array</tt>). The first time that callback is called (that is, on the
	 * second-to-last element element of this <tt>array</tt>) <tt>previousValue</tt> will be equal to the last value in
	 * this <tt>Array</tt> and <tt>currentValue</tt> will be equal to the second-to-last. It is a TypeError if this
	 * <tt>Array</tt> contains no elements.
	 * 
	 * <p>
	 * <tt>reduceRight</tt> does not directly mutate this <tt>Array</tt> but this <tt>Array</tt> may be mutated by the
	 * calls to <tt>callbackfn</tt>.
	 * 
	 * <p>
	 * The range of elements processed by <tt>reduceRight</tt> is set before the first call to <tt>callbackfn</tt>.
	 * Elements that are appended to this <tt>Array</tt> after the call to <tt>reduceRight</tt> begins will not be
	 * visited by <tt>callbackfn</tt>. If existing elements of this <tt>Array</tt> are changed, their value as passed to
	 * <tt>callbackfn</tt> will be the value at the time <tt>reduceRight</tt> visits them; elements that are deleted
	 * after the call to <tt>reduceRight</tt> begins and before being visited are not visited.
	 * 
	 * @param callbackfn
	 *            the function used to reduce the array to a single value
	 * @return a single value derived from calling the callback function on all the elements of this <tt>Array</tt>
	 */
	@BrowserCompatibility("IE:9+, Safari:4+, Opera:10.50+")
	public V reduceRight(Function4<V, V, Integer, Array<V>, V> callbackfn) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Applies the specified function against an accumulator and each value of this <tt>Array</tt> (from right-to-left)
	 * as to reduce it to a single value.
	 * 
	 * <p>
	 * <tt>reduceRight</tt> calls the callback, as a function, once for each element present in this <tt>Array</tt>, in
	 * descending order.
	 * 
	 * <p>
	 * <tt>callbackfn</tt> has four arguments: the <tt>previousValue</tt> (or value from the previous call to
	 * <tt>callbackfn</tt>), the <tt>currentValue</tt> (value of the current element), the <tt>currentIndex</tt>, and
	 * the <tt>Array</tt> being traversed (this <tt>Array</tt>). The first time that callback is called
	 * <tt>previousValue</tt> will be equal to <tt>initialValue</tt> and <tt>currentValue</tt> will be equal to the last
	 * value in this <tt>Array</tt>.
	 * 
	 * <p>
	 * <tt>reduceRight</tt> does not directly mutate this <tt>Array</tt> but this <tt>Array</tt> may be mutated by the
	 * calls to <tt>callbackfn</tt>.
	 * 
	 * <p>
	 * The range of elements processed by <tt>reduceRight</tt> is set before the first call to <tt>callbackfn</tt>.
	 * Elements that are appended to this <tt>Array</tt> after the call to <tt>reduceRight</tt> begins will not be
	 * visited by <tt>callbackfn</tt>. If existing elements of this <tt>Array</tt> are changed, their value as passed to
	 * <tt>callbackfn</tt> will be the value at the time <tt>reduceRight</tt> visits them; elements that are deleted
	 * after the call to <tt>reduceRight</tt> begins and before being visited are not visited.
	 * 
	 * @param callbackfn
	 *            the function used to reduce this <tt>Array</tt> to a single value
	 * @return a single value derived from calling the callback function on all the elements of this <tt>Array</tt>
	 */
	@BrowserCompatibility("IE:9+, Safari:4+, Opera:10.50+")
	public <T> T reduceRight(Function4<T, V, Integer, Array<V>, T> callbackfn, T initialValue) {
		// TODO Auto-generated method stub
		return null;
	}
}
