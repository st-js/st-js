package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public class SelectableOptions<FullJQuery extends JQuery<?>> {
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
