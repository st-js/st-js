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
package org.stjs.javascript.dom;

abstract public class HTMLSelectElement extends HTMLElement {
	public boolean disabled;
	public HTMLFormElement form;
	public int length;
	public boolean multiple;
	public String name;
	public HTMLCollection<HTMLOptionElement> options;
	public int selectedIndex;
	public int size;
	public int tabIndex;
	public String type;
	public String value;

	abstract public void blur();

	abstract public void add(HTMLElement arg0, HTMLElement arg1);

	abstract public void remove(int arg0);

	abstract public void focus();
}
