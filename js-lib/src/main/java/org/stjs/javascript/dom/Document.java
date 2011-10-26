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

import org.stjs.javascript.Location;

abstract public class Document {
	public String URL;
	public String documentURI;
	public HTMLCollection<Anchor> anchors;
	public HTMLCollection<Applet> applets;
	public Element body;
	public String cookie;
	public String domain;
	public HTMLCollection<Form> forms;
	public HTMLCollection<Image> images;
	public HTMLCollection<Link> links;
	public String referrer;
	public String title;
	public Element documentElement;
	public Location location;

	public HTMLList<Element> getElementsByName(String arg0) {
		throw new UnsupportedOperationException();
	}

	public Element getElementById(String id) {
		throw new UnsupportedOperationException();
	}

	public HTMLList<Element> getElementsByTagName(String tagName) {
		throw new UnsupportedOperationException();
	}

	public void writeln(String arg0) {
		throw new UnsupportedOperationException();
	}

	public void write(String arg0) {
		throw new UnsupportedOperationException();
	}

	public void close() {
		throw new UnsupportedOperationException();
	}

	public void open() {
		throw new UnsupportedOperationException();
	}

	public Element createElement(String tagName) {
		throw new UnsupportedOperationException();
	}

	public Text createTextNode(String data) {
		throw new UnsupportedOperationException();
	}

	public Attr createAttribute(String name) {
		throw new UnsupportedOperationException();
	}
}
