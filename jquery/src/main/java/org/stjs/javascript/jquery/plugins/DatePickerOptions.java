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

import static org.stjs.javascript.JSCollections.$array;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.functions.Callback2;
import org.stjs.javascript.functions.Callback3;
import org.stjs.javascript.functions.Function1;
import org.stjs.javascript.functions.Function2;
import org.stjs.javascript.jquery.JQueryCore;

@SyntheticType
public class DatePickerOptions<FullJQuery extends JQueryCore<?>> {
	public boolean disabled = false;

	public Object altField = "";

	public String altFormat = "";

	public String appendText = "";

	public boolean autoSize = false;

	public String buttonImage = "";

	public boolean buttonImageOnly = false;

	public String buttonText = "...";

	public Object calculateWeek;// = $.datepicker.iso8601Week

	public boolean changeMonth = false;

	public boolean changeYear = false;

	public String closeText = "Done";

	public boolean constrainInput = true;

	public String currentText = "Today";

	public String dateFormat = "mm/dd/yy";

	public Array<String> dayNames = $array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

	public Array<String> dayNamesMin = $array("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa");

	public Array<String> dayNamesShort = $array("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

	public String defaultDate = null;

	public Object duration = "normal";

	public int firstDay = 0;

	public boolean gotoCurrent = false;

	public boolean hideIfNoPrevNext = false;

	public boolean isRTL = false;

	public Object maxDate = null;

	public Object minDate = null;

	public Array<String> monthNames = $array("January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December");

	public Array<String> monthNamesShort = $array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
			"Nov", "Dec");

	public boolean navigationAsDateFormat = false;

	public String nextText = "Next";

	public Object numberOfMonths = 1;

	public String prevText = "Prev";

	public boolean selectOtherMonths = false;

	public Object shortYearCutoff = "+10";

	public String showAnim = "show";

	public boolean showButtonPanel = false;

	public int showCurrentAtPos = 0;

	public boolean showMonthAfterYear = false;

	public String showOn = "focus";

	public Map<String, Object> showOptions;// = {}

	public boolean showOtherMonths = false;

	public boolean showWeek = false;

	public int stepMonths = 1;

	public String weekHeader = "Wk";

	public String yearRange = "c-10:c+10";

	public String yearSuffix = "";

	public UIEventHandler<DatePickerUI<FullJQuery>> create;

	public Function2<Element, FullJQuery, Object> beforeShow;

	public Function1<Date, Array<Object>> beforeShowDay;

	public Callback3<Integer, Integer, FullJQuery> onChangeMonthYear;

	public Callback2<String, FullJQuery> onClose;

	public Callback2<String, FullJQuery> onSelect;
}
