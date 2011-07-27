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

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

abstract public class HTMLDocument {
	public String URL;
	public String documentURI;
	public HTMLCollection<HTMLAnchorElement> anchors;
	public HTMLCollection<HTMLAppletElement> applets;
	public HTMLElement body;
	public String cookie;
	public String domain;
	public HTMLCollection<HTMLFormElement> forms;
	public HTMLCollection<HTMLImageElement> images;
	public HTMLCollection<HTMLLinkElement> links;
	public String referrer;
	public String title;
	public HTMLElement documentElement;

	abstract public HTMLList<HTMLElement> getElementsByName(String arg0);

	abstract public HTMLElement getElementById(String id);

	abstract public HTMLList<HTMLElement> getElementsByTagName(String tagName);

	abstract public void writeln(String arg0);

	abstract public void write(String arg0);

	abstract public void close();

	abstract public void open();

	abstract public Element createElement(String tagName);

	abstract public Text createTextNode(String data);

	abstract public Attr createAttribute(String name);
}
