package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Resizable<FullJQuery extends JQuery<?>> {
	public FullJQuery resizable();

	public FullJQuery resizable(ResizableOptions<FullJQuery> options);

	public FullJQuery resizable(String methodName);

	public Object resizable(String option, String optionName);

	public FullJQuery resizable(String option, String optionName, Object value);

	public FullJQuery resizable(String option, DraggableOptions<FullJQuery> options);
}
