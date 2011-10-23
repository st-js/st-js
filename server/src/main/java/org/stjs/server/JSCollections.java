package org.stjs.server;

import org.stjs.shared.Array;
import org.stjs.shared.Map;

public class JSCollections {
	public static <V> Array<V> $array(V... values) {
		Array<V> a = new ArrayImpl<V>();
		a.splice(0, 0, values);
		return a;
	}

	public static <K, V> Map<K, V> $map() {
		return new MapImpl<K, V>();
	}

	public static <K, V> Map<K, V> $map(K k1, V v1) {
		Map<K, V> m = new MapImpl<K, V>();
		m.$put(k1, v1);
		return m;
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2) {
		Map<K, V> m = new MapImpl<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		return m;
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3) {
		Map<K, V> m = new MapImpl<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		m.$put(k3, v3);
		return m;
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		Map<K, V> m = new MapImpl<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		m.$put(k3, v3);
		m.$put(k4, v4);
		return m;
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		Map<K, V> m = new MapImpl<K, V>();
		m.$put(k1, v1);
		m.$put(k2, v2);
		m.$put(k3, v3);
		m.$put(k4, v4);
		m.$put(k5, v5);
		return m;
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6,
			Object... morePairs) {
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

}
