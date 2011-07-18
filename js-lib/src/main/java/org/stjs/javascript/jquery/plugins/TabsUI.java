package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public class TabsUI<FullJQuery extends JQuery<?>> {
	public FullJQuery tab; // anchor element of the selected (clicked) tab
	public FullJQuery panel; // element, that contains the selected/clicked tab contents
	public int index; // zero-based index of the selected (clicked) tab

}
