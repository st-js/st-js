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
package org.stjs.javascript.jquery;

import org.stjs.javascript.Array;
import org.stjs.javascript.GlobalScope;
import org.stjs.javascript.Map;
import org.stjs.javascript.functions.Callback2;
import org.stjs.javascript.functions.Function2;

@GlobalScope
abstract public class GlobalJQuery {
	public static GlobalJQuery $;

	/**
	 * jquery constructors
	 */
	public static <FullJQuery extends JQueryAndPlugins<?>> JQueryAndPlugins<FullJQuery> $(String path) {
		return null;
	}

	public static <FullJQuery extends JQueryAndPlugins<?>> JQueryAndPlugins<FullJQuery> $(Object path) {
		return null;
	}

	abstract public void ajax(AjaxParams params);

	abstract public void get(String url, Object params, SuccessListener successListener, String mode);

	abstract public <C, E, R> Array<R> map(C collection, Function2<E, Integer, R> callback);

	abstract public <T> int inArray(T element, Array<T> registeredListeners);

	abstract public <E> void each(Array<E> collection, Callback2<Integer, E> elementIterationFunction);

	abstract public <K, V> void each(Map<K, V> collection, Callback2<K, V> elementIterationFunction);

	abstract public String trim(String obj);

}
