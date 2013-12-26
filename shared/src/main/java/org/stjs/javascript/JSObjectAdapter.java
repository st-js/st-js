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

import org.stjs.javascript.annotation.Adapter;
import org.stjs.javascript.annotation.Template;

/**
 * here are the methods existent in Javascript for objects and inexistent in the Java counterpart. The generator should
 * generate the correct code
 * 
 * @author acraciun
 */
@Adapter
public final class JSObjectAdapter {
	private JSObjectAdapter() {
		//
	}

	@Template("get")
	public native static Object $get(Object obj, String property);

	@Template("put")
	public native static void $put(Object obj, String property, Object value);

	@Template("adapter")
	public native static boolean hasOwnProperty(Object obj, String property);

	@Template("toProperty")
	public native static Map<String, Object> $prototype(Object obj);

	@Template("toProperty")
	public native static Object $constructor(Object obj);

	@Template("properties")
	public native static Map<String, Object> $properties(Object obj);

	@Template("properties")
	public native static <T> T $object(Map<String, Object> properties);

	@Template("js")
	public native static <T> T $js(String code);
}
