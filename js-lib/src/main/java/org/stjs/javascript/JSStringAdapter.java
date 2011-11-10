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
import org.stjs.javascript.functions.Function1;

/**
 * here are the methods existent in Javascript for number objects and inexistent in the Java counterpart. The generator
 * should generate the correct code
 * 
 * @author acraciun
 */
@Adapter
public class JSStringAdapter {
	public static String anchor(String applyTo, String anchor) {
		throw new UnsupportedOperationException();
	}

	public static String big(String applyTo) {
		throw new UnsupportedOperationException();
	}

	public static String blink(String applyTo) {
		throw new UnsupportedOperationException();
	}

	public static String bold(String applyTo) {
		throw new UnsupportedOperationException();
	}

	public static String fixed(String applyTo) {
		throw new UnsupportedOperationException();
	}

	public static String fontcolor(String applyTo, String color) {
		throw new UnsupportedOperationException();
	}

	public static String fontsize(String applyTo, int size) {
		throw new UnsupportedOperationException();
	}

	public static String italics(String applyTo) {
		throw new UnsupportedOperationException();
	}

	public static String link(String applyTo, String url) {
		throw new UnsupportedOperationException();
	}

	public static String small(String applyTo) {
		throw new UnsupportedOperationException();
	}

	public static String strike(String applyTo) {
		throw new UnsupportedOperationException();
	}

	public static String sub(String applyTo) {
		throw new UnsupportedOperationException();
	}

	public static String sup(String applyTo) {
		throw new UnsupportedOperationException();
	}

	public static Array<String> match(String applyTo, RegExp re) {
		throw new UnsupportedOperationException();
	}

	public static Array<String> split(String applyTo, String re) {
		throw new UnsupportedOperationException();
	}

	public static Array<String> split(String applyTo, String re, int limit) {
		throw new UnsupportedOperationException();
	}

	public static String replace(String applyTo, RegExp re, String repl) {
		throw new UnsupportedOperationException();
	}

	public static String replace(String applyTo, RegExp re, Function1<String, String> replaceFunction) {
		throw new UnsupportedOperationException();
	}

	public static int charCodeAt(String applyTo, int x) {
		throw new UnsupportedOperationException();
	}

	public static String fromCharCode(Class<? extends String> applyTo, int... codes) {
		throw new UnsupportedOperationException();
	}
}
