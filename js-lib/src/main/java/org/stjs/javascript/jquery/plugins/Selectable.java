package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Selectable<FullJQuery extends JQuery<?>> {
	public FullJQuery selectable();

	public FullJQuery selectable(SelectableOptions<FullJQuery> options);

	public FullJQuery selectable(String methodName);

	public Object selectable(String option, String optionName);

	public FullJQuery selectable(String option, String optionName, Object value);

	public FullJQuery selectable(String option, DraggableOptions<FullJQuery> options);
}