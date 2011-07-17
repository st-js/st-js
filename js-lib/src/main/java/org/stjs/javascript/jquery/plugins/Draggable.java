package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Draggable<FullJQuery extends JQuery<?>> {
	public FullJQuery draggable();

	public FullJQuery draggable(DraggableOptions<FullJQuery> options);

	public FullJQuery draggable(String methodName);

	public Object draggable(String option, String optionName);

	public FullJQuery draggable(String option, DraggableOptions<FullJQuery> options);

	public FullJQuery draggable(String option, String optionName, Object value);
}
