package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Progressbar<FullJQuery extends JQuery<?>> {
	public FullJQuery progressbar();

	public FullJQuery progressbar(ProgressbarOptions<FullJQuery> options);

	public FullJQuery progressbar(String methodName);

	public Object progressbar(String option, String optionName);

	public FullJQuery progressbar(String option, String optionName, Object value);

	public FullJQuery progressbar(String option, DraggableOptions<FullJQuery> options);
}