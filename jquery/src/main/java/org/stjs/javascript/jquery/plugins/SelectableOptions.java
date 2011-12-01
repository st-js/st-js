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
public class SelectableOptions<FullJQuery extends JQueryCore<?>> {
	public boolean disabled = false;

	public boolean autoRefresh = true;

	public String cancel = ":input,option";

	public int delay = 0;

	public int distance = 0;

	public String filter = "*";

	public String tolerance = "touch";

	public UIEventHandler<SelectableUI<FullJQuery>> create;
	public UIEventHandler<SelectableUI<FullJQuery>> start;

	public UIEventHandler<SelectableUI<FullJQuery>> selected;
	public UIEventHandler<SelectableUI<FullJQuery>> selecting;
	public UIEventHandler<SelectableUI<FullJQuery>> unselected;
	public UIEventHandler<SelectableUI<FullJQuery>> unselecting;
	public UIEventHandler<SelectableUI<FullJQuery>> stop;
}
