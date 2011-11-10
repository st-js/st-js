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
 * This interface is used to separate the implementations between the client and the server for the maps.
 * 
 * @author acraciun
 * 
 */
public interface JSCollectionsImplementor {
	public <V> Array<V> $array(V... values);

	public <T> Array<T> $castArray(T[] a);

	public <K, V> Map<K, V> $map();

	public <K, V> Map<K, V> $map(K k1, V v1);

	public <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2);

	public <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3);

	public <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4);

	public <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5);

	public <K, V> Map<K, V> $map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, Object... morePairs);
}
