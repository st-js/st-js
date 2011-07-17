package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Accordion<FullJQuery extends JQuery<?>> {
	public FullJQuery accordion();

	public FullJQuery accordion(AccordionOptions<FullJQuery> options);

	public FullJQuery accordion(String methodName);

	public Object accordion(String option, String optionName);

	public FullJQuery accordion(String option, String optionName, Object value);

	public FullJQuery accordion(String option, DraggableOptions<FullJQuery> options);
}