/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"){throw new UnsupportedOperationException();}
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
package org.stjs.javascript.dom;

abstract public class HTMLCollection<T extends Node> {
	public int length;

	public T namedItem(String name) {
		throw new UnsupportedOperationException();
	}

	public T $get(String name) {
		throw new UnsupportedOperationException();
	}

	public T $get(int index) {
		throw new UnsupportedOperationException();
	}

	public T item(int index) {
		throw new UnsupportedOperationException();
	}

	public void $set(int index, T node) {
		throw new UnsupportedOperationException();
	}

	public void $set(String index, T node) {
		throw new UnsupportedOperationException();
	}
}
