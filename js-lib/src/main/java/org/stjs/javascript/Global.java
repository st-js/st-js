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
import org.stjs.javascript.functions.Callback0;

@GlobalScope
public class Global {
	public static Number Infinity;
	public static Number NaN;

	public static Object undefined;

	public static Window window;

	public static Console console;

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

	public static int parseInt(Object expr, int radix) {
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

	public static TimeoutHandler setTimeout(Callback0 expr, int timeoutMillis) {
		throw new UnsupportedOperationException();
	}

	public static TimeoutHandler setInterval(String expr, int timeoutMillis) {
		throw new UnsupportedOperationException();
	}

	public static TimeoutHandler setInterval(Callback0 expr, int timeoutMillis) {
		throw new UnsupportedOperationException();
	}

	public static void clearTimeout(TimeoutHandler handler) {
		throw new UnsupportedOperationException();
	}

	public static void clearInterval(TimeoutHandler handler) {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	/**
	 * defined in stjs.js
	 * 
	 * @param exception
	 * @return
	 */
	public static RuntimeException exception(Object exception) {
		throw new UnsupportedOperationException();
	}

	/**
	 * defined in stjs.js
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEnum(Object obj) {
		throw new UnsupportedOperationException();
	}

}
