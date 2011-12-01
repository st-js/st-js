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
public class SortableOptions<FullJQuery extends JQueryCore<?>> {
	public boolean disabled = false;

	public String appendTo = "parent";

	public String axis = "false";

	public String cancel = ":input,button";

	public String connectWith = "false";

	public Object containment = false;

	public String cursor = "auto";

	public Object cursorAt = false;
	public int delay = 0;

	public int distance = 1;

	public boolean dropOnEmpty = true;

	public boolean forceHelperSize = false;

	public boolean forcePlaceholderSize = false;

	public Object grid = false;

	public Object handle = false;

	public Object helper = "original";

	public String items = "> *";

	public float opacity = 0;

	public String placeholder = "false";

	public Object revert = false;

	public boolean scroll = true;

	public int scrollSensitivity = 20;

	public int scrollSpeed = 20;

	public String tolerance = "intersect";

	public int zIndex = 1000;

	public UIEventHandler<SortableUI<FullJQuery>> create;
	public UIEventHandler<SortableUI<FullJQuery>> start;

	public UIEventHandler<SortableUI<FullJQuery>> sort;
	public UIEventHandler<SortableUI<FullJQuery>> change;
	public UIEventHandler<SortableUI<FullJQuery>> beforeStop;
	public UIEventHandler<SortableUI<FullJQuery>> update;
	public UIEventHandler<SortableUI<FullJQuery>> stop;

	public UIEventHandler<SortableUI<FullJQuery>> receive;

	public UIEventHandler<SortableUI<FullJQuery>> remove;
	public UIEventHandler<SortableUI<FullJQuery>> over;
	public UIEventHandler<SortableUI<FullJQuery>> out;
	public UIEventHandler<SortableUI<FullJQuery>> activate;
	public UIEventHandler<SortableUI<FullJQuery>> deactivate;

}
