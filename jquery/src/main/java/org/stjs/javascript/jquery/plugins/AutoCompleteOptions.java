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

import org.stjs.javascript.jquery.JQuery;

public class AutoCompleteOptions<FullJQuery extends JQuery<?>> {
	public boolean disabled = false;

	public String appendTo = "body";

	public boolean autoFocus = false;

	public int delay = 300;

	public int minLength = 1;

	public Object position;// = { my: "left top", at: "left bottom", collision: "none" }

	public Object source;

	public UIEventHandler<AutoCompleteUI<FullJQuery>> create;
	public UIEventHandler<AutoCompleteUI<FullJQuery>> search;

	public UIEventHandler<AutoCompleteUI<FullJQuery>> open;
	public UIEventHandler<AutoCompleteUI<FullJQuery>> focus;

	public UIEventHandler<AutoCompleteUI<FullJQuery>> select;
	public UIEventHandler<AutoCompleteUI<FullJQuery>> close;
	public UIEventHandler<AutoCompleteUI<FullJQuery>> change;
}
