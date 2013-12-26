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

import java.util.HashMap;
import java.util.Iterator;

import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.annotation.Template;

/**
 * This interface represents a normal object in javascript (that acts as a map). The key can be only be a String! is
 * done on the keys to have the javascript equivalent of <br>
 * <b>for(var key in map)</b> <br>
 * The methods are prefixed with $ to let the generator know that is should generate bracket access instead, i.e <br>
 * map.$get(key) => map[key] <br>
 * map.$put(key, value) => map[key]=value
 * 
 * @author acraciun
 */
@SyntheticType
public final class Map<K extends String, V> implements Iterable<K> {
	private final java.util.Map<K, V> map = new HashMap<K, V>();

	/**
	 * Constructor is package private, it isn't supposed to be used directly by clients of the API. Use
	 * <tt>JSCollections.$map()</tt> instead.
	 */
	Map() {
		super();
	}

	public Iterator<K> iterator() {
		return map.keySet().iterator();
	}

	@Template("get")
	public V $get(K key) {
		return map.get(key);
	}

	@Template("put")
	public void $put(K key, V value) {
		map.put(key, value);
	}

	@Template("delete")
	public void $delete(K key) {
		map.remove(key);
	}

	@Override
	public String toString() {
		return map.toString();
	}
}
