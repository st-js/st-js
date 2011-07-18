package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Dialog<FullJQuery extends JQuery<?>> {
	public FullJQuery dialog();

	public FullJQuery dialog(DialogOptions<FullJQuery> options);

	public FullJQuery dialog(String methodName);

	public Object dialog(String option, String optionName);

	public FullJQuery dialog(String option, String optionName, Object value);

	public FullJQuery dialog(String option, DraggableOptions<FullJQuery> options);
}
