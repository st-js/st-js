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

import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.Template;

/**
 * this class offers map and array builders for both client and server side. For the server side, it delegates the execution to an instance of
 * {@link JSCollectionsImplementor}. On the client side, it simply throws an {@link UnsupportedOperationException} like all the client-side
 * bridges.
 * @author acraciun
 */
@GlobalScope
@SuppressWarnings("unchecked")
public class JSCollections {

	@Template("array")
	@SafeVarargs
	public static <V> Array<V> $array(V... values) {
		Array<V> a = new Array<V>();
		a.splice(0, 0, values);
		return a;
	}

	@Template("map")
	public static <K extends String, V> Map<K, V> $map() {
		return new Map<K, V>();
	}

	@Template("map")
	public static <K extends String, V> Map<K, V> $map(K k1, V v1) {
		Map<K, V> m = new Map<K, V>();
		m.$put(k1, v1);
		return m;
	}

	@Template("map")
	public static <K extends String, V> Map<K, V> $map(K k1, V v1, K k2, V v2) {
		Map<K, V> m = new Map<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		return m;
	}

	@Template("map")
	public static <K extends String, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3) {
		Map<K, V> m = new Map<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		m.$put(k3, v3);
		return m;
	}

	@Template("map")
	public static <K extends String, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		Map<K, V> m = new Map<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		m.$put(k3, v3);
		m.$put(k4, v4);
		return m;
	}

	@Template("map")
	public static <K extends String, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		Map<K, V> m = new Map<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		m.$put(k3, v3);
		m.$put(k4, v4);
		m.$put(k5, v5);
		return m;
	}

	@Template("map")
	@SuppressWarnings("unchecked")
	public static <K extends String, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, Object... morePairs) {
		Map<K, V> m = new Map<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		m.$put(k3, v3);
		m.$put(k4, v4);
		m.$put(k5, v5);
		for (int i = 0; i < morePairs.length - 1; i += 2) {
			m.$put((K) morePairs[i], (V) morePairs[i + 1]);
		}
		return m;
	}

	@Template("properties")
	public static <T> Array<T> $castArray(T[] a) {
		return $array(a);
	}

}
