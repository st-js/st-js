package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Button<FullJQuery extends JQuery<?>> {
	public FullJQuery button();

	public FullJQuery button(ButtonOptions<FullJQuery> options);

	public FullJQuery button(String methodName);

	public Object button(String option, String optionName);

	public FullJQuery button(String option, String optionName, Object value);

	public FullJQuery button(String option, DraggableOptions<FullJQuery> options);
}