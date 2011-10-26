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

abstract public class Node {
	public HTMLCollection<Attr> attributes;

	public void appendChild(Node node) {
		throw new UnsupportedOperationException();
	}

	public Node cloneNode(boolean deep) {
		throw new UnsupportedOperationException();
	}

	public short compareDocumentPosition(Node n) {
		throw new UnsupportedOperationException();
	}

	public String baseURI;
	public HTMLList<Node> childNodes;

	public String getFeature(String feature, String version) {
		throw new UnsupportedOperationException();
	}

	public Node firstChild;
	public Node lastChild;
	public String localName;
	public String namespaceURI;
	public Node nextSibling;
	public String nodeName;
	public short nodeType;
	public String nodeValue;
	public Document ownerDocument;
	public Node parentNode;
	public String prefix;
	public Node previousSibling;
	public String textContent;

	public String getUserData(String s) {
		throw new UnsupportedOperationException();
	}

	public boolean hasAttributes;
	public boolean hasChildNodes;

	public void insertBefore(Node n1, Node n2) {
		throw new UnsupportedOperationException();
	}

	public boolean isDefaultNamespace(String s) {
		throw new UnsupportedOperationException();
	}

	public boolean isEqualNode(Node n) {
		throw new UnsupportedOperationException();
	}

	public boolean isSameNode(Node n) {
		throw new UnsupportedOperationException();
	}

	public boolean isSupported(String feature, String version) {
		throw new UnsupportedOperationException();
	}

	public String lookupNamespaceURI(String s) {
		throw new UnsupportedOperationException();
	}

	public String lookupPrefix(String s) {
		throw new UnsupportedOperationException();
	}

	public void normalize() {
		throw new UnsupportedOperationException();
	}

	public void removeChild(Node n) {
		throw new UnsupportedOperationException();
	}

	public void replaceChild(Node n1, Node n2) {
		throw new UnsupportedOperationException();
	}
	// setUserData(String, Object, UserDataHandler)
}
