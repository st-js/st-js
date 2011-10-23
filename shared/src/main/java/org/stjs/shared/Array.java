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
package org.stjs.shared;

import org.stjs.shared.functions.Callback1;

/**
 * This interface represents an array from Javascript.The value may be typed. The iteration is done on the indexes to
 * have the javascript equivalent of <br>
 * <b>for(var key in array)</b> <br>
 * The methods are prefixed with $ to let the generator know that is should generate braket access instead, i.e <br>
 * array.$get(key) => array[key] <br>
 * array.$set(key, value) => array[key]=value
 * 
 * @author acraciun
 */
public interface Array<V> extends Iterable<Integer> {
	public V $get(int index);

	public void $set(int index, V value);

	public int $length();

	public void $length(int newLength);

	public Array<V> concat(Array<V>... arrays);

	public int indexOf(V element);

	public int indexOf(V element, int start);

	public String join();

	public String join(String separator);

	public V pop();

	public int push(V... values);

	public void reverse();

	public V shift();

	public Array<V> slice(int start);

	public Array<V> slice(int start, int end);

	public Array<V> splice(int start);

	public Array<V> splice(int start, int howMany);

	public Array<V> splice(int start, int howMany, V... values);

	public void sort(SortFunction<V> function);

	public int unshift(V... values);

	public void forEach(Callback1<V> callback);
}
