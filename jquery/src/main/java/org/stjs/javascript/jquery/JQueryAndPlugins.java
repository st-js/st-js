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
import org.stjs.javascript.jquery.plugins.AutoComplete;
import org.stjs.javascript.jquery.plugins.Button;
import org.stjs.javascript.jquery.plugins.Datepicker;
import org.stjs.javascript.jquery.plugins.Dialog;
import org.stjs.javascript.jquery.plugins.Draggable;
import org.stjs.javascript.jquery.plugins.Droppable;
import org.stjs.javascript.jquery.plugins.JQueryUI;
import org.stjs.javascript.jquery.plugins.Progressbar;
import org.stjs.javascript.jquery.plugins.Resizable;
import org.stjs.javascript.jquery.plugins.Selectable;
import org.stjs.javascript.jquery.plugins.Slider;
import org.stjs.javascript.jquery.plugins.Sortable;
import org.stjs.javascript.jquery.plugins.Tabs;

/**
 * In the JQuery model, the plugins add methods to the jquery object. FullJQueryhis interface inherits from all the
 * needed plugins.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
@SyntheticType
public interface JQueryAndPlugins<FullJQuery extends JQueryAndPlugins<?>> extends JQueryCore<FullJQuery>, //
		JQueryUI<FullJQuery>,// Accordion<FullJQuery>,//
		AutoComplete<FullJQuery>,//
		Button<FullJQuery>,//
		Datepicker<FullJQuery>,//
		Dialog<FullJQuery>,//
		Draggable<FullJQuery>,//
		Droppable<FullJQuery>,//
		Progressbar<FullJQuery>,//
		Resizable<FullJQuery>,//
		Selectable<FullJQuery>,//
		Slider<FullJQuery>,//
		Sortable<FullJQuery>,//
		Tabs<FullJQuery>//
{

}
