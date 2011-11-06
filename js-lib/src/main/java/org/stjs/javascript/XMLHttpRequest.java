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

import org.stjs.javascript.dom.Element;
import org.stjs.javascript.functions.Callback0;

abstract public class XMLHttpRequest {
	public Callback0 onreadystatechange;
	public int readyState;
	public String responseText;
	// is in fact DOM Element, but they were mixed
	public Element responseXML;
	public int status;
	public String statusText;

	abstract public void abort();

	abstract public String getAllResponseHeaders();

	abstract public String getResponseHeader(String name);

	abstract public void open(String method, String url);

	abstract public void open(String method, String url, boolean async, String uname, String pswd);

	abstract public void open(String method, String url, boolean async);

	abstract public void send();

	abstract public void send(String data);

	abstract public void setRequestHeader(String name, Object value);
}
