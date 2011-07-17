package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public class ProgressbarOptions<FullJQuery extends JQuery<?>> {
	public boolean disabled = false;

	public Number value = 0;

	public UIEventHandler<ProgressbarUI<FullJQuery>> create;

	public UIEventHandler<ProgressbarUI<FullJQuery>> change;

	public UIEventHandler<ProgressbarUI<FullJQuery>> complete;
}
