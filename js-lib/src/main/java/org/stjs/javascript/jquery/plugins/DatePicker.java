package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface DatePicker<FullJQuery extends JQuery<?>> {
	public FullJQuery datepicker();

	public FullJQuery datepicker(DatePickerOptions<FullJQuery> options);

	public FullJQuery datepicker(String methodName);

	public Object datepicker(String option, String optionName);

	public FullJQuery datepicker(String option, String optionName, Object value);

	public FullJQuery datepicker(String option, DraggableOptions<FullJQuery> options);
}