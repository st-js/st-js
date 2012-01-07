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

import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.jquery.JQueryCore;

@SyntheticType
public class AccordionOptions<FullJQuery extends JQueryCore<?>> {
	public boolean disabled = false;

	public Object active;
	public String animated = "slide";

	public boolean autoHeight = true;

	public boolean clearStyle = false;

	public boolean collapsible = false;

	public String event = "click";

	public boolean fillSpace = false;

	public String header = "> li > :first-child,> :not(li):even";

	public Object icons;// = { "header": "ui-icon-triangle-1-e", "headerSelected": "ui-icon-triangle-1-s" }

	public boolean navigation = false;

	public Object navigationFilter;

	public UIEventHandler<AccordionUI<FullJQuery>> create;
	public UIEventHandler<AccordionUI<FullJQuery>> change;

	public UIEventHandler<AccordionUI<FullJQuery>> changestart;
}
