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

import java.lang.reflect.Method;

import org.stjs.javascript.annotation.Adapter;
import org.stjs.javascript.annotation.Template;

/**
 * here are the methods existent in Javascript for objects and inexistent in the Java counterpart. The generator should
 * generate the correct code
 *
 * @author acraciun
 * @version $Id: $Id
 */
@Adapter
public final class JSObjectAdapter {
	private JSObjectAdapter() {
		//
	}

	/**
	 * <p>$get.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @param property a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	@Template("get")
	public native static Object $get(Object obj, String property);

	/**
	 * <p>$put.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @param property a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 */
	@Template("put")
	public native static void $put(Object obj, String property, Object value);

	/**
	 * <p>hasOwnProperty.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @param property a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	@Template("adapter")
	public native static boolean hasOwnProperty(Object obj, String property);

	/**
	 * <p>toLocaleString.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 */
	@Template("adapter")
	public static String toLocaleString(Object obj) {
		if (obj == null) {
			throw new Error("ReferenceError", "Cannot access property toLocaleString of null");
		}
		try {
			Method toLocaleString = obj.getClass().getMethod("toLocaleString");
			return (String) toLocaleString.invoke(obj);

		}
		catch (NoSuchMethodException e) {
			// this one could happen. Default behavior of the Object prototy in JS is to call ToString
			// let's do the same
			return JSAbstractOperations.ToString(obj.toString());
		}
		catch (Exception e) {
			// any other error is a real error
			throw new Error("TypeError", "Could not access toLocaleString() method", e);
		}
	}

	/**
	 * <p>$prototype.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @return a {@link org.stjs.javascript.Map} object.
	 */
	@Template("toProperty")
	public native static Map<String, Object> $prototype(Object obj);

	/**
	 * <p>$constructor.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 */
	@Template("toProperty")
	public native static Object $constructor(Object obj);

	/**
	 * <p>$properties.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @return a {@link org.stjs.javascript.Map} object.
	 */
	@Template("properties")
	public native static Map<String, Object> $properties(Object obj);

	/**
	 * <p>$object.</p>
	 *
	 * @param properties a {@link org.stjs.javascript.Map} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	@Template("properties")
	public native static <T> T $object(Map<String, Object> properties);

	/**
	 * <p>$js.</p>
	 *
	 * @param code a {@link java.lang.String} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	@Template("js")
	public native static <T> T $js(String code);
}
