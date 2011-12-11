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
package org.stjs.javascript;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * this date is implemented using a java {@link Calendar}. the aim of this class is to offer a similar behavior to the
 * Javascript date.
 * 
 * @author acraciun
 * 
 */
public class Date {
	private Calendar calendar;
	private Calendar utc;
	private final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public Date() {
		calendar = Calendar.getInstance();
	}

	public Date(long milliseconds) {
		getUTC(true).setTimeInMillis(milliseconds);
	}

	public Date(String dateString) {
		// XXX in fact the format of the date seems to be platform dependent!. we use the standard format
		java.util.Date d = null;
		try {
			d = new SimpleDateFormat(DEFAULT_DATE_PATTERN).parse(dateString);
		} catch (ParseException e) {
		}
		if (d != null) {
			calendar = Calendar.getInstance();
			calendar.setTime(d);
		} else {
			// all the fiels should return NaN afterwards
			calendar = null;
		}
	}

	public Date(int year, int month, int day, int hours, int minutes, int seconds, int ms) {
		calendar = new GregorianCalendar(year, month, day, hours, minutes, seconds);
		calendar.set(Calendar.MILLISECOND, ms);
	}

	private Calendar getUTC(boolean forceCreate) {
		if (utc == null) {
			if (calendar != null) {
				utc = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				utc.setTimeInMillis(calendar.getTimeInMillis());
			} else if (forceCreate) {
				utc = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			}
		}
		return utc;
	}

	private Calendar getCalendar(boolean forceCreate) {
		if (calendar == null) {
			if (utc != null) {
				calendar = Calendar.getInstance();
				calendar.setTimeInMillis(utc.getTimeInMillis());
			} else if (forceCreate) {
				calendar = Calendar.getInstance();
			}
		}
		return calendar;
	}

	private double getField(Calendar c, int field) {
		if (c == null) {
			return Double.NaN;
		}
		return c.get(field);
	}

	private void setField(Calendar c, int field, int n) {
		if (c == null) {
			throw new IllegalStateException("The calendar shold not be null");
		}
		c.set(field, n);
		// set the other calendar to null to force recalculation
		if (c == calendar) {
			utc = null;
		} else {
			calendar = null;
		}
	}

	public double getDate() {
		return getField(getCalendar(false), Calendar.DAY_OF_MONTH);
	}

	public double getDay() {
		return getField(getCalendar(false), Calendar.DAY_OF_WEEK);
	}

	public double getFullYear() {
		return getField(getCalendar(false), Calendar.YEAR);
	}

	public double getHours() {
		return getField(getCalendar(false), Calendar.HOUR_OF_DAY);
	}

	public double getMilliseconds() {
		return getField(getCalendar(false), Calendar.MILLISECOND);
	}

	public double getMinutes() {
		return getField(getCalendar(false), Calendar.MINUTE);
	}

	public double getMonth() {
		return getField(getCalendar(false), Calendar.MONTH);
	}

	public double getSeconds() {
		return getField(getCalendar(false), Calendar.SECOND);
	}

	public double getTime() {
		if (getCalendar(false) != null) {
			return calendar.getTimeInMillis();
		}
		return Double.NaN;
	}

	public double getTimezoneOffset() {
		// in minutes
		if (getCalendar(false) != null) {
			return -calendar.getTimeZone().getRawOffset() / 1000 / 60;
		}
		return Double.NaN;
	}

	public double getUTCDate() {
		return getField(getUTC(false), Calendar.DAY_OF_MONTH);
	}

	public double getUTCDay() {
		return getField(getUTC(false), Calendar.DAY_OF_WEEK);
	}

	public double getUTCFullYear() {
		return getField(getUTC(false), Calendar.YEAR);
	}

	public double getUTCHours() {
		return getUTC(false).get(Calendar.HOUR_OF_DAY);
	}

	public double getUTCMilliseconds() {
		return getField(getUTC(false), Calendar.MILLISECOND);
	}

	public double getUTCMinutes() {
		return getField(getUTC(false), Calendar.MINUTE);
	}

	public double getUTCMonth() {
		return getField(getUTC(false), Calendar.MONTH);
	}

	public double getUTCSeconds() {
		return getField(getUTC(false), Calendar.SECOND);
	}

	public double getYear() {
		return getFullYear() - 1900;
	}

	public static double parse(String date) {
		return new Date(date).getTime();
	}

	public void setDate(int n) {
		setField(getCalendar(true), Calendar.DAY_OF_WEEK, n);
	}

	public void setFullYear(int n) {
		setField(getCalendar(true), Calendar.YEAR, n);
	}

	public void setHours(int n) {
		setField(getCalendar(true), Calendar.HOUR_OF_DAY, n);
	}

	public void setMilliseconds(int n) {
		setField(getCalendar(true), Calendar.MILLISECOND, n);
	}

	public void setMinutes(int n) {
		setField(getCalendar(true), Calendar.MINUTE, n);
	}

	public void setMonth(int n) {
		setField(getCalendar(true), Calendar.MONTH, n);
	}

	public void setSeconds(int n) {
		setField(getCalendar(true), Calendar.SECOND, n);
	}

	public void setTime(int n) {
		getCalendar(true).setTimeInMillis(n);
	}

	public void setUTCDate(int n) {
		setField(getUTC(true), Calendar.DAY_OF_MONTH, n);
	}

	public void setUTCFullYear(int n) {
		setField(getUTC(true), Calendar.YEAR, n);
	}

	public void setUTCHours(int n) {
		setField(getUTC(true), Calendar.HOUR_OF_DAY, n);
	}

	public void setUTCMilliseconds(int n) {
		setField(getUTC(true), Calendar.MILLISECOND, n);
	}

	public void setUTCMinutes(int n) {
		setField(getUTC(true), Calendar.MINUTE, n);
	}

	public void setUTCMonth(int n) {
		setField(getUTC(true), Calendar.MONTH, n);
	}

	public void setUTCSeconds(int n) {
		setField(getUTC(true), Calendar.SECOND, n);
	}

	public void setYear(int n) {
		setField(getUTC(true), Calendar.YEAR, n);
	}

	public String toDateString() {
		if (getCalendar(false) != null) {
			return DateFormat.getDateInstance(DateFormat.FULL, Locale.US).format(calendar.getTime());
		}
		return null;
	}

	public String toGMTString() {
		return toUTCString();
	}

	public String toLocaleDateString() {
		if (getCalendar(false) != null) {
			return DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault()).format(calendar.getTime());
		}
		return null;
	}

	public String toLocaleTimeString() {
		if (getCalendar(false) != null) {
			return DateFormat.getTimeInstance(DateFormat.FULL, Locale.getDefault()).format(calendar.getTime());
		}
		return null;
	}

	public String toLocaleString() {
		if (getCalendar(false) != null) {
			return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.getDefault()).format(
					calendar.getTime());
		}
		return null;
	}

	@Override
	public String toString() {
		if (getCalendar(false) != null) {
			return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US).format(
					calendar.getTime());
		}
		return null;
	}

	public String toTimeString() {
		if (getCalendar(false) != null) {
			return DateFormat.getTimeInstance(DateFormat.FULL, Locale.US).format(calendar.getTime());
		}
		return null;
	}

	public String toUTCString() {
		if (getUTC(false) != null) {
			return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US).format(utc.getTime());
		}
		return null;
	}

	public static double UTC(int year, int month, int day, int hours, int minutes, int seconds, int ms) {
		return new Date(year, month, day, hours, minutes, seconds, ms).getTime();
	}

	public double valueOf() {
		return getTime();
	}
}
