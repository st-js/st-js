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

import java.util.HashMap;
import java.util.Iterator;

import org.stjs.javascript.Map;

/**
 * This class implements the {@link Map} interface to be used on the server side.
 * 
 * @author acraciun
 * @param <V>
 */
public class MapImpl<K extends String, V> implements Map<K, V> {
	private final java.util.Map<K, V> map = new HashMap<K, V>();

	@Override
	public Iterator<K> iterator() {
		return map.keySet().iterator();
	}

	@Override
	public V $get(K key) {
		return map.get(key);
	}

	@Override
	public void $put(K key, V value) {
		map.put(key, value);
	}

	@Override
	public void $delete(K key) {
		map.remove(key);
	}

	@Override
	public String toString() {
		return map.toString();
	}
}
