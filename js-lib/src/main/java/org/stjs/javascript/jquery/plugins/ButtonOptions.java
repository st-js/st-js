package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public class ButtonOptions<FullJQuery extends JQuery<?>> {
	public boolean disabled = false;

	public boolean text = true;

	public Object icons;// = { primary: null, secondary: null }

	public String label;

	public UIEventHandler<ButtonUI<FullJQuery>> create;

}
