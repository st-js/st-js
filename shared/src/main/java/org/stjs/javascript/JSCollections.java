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

/**
 * this class offers map and array builders for both client and server side. For the server side, it delegates the
 * execution to an instance of {@link JSCollectionsImplementor}. On the client side, it simply throws an
 * {@link UnsupportedOperationException} like all the client-side bridges.
 * 
 * @author acraciun
 * 
 */
public class JSCollections {
	private static JSCollectionsImplementor implementor = null;

	@SuppressWarnings("unchecked")
	private static synchronized JSCollectionsImplementor getImplementor() {
		if (implementor != null) {
			return implementor;
		}
		try {
			// TODO it could be a cleaner way to inject the implementation, but for the moment this should be enough
			Class<? extends JSCollectionsImplementor> clazz = (Class<? extends JSCollectionsImplementor>) Class
					.forName("org.stjs.server.JSCollectionsServerImplementor");
			implementor = clazz.newInstance();
		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}
		return implementor;
	}

	public static <V> Array<V> $array(V... values) {
		return getImplementor().$array(values);
	}

	public static <T> Array<T> $castArray(T[] a) {
		return getImplementor().$castArray(a);
	}

	public static <K, V> Map<K, V> $map() {
		return getImplementor().$map();
	}

	public static <K, V> Map<K, V> $map(K k1, V v1) {
		return getImplementor().$map(k1, v1);
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2) {
		return getImplementor().$map(k1, v1, k2, v2);
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3) {
		return getImplementor().$map(k1, v1, k2, v2, k3, v3);

	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		return getImplementor().$map(k1, v1, k2, v2, k3, v3, k4, v4);
	}

	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		return getImplementor().$map(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6,
			Object... morePairs) {
		return (Map) getImplementor().$map(k1, v1, k2, v2, k3, v3, k4, v4, k6, morePairs);
	}

}
