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
public class DroppableOptions<FullJQuery extends JQueryCore<?>> {
	public boolean disabled = false;

	public Object accept = "*";

	public String activeClass = "false";

	public boolean addClasses = true;

	public boolean greedy = false;

	public String hoverClass = "false";

	public String scope = "default";

	public String tolerance = "intersect";

	public UIEventHandler<DroppableUI<FullJQuery>> create;
	public UIEventHandler<DroppableUI<FullJQuery>> activate;

	public UIEventHandler<DroppableUI<FullJQuery>> deactivate;

	public UIEventHandler<DroppableUI<FullJQuery>> over;

	public UIEventHandler<DroppableUI<FullJQuery>> out;
	public UIEventHandler<DroppableUI<FullJQuery>> drop;
}
