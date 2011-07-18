package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Tabs<FullJQuery extends JQuery<?>> {
	public FullJQuery tabs();

	public FullJQuery tabs(TabsOptions<FullJQuery> options);

	public FullJQuery tabs(String methodName);

	public Object tabs(String option, String optionName);

	public FullJQuery tabs(String option, String optionName, Object value);

	public FullJQuery tabs(String option, DraggableOptions<FullJQuery> options);
}