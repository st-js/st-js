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
 * <p>RegExp class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class RegExp {
	public boolean global;
	public boolean ignoreCase;
	public int lastIndex;
	public boolean multiline;
	public String source;

	/**
	 * <p>Constructor for RegExp.</p>
	 *
	 * @param pattern a {@link java.lang.String} object.
	 */
	public RegExp(String pattern) {//
	}

	/**
	 * <p>Constructor for RegExp.</p>
	 *
	 * @param pattern a {@link java.lang.String} object.
	 * @param modifiers a {@link java.lang.String} object.
	 */
	public RegExp(String pattern, String modifiers) {
		//
	}

	/**
	 * <p>compile.</p>
	 *
	 * @param pattern a {@link java.lang.String} object.
	 * @param modifiers a {@link java.lang.String} object.
	 */
	public native void compile(String pattern, String modifiers);

	/**
	 * <p>compile.</p>
	 *
	 * @param pattern a {@link java.lang.String} object.
	 */
	public native void compile(String pattern);

	/**
	 * <p>exec.</p>
	 *
	 * @param source a {@link java.lang.String} object.
	 * @return a {@link org.stjs.javascript.RegExpMatch} object.
	 */
	public native RegExpMatch exec(String source);

	/**
	 * <p>test.</p>
	 *
	 * @param source a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public native boolean test(String source);
}
