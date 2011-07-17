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
