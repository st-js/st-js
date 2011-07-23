package org.stjs.javascript.jquery;

import org.stjs.javascript.jquery.plugins.Accordion;
import org.stjs.javascript.jquery.plugins.AutoComplete;
import org.stjs.javascript.jquery.plugins.Button;
import org.stjs.javascript.jquery.plugins.DatePicker;
import org.stjs.javascript.jquery.plugins.Dialog;
import org.stjs.javascript.jquery.plugins.Draggable;
import org.stjs.javascript.jquery.plugins.Droppable;
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
public interface JQueryAndPlugins<FullJQuery extends JQueryAndPlugins<?>> extends JQuery<FullJQuery>, //
		Accordion<FullJQuery>,//
		AutoComplete<FullJQuery>,//
		Button<FullJQuery>,//
		DatePicker<FullJQuery>,//
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
