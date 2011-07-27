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

abstract public class Node {
	public HTMLCollection<Attr> attributes;

	abstract public void appendChild(Node node);

	abstract public Node cloneNode(boolean deep);

	abstract public short compareDocumentPosition(Node n);

	public String baseURI;
	public HTMLList<Node> childNodes;

	abstract public String getFeature(String feature, String version);

	public Node firstChild;
	public Node lastChild;
	public String localName;
	public String namespaceURI;
	public Node nextSibling;
	public String nodeName;
	public short nodeType;
	public String nodeValue;
	public HTMLDocument ownerDocument;
	public Node parentNode;
	public String prefix;
	public Node previousSibling;
	public String textContent;

	abstract public String getUserData(String s);

	public boolean hasAttributes;
	public boolean hasChildNodes;

	abstract public void insertBefore(Node n1, Node n2);

	abstract public boolean isDefaultNamespace(String s);

	abstract public boolean isEqualNode(Node n);

	abstract public boolean isSameNode(Node n);

	abstract public boolean isSupported(String feature, String version);

	abstract public String lookupNamespaceURI(String s);

	abstract public String lookupPrefix(String s);

	abstract public void normalize();

	abstract public void removeChild(Node n);

	abstract public void replaceChild(Node n1, Node n2);
	// setUserData(String, Object, UserDataHandler)
}
