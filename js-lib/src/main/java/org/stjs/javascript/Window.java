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

import org.stjs.javascript.dom.DOMEvent;
import org.stjs.javascript.dom.Document;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Function1;

abstract public class Window {
	public boolean closed;
	public String defaultStatus;
	public Document document;
	public Window[] frames;
	public History history;
	public int innerHeight;
	public int innerWidth;
	public int length;
	public Location location;
	public String name;
	public Navigator navigator;
	public Window opener;
	public int outerHeight;
	public int outerWidth;
	public int pageXOffset;
	public int pageYOffset;
	public Window parent;
	public Screen screen;
	public int screenLeft;
	public int screenTop;
	public int screenX;
	public int screenY;
	public Window self;
	public String status;
	public Window top;

	public Function1<DOMEvent, String> onbeforeunload;
	public Callback1<DOMEvent> onload;
	public Callback1<DOMEvent> onunload;
	public Callback1<DOMEvent> onresize;

	abstract public void blur();

	abstract public void close();

	abstract public Window createPopup();

	abstract public void focus();

	abstract public void moveBy(int x, int y);

	abstract public void moveTo(int x, int y);

	abstract public Window open(String url);

	abstract public Window open(String url, String target);

	abstract public Window open(String url, String target, String specs, boolean replace);

	abstract public void print();

	abstract public void resizeBy(int w, int h);

	abstract public void resizeTo(int w, int h);

	abstract public void scrollBy(int x, int y);

	abstract public void scrollTo(int x, int y);
}
