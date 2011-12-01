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

import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollectionsImplementor;
import org.stjs.javascript.Map;

/**
 * this class implements the contract for Javascript collection on the server side
 * @author acraciun
 */
public class JSCollectionsServerImplementor implements JSCollectionsImplementor {
	@Override
	public <V> Array<V> $array(V... values) {
		Array<V> a = new ArrayImpl<V>();
		a.splice(0, 0, values);
		return a;
	}

	@Override
	public <K extends String, V> Map<K, V> $map() {
		return new MapImpl<K, V>();
	}

	@Override
	public <K extends String, V> Map<K, V> $map(K k1, V v1) {
		Map<K, V> m = new MapImpl<K, V>();
		m.$put(k1, v1);
		return m;
	}

	@Override
	public <K extends String, V> Map<K, V> $map(K k1, V v1, K k2, V v2) {
		Map<K, V> m = new MapImpl<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		return m;
	}

	@Override
	public <K extends String, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3) {
		Map<K, V> m = new MapImpl<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		m.$put(k3, v3);
		return m;
	}

	@Override
	public <K extends String, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		Map<K, V> m = new MapImpl<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		m.$put(k3, v3);
		m.$put(k4, v4);
		return m;
	}

	@Override
	public <K extends String, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		Map<K, V> m = new MapImpl<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		m.$put(k3, v3);
		m.$put(k4, v4);
		m.$put(k5, v5);
		return m;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <K extends String, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, Object... morePairs) {
		Map<K, V> m = new MapImpl<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		m.$put(k3, v3);
		m.$put(k4, v4);
		m.$put(k5, v5);
		for (int i = 0; i < (morePairs.length - 1); i += 2) {
			m.$put((K) morePairs[i], (V) morePairs[i + 1]);
		}
		return m;
	}

	@Override
	public <T> Array<T> $castArray(T[] a) {
		return $array(a);
	}

}
