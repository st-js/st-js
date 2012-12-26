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

import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.dom.DOMEvent;
import org.stjs.javascript.dom.Element;

/**
 * jquery event
 */
@SyntheticType
abstract public class Event {
	public boolean attrChange;
	public String attrName;
	public boolean bubbles;
	public Object button;
	public boolean cancelable;
	public int charCode;
	public int clientX;
	public int clientY;
	public Object detail;
	public Object eventPhase;
	public Element fromElement;
	public Object handler;
	public int keyCode;
	public int layerX;
	public int layerY;
	public Object newValue;
	public int offsetX;
	public int offsetY;
	public Element originalTarget;
	public Object prevValue;
	public Element relatedNode;
	public int screenX;
	public int screenY;
	public Element srcElement;
	public Element toElement;
	public Object view;
	public int wheelDelta;

	public Element target;
	public Element relatedTarget;
	public Element currentTarget;
	public int pageX;
	public int pageY;
	public int which;
	public boolean metaKey;
	public boolean shiftKey;
	public boolean altKey;
	public boolean ctrlKey;

	public Object data;
	public String namespace;
	public Object result;
	public long timeStamp;
	public String type;

	public DOMEvent originalEvent;

	abstract public boolean isDefaultPrevented();

	abstract public boolean isImmediatePropagationStopped();

	abstract public boolean isPropagationStopped();

	abstract public void preventDefault();

	abstract public void stopImmediatePropagation();

	abstract public void stopPropagation();
}
