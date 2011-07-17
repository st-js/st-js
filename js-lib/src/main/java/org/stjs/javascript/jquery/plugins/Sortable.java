package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Sortable<FullJQuery extends JQuery<?>> {
	public FullJQuery sortable();

	public FullJQuery sortable(SortableOptions<FullJQuery> options);

	public FullJQuery sortable(String methodName);

	public Object sortable(String option, String optionName);

	public FullJQuery sortable(String option, String optionName, Object value);

	public FullJQuery sortable(String option, DraggableOptions<FullJQuery> options);
}