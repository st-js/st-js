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

abstract public class HTMLElement extends Node {
	public String className;
	public String dir;
	public String id;
	public String lang;
	public String title;
	public String tagName;

	abstract public String getAttribute(String name);

	abstract public Attr getAttributeNode(String name);

	// abstract public getAttributeNodeNS(String, String)
	// abstract public getAttributeNS(String, String)
	abstract public HTMLList<HTMLElement> getElementsByTagName(String tag);

	// abstract public getElementsByTagNameNS(String, String)
	// abstract public getSchemaTypeInfo()

	abstract public boolean hasAttribute(String name);

	// abstract public hasAttributeNS(String, String)
	abstract public void removeAttribute(String name);

	abstract public void removeAttributeNode(Attr att);

	// abstract public removeAttributeNS(String, String)
	abstract public void setAttribute(String name, String value);

	abstract public void setAttributeNode(Attr name);

	// abstract public setAttributeNodeNS(Attr)
	// abstract public setAttributeNS(String, String, String)
	abstract public void setIdAttribute(String name, boolean id);

	abstract public void setIdAttributeNode(Attr attr, boolean id);

	// abstract public setIdAttributeNS(String, String, boolean)

}
