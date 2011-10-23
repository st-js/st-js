/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface Datepicker<FullJQuery extends JQuery<?>> {
	public FullJQuery datepicker();

	public FullJQuery datepicker(DatePickerOptions<FullJQuery> options);

	public Object datepicker(String methodName);

	public Object datepicker(String option, String optionName);

	public FullJQuery datepicker(String option, String optionName, Object value);

	public FullJQuery datepicker(String option, DraggableOptions<FullJQuery> options);
}