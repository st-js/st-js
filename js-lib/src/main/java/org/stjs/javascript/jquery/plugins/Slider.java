package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Slider<FullJQuery extends JQuery<?>> {
	public FullJQuery slider();

	public FullJQuery slider(SliderOptions<FullJQuery> options);

	public FullJQuery slider(String methodName);

	public Object slider(String option, String optionName);

	public FullJQuery slider(String option, String optionName, Object value);

	public FullJQuery slider(String option, DraggableOptions<FullJQuery> options);
}
