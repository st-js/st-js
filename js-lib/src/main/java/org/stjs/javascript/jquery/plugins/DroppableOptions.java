package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public class DroppableOptions<FullJQuery extends JQuery<?>> {
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
