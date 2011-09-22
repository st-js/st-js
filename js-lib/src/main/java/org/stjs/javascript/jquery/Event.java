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
package org.stjs.javascript.jquery;

import org.stjs.javascript.dom.HTMLElement;

/**
 * jquery event
 */
abstract public class Event {
	public HTMLElement target;
	public HTMLElement relatedTarget;
	public HTMLElement currentTarget;
	public int pageX;
	public int pageY;
	public int which;
	public String metaKey;
	public Object data;
	public String namespace;
	public Object result;
	public long timeStamp;
	public String type;

	public boolean altKey;
	public int keyCode;
	public HTMLElement srcElement;
	public boolean ctrlKey;
	public boolean returnValue;

	abstract public boolean isDefaultPrevented();

	abstract public boolean isImmediatePropagationStopped();

	abstract public boolean isPropagationStopped();

	abstract public void preventDefault();

	abstract public void stopImmediatePropagation();

	abstract public void stopPropagation();
}
