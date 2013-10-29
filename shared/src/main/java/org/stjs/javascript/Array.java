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
 * @author acraciun
 */
public interface Array<V> extends Iterable<String> {
	@Template("get")
	public V $get(int index);

	@Template("get")
	public V $get(String index);

	@Template("set")
	public void $set(int index, V value);

	@Template("set")
	public void $set(String index, V value);

	@Template("toProperty")
	public int $length();

	@Template("toProperty")
	public void $length(int newLength);

	public Array<V> concat(Array<V>... arrays);

	public int indexOf(V element);

	public int indexOf(V element, int start);

	public String join();

	public String join(String separator);

	public V pop();

	public int push(V... values);

	public Array<V> reverse();

	public V shift();

	public Array<V> slice(int start);

	public Array<V> slice(int start, int end);

	public Array<V> splice(int start, int deleteCount);

	public Array<V> splice(int start, int deleteCount, V... values);

	public Array<V> sort();
	
	public Array<V> sort(SortFunction<V> comparefn);

	public int unshift(V... values);

	public void forEach(Callback1<V>  callbackfn);
	
	public void forEach(Callback3<V, Integer, Array<V>> callbackfn);
	
	public String toString();
	
	public String toLocaleString();
	
	public int lastIndexOf(V searchElement);
	
	public int lastIndexOf(V searchElement, int fromIndex);
	
	public boolean every(Function3<V, Integer, Array<V>, Boolean> callbackfn);
	
	public boolean some(Function3<V, Integer, Array<V>, Boolean> callbackfn);
	
	public <T> Array<T> map(Function3<V, Integer, Array<V>, T> callbackfn);
	
	public Array<V> filter(Function3<V, Integer, Array<V>, Boolean> callbackfn);
	
	public V reduce(Function4<V, V, Integer, Array<V>, V> callbackfn);
	
	public <T> T reduce(Function4<T, V, Integer, Array<V>, T> callbackfn, T initialValue);
	
	public V reduceRight(Function4<V, V, Integer, Array<V>, V> callbackfn);
	
	public <T> T reduceRight(Function4<T, V, Integer, Array<V>, T> callbackfn, T initialValue);
}
