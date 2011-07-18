package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public class TabsOptions<FullJQuery extends JQuery<?>> {
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

}
