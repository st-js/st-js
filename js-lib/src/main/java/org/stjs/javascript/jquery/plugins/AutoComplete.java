package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface AutoComplete<FullJQuery extends JQuery<?>> {
	public FullJQuery autocomplete();

	public FullJQuery autocomplete(AutoCompleteOptions<FullJQuery> options);

	public FullJQuery autocomplete(String methodName);

	public Object autocomplete(String option, String optionName);

	public FullJQuery autocomplete(String option, String optionName, Object value);

	public FullJQuery autocomplete(String option, DraggableOptions<FullJQuery> options);
}