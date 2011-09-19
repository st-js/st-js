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

@GlobalScope
public class Global {
	public static Number Infinity;
	public static Number NaN;

	public static Object undefined;

	public static Window window;

	// do not add this one too
	// public static Window self;

	public static <T> T eval(String expr) {
		return null;
	}

	public static void alert(Object expr) {
	}

	public static boolean confirm(String expr) {
		return true;
	}

	public static String prompt(String expr) {
		return null;
	}

	public static String prompt(String expr, String defaultText) {
		return null;
	}

	public static double parseFloat(Object expr) {
		return 0;
	}

	public static int parseInt(Object expr) {
		return 0;
	}

	public static String typeof(Object obj) {
		return "";
	}

	/**
	 * this is the equivalent of x || y || z in javascript
	 * 
	 * @return
	 */
	public static <T> T $or(T value, T... otherValues) {
		return value;
	}

	public static TimeoutHandler setTimeout(String expr, int timeoutMillis) {
		return null;
	}

	public static TimeoutHandler setTimeout(Runnable expr, int timeoutMillis) {
		return null;
	}

	public static TimeoutHandler setInterval(String expr, int timeoutMillis) {
		return null;
	}

	public static TimeoutHandler setInterval(Runnable expr, int timeoutMillis) {
		return null;
	}

	public static void clearTimeout(TimeoutHandler handler) {
	}

	public static void clearInterval(TimeoutHandler handler) {
	}

	public static <V> Array<V> $array(V... values) {
		return null;
	}

	public static <K, V> Map<K, V> $map() {
		return null;
	}

	public static <K, V> Map<K, V> $map(K k1, V v1) {
		return null;
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2) {
		return null;
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3) {
		return null;
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		return null;
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		return null;
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6,
			Object... morePairs) {
		return null;
	}

	public static String decodeURI(String uri) {
		return null;
	}

	public static String decodeURIComponent(String uri) {
		return null;
	}

	public static String encodeURI(String uri) {
		return null;
	}

	public static String encodeURIComponent(String uri) {
		return null;
	}

	public static String escape(String uri) {
		return null;
	}

	public static boolean isFinite(Object value) {
		return true;
	}

	public static boolean isNaN(Object value) {
		return true;
	}

	public static String unescape(String uri) {
		return null;
	}

}
