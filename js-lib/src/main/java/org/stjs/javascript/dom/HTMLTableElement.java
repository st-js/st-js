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

abstract public class HTMLTableElement extends HTMLElement {
	public HTMLCollection<HTMLTableSectionElement> TBodies;
	public HTMLTableSectionElement TFoot;
	public HTMLTableSectionElement THead;
	public String align;
	public String bgColor;
	public String border;
	public HTMLTableCaptionElement caption;
	public String cellPadding;
	public String cellSpacing;
	public String frame;
	public HTMLCollection<HTMLTableRowElement> rows;
	public String rules;
	public String summary;
	public String width;

	abstract public HTMLElement createCaption();

	abstract public HTMLElement createTFoot();

	abstract public HTMLElement createTHead();

	abstract public void deleteCaption();

	abstract public void deleteRow(int arg0);

	abstract public void deleteTFoot();

	abstract public void deleteTHead();

	abstract public HTMLElement insertRow(int arg0);
}
