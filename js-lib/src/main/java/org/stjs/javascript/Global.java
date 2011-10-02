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

@GlobalScope
public class Global {
	public static Number Infinity;
	public static Number NaN;

	public static Object undefined;

	public static Window window;

	// do not add this one too
	// public static Window self;

	public static <T> T eval(String expr) {
		throw new UnsupportedOperationException();
	}

	public static void alert(Object expr) {
		throw new UnsupportedOperationException();
	}

	public static boolean confirm(String expr) {
		throw new UnsupportedOperationException();
	}

	public static String prompt(String expr) {
		throw new UnsupportedOperationException();
	}

	public static String prompt(String expr, String defaultText) {
		throw new UnsupportedOperationException();
	}

	public static double parseFloat(Object expr) {
		throw new UnsupportedOperationException();
	}

	public static int parseInt(Object expr) {
		throw new UnsupportedOperationException();
	}

	public static String typeof(Object obj) {
		throw new UnsupportedOperationException();
	}

	/**
	 * this is the equivalent of x || y || z in javascript
	 * 
	 * @return
	 */
	public static <T> T $or(T value, T... otherValues) {
		throw new UnsupportedOperationException();
	}

	public static TimeoutHandler setTimeout(String expr, int timeoutMillis) {
		throw new UnsupportedOperationException();
	}

	public static TimeoutHandler setTimeout(Runnable expr, int timeoutMillis) {
		throw new UnsupportedOperationException();
	}

	public static TimeoutHandler setInterval(String expr, int timeoutMillis) {
		throw new UnsupportedOperationException();
	}

	public static TimeoutHandler setInterval(Runnable expr, int timeoutMillis) {
		throw new UnsupportedOperationException();
	}

	public static void clearTimeout(TimeoutHandler handler) {
		throw new UnsupportedOperationException();
	}

	public static void clearInterval(TimeoutHandler handler) {
		throw new UnsupportedOperationException();
	}

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
		for (int i = 0; i < morePairs.length - 1; i += 2) {
			m.$put((K) morePairs[i], (V) morePairs[i + 1]);
		}
		return m;
	}

	public static String decodeURI(String uri) {
		throw new UnsupportedOperationException();
	}

	public static String decodeURIComponent(String uri) {
		throw new UnsupportedOperationException();
	}

	public static String encodeURI(String uri) {
		throw new UnsupportedOperationException();
	}

	public static String encodeURIComponent(String uri) {
		throw new UnsupportedOperationException();
	}

	public static String escape(String uri) {
		throw new UnsupportedOperationException();
	}

	public static boolean isFinite(Object value) {
		throw new UnsupportedOperationException();
	}

	public static boolean isNaN(Object value) {
		throw new UnsupportedOperationException();
	}

	public static String unescape(String uri) {
		return null;
	}

}
