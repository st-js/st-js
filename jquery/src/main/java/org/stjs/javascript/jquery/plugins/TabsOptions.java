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

import org.stjs.javascript.jquery.JQueryCore;

public class TabsOptions<FullJQuery extends JQueryCore<?>> {
	public Object disabled = false;
	// also : public Array<String> disabled;

	public Object ajaxOptions = null;

	public boolean cache = false;

	public boolean collapsible = false;

	public Object cookie = null;

	public boolean deselectable = false;

	public String event = "click";

	public Object fx = null;

	public String idPrefix = "ui-tabs-";

	public String panelTemplate = "<div></div>";

	public int selected = 0;

	public String spinner = "<em>Loading&#8230;</em>";

	public String tabTemplate = "<li><a href='#{href}'><span>#{label}</span></a></li>";

	public UIEventHandler<TabsUI<FullJQuery>> create;

	public UIEventHandler<TabsUI<FullJQuery>> select;

	public UIEventHandler<TabsUI<FullJQuery>> load;

	public UIEventHandler<TabsUI<FullJQuery>> show;

	public UIEventHandler<TabsUI<FullJQuery>> add;

	public UIEventHandler<TabsUI<FullJQuery>> remove;

	public UIEventHandler<TabsUI<FullJQuery>> enable;

	public UIEventHandler<TabsUI<FullJQuery>> disable;

}
