package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Droppable<FullJQuery extends JQuery<?>> {
	public FullJQuery droppable();

	public FullJQuery droppable(DroppableOptions<FullJQuery> options);

	public FullJQuery droppable(String methodName);

	public Object droppable(String option, String optionName);

	public FullJQuery droppable(String option, String optionName, Object value);

	public FullJQuery droppable(String option, DraggableOptions<FullJQuery> options);
}