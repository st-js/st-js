package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public class AccordionOptions<FullJQuery extends JQuery<?>> {
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
