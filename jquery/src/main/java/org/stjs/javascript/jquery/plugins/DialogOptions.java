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
package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.annotation.DataType;
import org.stjs.javascript.jquery.JQueryCore;

@DataType
public class DialogOptions<FullJQuery extends JQueryCore<?>> {
	public boolean disabled = false;

	public boolean autoOpen = true;

	public Object buttons;// ={ } or Array

	public boolean closeOnEscape = true;

	public String closeText = "close";

	public String dialogClass = "";

	public boolean draggable = true;

	public Object height = "auto";

	public Object hide = null;

	public Integer maxHeight = null;

	public Integer maxWidth = null;

	public int minHeight = 150;

	public int minWidth = 150;

	public boolean modal = false;

	public Object position = "center";

	public boolean resizable = true;

	public Object show = null;

	public boolean stack = true;

	public String title = "";

	public Object width = 300;

	public int zIndex = 1000;

	public UIEventHandler<DialogUI<FullJQuery>> create;
	public UIEventHandler<DialogUI<FullJQuery>> beforeClose;

	public UIEventHandler<DialogUI<FullJQuery>> open;
	public UIEventHandler<DialogUI<FullJQuery>> focus;

	public UIEventHandler<DialogUI<FullJQuery>> dragStart;
	public UIEventHandler<DialogUI<FullJQuery>> drag;
	public UIEventHandler<DialogUI<FullJQuery>> dragStop;

	public UIEventHandler<DialogUI<FullJQuery>> resizeStart;
	public UIEventHandler<DialogUI<FullJQuery>> resize;
	public UIEventHandler<DialogUI<FullJQuery>> resizeSop;

	public UIEventHandler<DialogUI<FullJQuery>> close;

}
