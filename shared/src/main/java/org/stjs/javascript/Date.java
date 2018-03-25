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

import org.stjs.javascript.annotation.BrowserCompatibility;

/**
 * this date is implemented using a java {@link java.util.Calendar}. the aim of this class is to offer a similar behavior to the
 * Javascript date.
 *
 *
 * <h3>Time Values and Time Range</h3>
 *
 * <p>A Date object contains a Number indicating a particular instant in time to
 * within a millisecond. Such a Number is called a time value. A time value may
 * also be NaN, indicating that the Date object does not represent a specific
 * instant of time.
 *
 * <p>Time is measured in ECMAScript in milliseconds since 01 January, 1970 UTC.
 * In time values leap seconds are ignored. It is assumed that there are exactly
 * 86,400,000 milliseconds per day. ECMAScript Number values can represent all
 * integers from –9,007,199,254,740,992 to 9,007,199,254,740,992; this range
 * suffices to measure times to millisecond precision for any instant that is
 * within approximately 285,616 years, either forward or backward, from 01
 * January, 1970 UTC.
 *
 * <p>The actual range of times supported by ECMAScript Date objects is slightly
 * smaller: exactly –100,000,000 days to 100,000,000 days measured relative to
 * midnight at the beginning of 01 January, 1970 UTC. This gives a range of
 * 8,640,000,000,000,000 milliseconds to either side of 01 January, 1970 UTC.
 *
 * <p>The exact moment of midnight at the beginning of 01 January, 1970 UTC is
 * represented by the value +0.
 *
 *
 * <h3>Date Time String Format</h3>
 *
 * <p>ECMAScript defines a string interchange format for date-times based upon a
 * simplification of the ISO 8601 Extended Format. The format is as follows:
 * <strong>YYYY-MM-DDTHH:mm:ss.sssZ</strong>
 *
 * <p>Where the fields are as follows:
 * <ul>
 * <li><strong>YYYY</strong> is the decimal digits of the year 0000 to 9999 in the Gregorian calendar.
 * <li><strong>-</strong> “-” (hyphen) appears literally twice in the string.
 * <li><strong>MM</strong> is the month of the year from 01 (January) to 12 (December).
 * <li><strong>DD</strong> is the day of the month from 01 to 31.
 * <li><strong>T</strong> “T” appears literally in the string, to indicate the beginning of the time element.
 * <li><strong>HH</strong> is the number of complete hours that have passed since midnight as two decimal digits from 00 to 24.
 * <li><strong>:</strong> “:” (colon) appears literally twice in the string.
 * <li><strong>mm</strong> is the number of complete minutes since the start of the hour as two decimal digits from 00 to 59.
 * <li><strong>ss</strong> is the number of complete seconds since the start of the minute as two decimal digits from 00 to 59.
 * <li><strong>.</strong> “.” (dot) appears literally in the string.
 * <li><strong>sss</strong>	is the number of complete milliseconds since the start of the second as three decimal digits.
 * <li><strong>Z</strong> is the time zone offset specified as “Z” (for UTC) or either “+” or “-” followed by a time expression <tt>HH:mm</tt>
 * </ul>
 *
 * <p>This format includes date-only forms:
 * <ul>
 * <li><tt>YYYY</tt>
 * <li><tt>YYYY-MM</tt>
 * <li><tt>YYYY-MM-DD</tt>
 * </ul>
 *
 * <p>It also includes “date-time” forms that consist of one of the above date-only
 * forms immediately followed by one of the following time forms with an optional
 * time zone offset appended:
 * <ul>
 * <li><tt>THH:mm</tt>
 * <li><tt>THH:mm:ss</tt>
 * <li><tt>THH:mm:ss.sss</tt>
 * </ul>
 *
 * <p>All numbers must be base 10. If the <tt>MM</tt> or <tt>DD</tt> fields are
 * absent <tt>“01”</tt> is used as the value. If the <tt>HH</tt>, <tt>mm</tt>, or
 * <tt>ss</tt> fields are absent <tt>“00”</tt> is used as the value and the value
 * of an absent <tt>sss</tt> field is <tt>“000”</tt>. The value of an absent time
 * zone offset is <tt>“Z”</tt>.
 *
 * <p>Illegal values (out-of-bounds as well as syntax errors) in a format string
 * means that the format string is not a valid instance of this format.
 *
 * <p><strong>NOTE 1:</strong> As every day both starts and ends with midnight, the
 * two notations <tt>00:00</tt> and <tt>24:00</tt> are available to distinguish
 * the two midnights that can be associated with one date. This means that the
 * following two notations refer to exactly the same point in time:
 * <tt>1995-02-04T24:00</tt> and <tt>1995-02-05T00:00</tt>
 *
 * <p><strong>NOTE 2:</strong> There exists no international standard that
 * specifies abbreviations for civil time zones like CET, EST, etc. and sometimes
 * the same abbreviation is even used for two very different time zones. For this
 * reason, ISO 8601 and this format specifies numeric representations of date and
 * time.
 *
 * The documentation of this class is mostly adapted from the ECMAScript 5.1 Specification: http://www.ecma-international.org/ecma-262/5.1/
 * Browser compatibility information comes from: http://kangax.github.io/es5-compat-table
 *
 * @version $Id: $Id
 */
public class Date {
	private Calendar calendar;
	private Calendar utc;
	private final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Constructs a new <tt>Date</tt> object, setting it to the time value
	 * identifying the current time.
	 */
	public Date() {
		calendar = Calendar.getInstance();
	}

	/**
	 * Constructs a new <tt>Date</tt> object, setting it to the specified time
	 * value. If the specified time value (see class documentation) is greater
	 * than the maximum time value, it is instead set to <tt>NaN</tt>.
	 *
	 * @param milliseconds the time value to set this date to
	 */
	public Date(double milliseconds) {
		if(!Double.isNaN(milliseconds) && 
				!Double.isInfinite(milliseconds) &&
				milliseconds <= 8640000000000000d &&
				milliseconds >= -8640000000000000d) {
			getUTC(true).setTimeInMillis((long)milliseconds);
		}
	}

	/**
	 * Constructs a new <tt>Date</tt> object by parsing its string representation,
	 * and setting it's value to the result of calling the <tt>parse(String)</tt>
	 * with the supplied argument. <tt>dateString</tt> is expected to be formatted
	 * as an ECMAScript date-times string interchange format (see "Date Time
	 * String format" in the class documentation).
	 *
	 * @param dateString a string containing a formatted date
	 */
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
	
	/**
	 * Constructs a <tt>Date</tt> from the given fields that specify a date and
	 * time in the local time zone, with the <tt>day</tt> field set to 1, and the
	 * <tt>hours</tt>, <tt>minutes</tt>, <tt>seconds</tt> and <tt>milliseconds</tt>
	 * fields set to 0.
	 *
	 * @param year a int.
	 * @param month a int.
	 */
	public Date(int year, int month) {
		this(year, month, 1);
	}
	
	/**
	 * Constructs a <tt>Date</tt> from the given fields that specify a date and
	 * time in the local time zone, with the <tt>hours</tt>, <tt>minutes</tt>,
	 * <tt>seconds</tt> and <tt>milliseconds</tt> fields set to 0.
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param day a int.
	 */
	public Date(int year, int month, int day) {
		this(year, month, day, 0);
	}
		
	/**
	 * Constructs a <tt>Date</tt> from the given fields that specify a date and
	 * time in the local time zone, with the <tt>minutes</tt>,<tt>seconds</tt> and
	 * <tt>milliseconds</tt> fields set to 0.
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param day a int.
	 * @param hours a int.
	 */
	public Date(int year, int month, int day, int hours) {
		this(year, month, day, hours, 0);
	}
	
	/**
	 * Constructs a <tt>Date</tt> from the given fields that specify a date and
	 * time in the local time zone, with the <tt>seconds</tt> and
	 * <tt>milliseconds</tt> fields set to 0.
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param day a int.
	 * @param hours a int.
	 * @param minutes a int.
	 */
	public Date(int year, int month, int day, int hours, int minutes) {
		this(year, month, day, hours, minutes, 0);
	}
	
	/**
	 * Constructs a <tt>Date</tt> from the given fields that specify a date and
	 * time in the local time zone, with the <tt>milliseconds</tt> field set to 0.
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param day a int.
	 * @param hours a int.
	 * @param minutes a int.
	 * @param seconds a int.
	 */
	public Date(int year, int month, int day, int hours, int minutes, int seconds) {
		this(year, month, day, hours, minutes, seconds, 0);
	}
	
	/**
	 * Constructs a <tt>Date</tt> from the given fields that specify a date and
	 * time in the local time zone.
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param day a int.
	 * @param hours a int.
	 * @param minutes a int.
	 * @param seconds a int.
	 * @param ms a int.
	 */
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

	/**
	 * Returns the day of the month corresponding to this <tt>Date</tt>'s time value,
	 * in the local time zone.
	 *
	 * @return the day of the month corresponding to this <tt>Date</tt>'s time value, in the local time zone.
	 */
	public double getDate() {
		return getField(getCalendar(false), Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns the day of the week corresponding to this <tt>Date</tt>'s time
	 * value, in the local time zone. A weekday value of 0 specifies Sunday; 1
	 * specifies Monday; 2 specifies Tuesday; 3 specifies Wednesday; 4
	 * specifies Thursday; 5 specifies Friday; and 6 specifies Saturday
	 *
	 * @return the day of the week corresponding to this <tt>Date</tt>'s time value, in the local time zone.
	 */
	public double getDay() {
		return getField(getCalendar(false), Calendar.DAY_OF_WEEK);
	}

	/**
	 * Returns the year corresponding to this <tt>Date</tt>'s time value, in
	 * the local time zone.
	 *
	 * @return the year corresponding to this <tt>Date</tt>'s time value, in the local time zone.
	 */
	public double getFullYear() {
		return getField(getCalendar(false), Calendar.YEAR);
	}

	/**
	 * Returns the hour of the day corresponding to this <tt>Date</tt>'s time
	 * value, in the local time zone.
	 *
	 * @return the hour of the day corresponding to this <tt>Date</tt>'s time value, in the local time zone.
	 */
	public double getHours() {
		return getField(getCalendar(false), Calendar.HOUR_OF_DAY);
	}

	/**
	 * Returns the milliseconds of the second corresponding to this <tt>Date</tt>'s time
	 * value, in the local time zone.
	 *
	 * @return the milliseconds of the second corresponding to this <tt>Date</tt>'s time value, in the local time zone.
	 */
	public double getMilliseconds() {
		return getField(getCalendar(false), Calendar.MILLISECOND);
	}

	/**
	 * Returns the minutes of the hour corresponding to this <tt>Date</tt>'s time
	 * value, in the local time zone.
	 *
	 * @return the minutes of the hour corresponding to this <tt>Date</tt>'s time value, in the local time zone.
	 */
	public double getMinutes() {
		return getField(getCalendar(false), Calendar.MINUTE);
	}

	/**
	 * Returns the month number corresponding to this <tt>Date</tt>'s time value,
	 * in the local time zone. Month numbers are 0 indexed, with January being 0
	 * and December being 11.
	 *
	 * @return Returns the month number in which this <tt>Date</tt> is placed, in the local time zone
	 */
	public double getMonth() {
		return getField(getCalendar(false), Calendar.MONTH);
	}

	/**
	 * Returns the seconds of the minute corresponding to this <tt>Date</tt>'s time
	 * value, in the local time zone.
	 *
	 * @return the seconds of the minute corresponding to this <tt>Date</tt>'s time value, in the local time zone.
	 */
	public double getSeconds() {
		return getField(getCalendar(false), Calendar.SECOND);
	}

	/**
	 * Returns the time value associated to this <tt>Date</tt>
	 *
	 * @return the time value associated to this <tt>Date</tt>
	 */
	public double getTime() {
		if (getCalendar(false) != null) {
			return calendar.getTimeInMillis();
		}
		return Double.NaN;
	}

	/**
	 * Returns the difference between local time and UTC time in minutes.
	 *
	 * @return the difference between local time and UTC time in minutes.
	 */
	public double getTimezoneOffset() {
		// in minutes
		if (getCalendar(false) != null) {
			return -calendar.getTimeZone().getRawOffset() / 1000 / 60;
		}
		return Double.NaN;
	}

	/**
	 * Returns the day of the month corresponding to this <tt>Date</tt>'s time value,
	 * in UTC.
	 *
	 * @return the day of the month corresponding to this <tt>Date</tt>'s time value, in UTC.
	 */
	public double getUTCDate() {
		return getField(getUTC(false), Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns the day of the week corresponding to this <tt>Date</tt>'s time
	 * value, in UTC. A weekday value of 0 specifies Sunday; 1
	 * specifies Monday; 2 specifies Tuesday; 3 specifies Wednesday; 4
	 * specifies Thursday; 5 specifies Friday; and 6 specifies Saturday
	 *
	 * @return the day of the week corresponding to this <tt>Date</tt>'s time value, in UTC.
	 */
	public double getUTCDay() {
		return getField(getUTC(false), Calendar.DAY_OF_WEEK);
	}

	/**
	 * Returns the year corresponding to this <tt>Date</tt>'s time value, in UTC.
	 *
	 * @return the year corresponding to this <tt>Date</tt>'s time value, in UTC.
	 */
	public double getUTCFullYear() {
		return getField(getUTC(false), Calendar.YEAR);
	}

	/**
	 * Returns the hour of the day corresponding to this <tt>Date</tt>'s time
	 * value, in UTC.
	 *
	 * @return the hour of the day corresponding to this <tt>Date</tt>'s time value, in UTC.
	 */
	public double getUTCHours() {
		return getUTC(false).get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Returns the milliseconds of the second corresponding to this <tt>Date</tt>'s time
	 * value, in UTC.
	 *
	 * @return the milliseconds of the second corresponding to this <tt>Date</tt>'s time value, in UTC.
	 */
	public double getUTCMilliseconds() {
		return getField(getUTC(false), Calendar.MILLISECOND);
	}

	/**
	 * Returns the minutes of the hour corresponding to this <tt>Date</tt>'s time
	 * value, in UTC.
	 *
	 * @return the minutes of the hour corresponding to this <tt>Date</tt>'s time value, in UTC.
	 */
	public double getUTCMinutes() {
		return getField(getUTC(false), Calendar.MINUTE);
	}

	/**
	 * Returns the month number corresponding to this <tt>Date</tt>'s time value,
	 * in UTC. Month numbers are 0 indexed, with January being 0 and December
	 * being 11.
	 *
	 * @return Returns the month number in which this <tt>Date</tt> is placed, in UTC
	 */
	public double getUTCMonth() {
		return getField(getUTC(false), Calendar.MONTH);
	}

	/**
	 * Returns the seconds of the minute corresponding to this <tt>Date</tt>'s time
	 * value, in UTC.
	 *
	 * @return the seconds of the minute corresponding to this <tt>Date</tt>'s time value, in UTC.
	 */
	public double getUTCSeconds() {
		return getField(getUTC(false), Calendar.SECOND);
	}

	/**
	 * Returns <tt>getFullYear() - 1900</tt>
	 *
	 * @deprecated use <tt>getFullYear()</tt> or <tt>getUTCFullYear()</tt> instead
	 * @return <tt>getFullYear() - 1900</tt>
	 */
	@Deprecated
	public double getYear() {
		return getFullYear() - 1900;
	}

	/**
	 * Interprets the specified <tt>String</tt> as a date and time, and returns
	 * the corresponding UTC time value corresponding to the date and time.
	 *
	 * <p>The <tt>String</tt> may be interpreted as a local time, a UTC time, or
	 * a time in some other time zone, depending on the contents of the
	 * <tt>String</tt>. The function first attempts to parse the format of the
	 * <tt>String</tt> according to the rules called out in "Date Time String
	 * Format" (see class documentation). If the <tt>String</tt> does not conform
	 * to that format the function may fall back to any implementation-specific
	 * heuristics or implementation-specific date formats. Unrecognisable
	 * <tt>Strings</tt> or dates containing illegal element values in the format
	 * <tt>String</tt> shall cause <tt>Date.parse</tt> to return <tt>NaN</tt>.
	 *
	 * <p>If <tt>x</tt> is any <tt>Date</tt> object whose milliseconds amount is
	 * zero within a particular implementation of ECMAScript, then all of the
	 * following expressions should produce the same numeric value in that
	 * implementation, if all the properties referenced have their initial
	 * values:
	 * <ul>
	 * <li><tt>x.valueOf()</tt>
	 * <li><tt>Date.parse(x.toString())</tt>
	 * <li><tt>Date.parse(x.toUTCString())</tt>
	 * <li><tt>Date.parse(x.toISOString())</tt>
	 * </ul>
	 *
	 * However, the expression <tt>Date.parse(x.toLocaleString())</tt> is not
	 * required to produce the same <tt>Number</tt> value as the preceding three
	 * expressions and, in general, the value produced by <tt>Date.parse</tt> is
	 * implementation-dependent when given any String value that does not
	 * conform to the Date Time String Format (15.9.1.15) and that could not
	 * be produced in that implementation by the <tt>toString</tt> or
	 * <tt>toUTCString</tt> method.
	 *
	 * @param date a {@link java.lang.String} object.
	 * @return a double.
	 */
	@BrowserCompatibility("IE:7+")
	public static double parse(String date) {
		return new Date(date).getTime();
	}

	/**
	 * Sets the day of month field of this <tt>Date</tt> interpreted in the
	 * local time zone.
	 *
	 * @param day the new day of month
	 */
	public void setDate(int day) {
		setField(getCalendar(true), Calendar.DAY_OF_MONTH, day);
	}

	/**
	 * Sets the month of year field of this <tt>Date</tt> interpreted in the
	 * local time zone. Month numbers are 0 indexed, with January being 0
	 * and December being 11.
	 *
	 * @param month the new month
	 */
	public void setMonth(int month) {
		setMonth(month, (int)getDay());
	}
	
	/**
	 * Sets the month of year and day of month fields of this <tt>Date</tt>
	 * interpreted in the local time zone. Month numbers are 0 indexed, with
	 * January being 0 and December being 11.
	 *
	 * @param month the new month
	 * @param day the new day of month
	 */
	public void setMonth(int month, int day) {
		setField(getCalendar(true), Calendar.MONTH, month);
		setDate(day);
	}

	/**
	 * Sets the year field of this <tt>Date</tt>
	 * interpreted in the local time zone.
	 *
	 * @param year the new year
	 */
	public void setFullYear(int year) {
		setFullYear(year, (int)getMonth());
	}

	/**
	 * Sets the year and month fields of this <tt>Date</tt>
	 * interpreted in the local time zone.
	 *
	 * @param year the new year
	 * @param month the new month
	 */
	public void setFullYear(int year, int month) {
		setFullYear(year, month, (int)getDate());
	}

	/**
	 * Sets the year, month and day of month fields of this <tt>Date</tt>
	 * interpreted in the local time zone.
	 *
	 * @param year the new year
	 * @param month the new month
	 * @param day the new day of month
	 */
	public void setFullYear(int year, int month, int day) {
		setField(getCalendar(true), Calendar.YEAR, year);
		setMonth(month, day);
	}

	/**
	 * Sets the milliseconds field of this <tt>Date</tt> interpreted in the local time zone.
	 *
	 * @param ms the new milliseconds
	 */
	public void setMilliseconds(int ms) {
		setField(getCalendar(true), Calendar.MILLISECOND, ms);
	}

	/**
	 * Sets the seconds field of this <tt>Date</tt> interpreted in the local
	 * time zone.
	 *
	 * @param sec the new seconds
	 */
	public void setSeconds(int sec) {
		setSeconds(sec, (int)getMilliseconds());
	}
	
	/**
	 * Sets the seconds and milliseconds fields of this <tt>Date</tt>
	 * interpreted in the local time zone.
	 *
	 * @param sec the new seconds
	 * @param ms the new milliseconds
	 */
	public void setSeconds(int sec, int ms) {
		setField(getCalendar(true), Calendar.SECOND, sec);
		setMilliseconds(ms);
	}
	
	/**
	 * Sets the minutes field of this <tt>Date</tt> interpreted in the local
	 * time zone.
	 *
	 * @param min the new minutes
	 */
	public void setMinutes(int min) {
		setMinutes(min, (int)getSeconds());
	}
	
	/**
	 * Sets the minutes and seconds field of this <tt>Date</tt> interpreted in
	 * the local time zone.
	 *
	 * @param min the new minutes
	 * @param sec the new seconds
	 */
	public void setMinutes(int min, int sec) {
		setMinutes(min, sec, (int)getMilliseconds());
	}
	
	/**
	 * Sets the minutes, seconds and milliseconds field of this <tt>Date</tt>
	 * interpreted in the local time zone.
	 *
	 * @param min the new minutes
	 * @param sec the new seconds
	 * @param ms the new milliseconds
	 */
	public void setMinutes(int min, int sec, int ms) {
		setField(getCalendar(true), Calendar.MINUTE, min);
		setSeconds(sec, ms);
	}

	/**
	 * Sets the hours field of this <tt>Date</tt> interpreted in the local
	 * time zone.
	 *
	 * @param hour the new hour
	 */
	public void setHours(int hour) {
		setHours(hour, (int)getMinutes());
	}
	
	/**
	 * Sets the hours and minutes field of this <tt>Date</tt> interpreted in
	 * the local time zone.
	 *
	 * @param hour the new hour
	 * @param min the new minutes
	 */
	public void setHours(int hour, int min) {
		setHours(hour, min, (int)getSeconds());
	}
	
	/**
	 * Sets the hours, minutes and seconds field of this <tt>Date</tt>
	 * interpreted in the local time zone.
	 *
	 * @param hour the new hour
	 * @param min the new minutes
	 * @param sec the new seconds
	 */
	public void setHours(int hour, int min, int sec) {
		setHours(hour, min, sec, (int)getMilliseconds());
	}
	
	/**
	 * Sets the hours, minutes, seconds and milliseconds field of this
	 * <tt>Date</tt> interpreted in the local time zone.
	 *
	 * @param hour the new hour
	 * @param min the new minutes
	 * @param sec the new seconds
	 * @param ms the new milliseconds
	 */
	public void setHours(int hour, int min, int sec, int ms) {
		setField(getCalendar(true), Calendar.HOUR_OF_DAY, hour);
		setMinutes(min, sec, ms);
	}

	/**
	 * Sets the time value of this <tt>Date</tt> directly
	 *
	 * @param time the new time value for this <tt>Date</tt>
	 */
	public void setTime(long time) {
		getCalendar(true).setTimeInMillis(time);
	}

	/**
	 * Sets the milliseconds field of this <tt>Date</tt> interpreted in UTC.
	 *
	 * @param ms the new milliseconds
	 */
	public void setUTCMilliseconds(int ms) {
		setField(getUTC(true), Calendar.MILLISECOND, ms);
	}
	
	/**
	 * Sets the seconds field of this <tt>Date</tt> interpreted in UTC.
	 *
	 * @param sec the new seconds
	 */
	public void setUTCSeconds(int sec) {
		setUTCSeconds(sec, (int)getUTCMilliseconds());
	}
	
	/**
	 * Sets the seconds and milliseconds fields of this <tt>Date</tt>
	 * interpreted in UTC.
	 *
	 * @param sec the new seconds
	 * @param ms the new milliseconds
	 */
	public void setUTCSeconds(int sec, int ms) {
		setField(getUTC(true), Calendar.SECOND, sec);
		setUTCMilliseconds(ms);
	}
	
	/**
	 * Sets the minutes field of this <tt>Date</tt> interpreted in UTC.
	 *
	 * @param min the new minutes
	 */
	public void setUTCMinutes(int min) {
		setUTCMinutes(min, (int)getUTCSeconds());
	}
	
	/**
	 * Sets the minutes and seconds field of this <tt>Date</tt> interpreted in
	 * UTC.
	 *
	 * @param min the new minutes
	 * @param sec the new seconds
	 */
	public void setUTCMinutes(int min, int sec) {
		setUTCMinutes(min, sec, (int)getUTCMilliseconds());
	}
	
	/**
	 * Sets the minutes, seconds and milliseconds field of this <tt>Date</tt>
	 * interpreted in UTC.
	 *
	 * @param min the new minutes
	 * @param sec the new seconds
	 * @param ms the new milliseconds
	 */
	public void setUTCMinutes(int min, int sec, int ms) {
		setField(getUTC(true), Calendar.MINUTE, min);
		setUTCSeconds(sec, ms);
	}
	
	/**
	 * Sets the hours field of this <tt>Date</tt> interpreted in UTC.
	 *
	 * @param hour the new hour
	 */
	public void setUTCHours(int hour) {
		setUTCHours(hour, (int)getUTCMinutes());
	}
	
	/**
	 * Sets the hours and minutes field of this <tt>Date</tt> interpreted in
	 * UTC.
	 *
	 * @param hour the new hour
	 * @param min the new minutes
	 */
	public void setUTCHours(int hour, int min) {
		setUTCHours(hour, min, (int)getUTCSeconds());
	}
	
	/**
	 * Sets the hours, minutes and seconds field of this <tt>Date</tt>
	 * interpreted in UTC.
	 *
	 * @param hour the new hour
	 * @param min the new minutes
	 * @param sec the new seconds
	 */
	public void setUTCHours(int hour, int min, int sec) {
		setUTCHours(hour, min, sec, (int)getUTCMilliseconds());
	}
	
	/**
	 * Sets the hours, minutes, seconds and milliseconds field of this
	 * <tt>Date</tt> interpreted in UTC.
	 *
	 * @param hour the new hour
	 * @param min the new minutes
	 * @param sec the new seconds
	 * @param ms the new milliseconds
	 */
	public void setUTCHours(int hour, int min, int sec, int ms) {
		setField(getUTC(true), Calendar.HOUR_OF_DAY, hour);
		setUTCMinutes(min, sec, ms);
	}

	/**
	 * Sets the day of month field of this <tt>Date</tt> interpreted in UTC.
	 *
	 * @param n the new day of month
	 */
	public void setUTCDate(int n) {
		setField(getUTC(true), Calendar.DAY_OF_MONTH, n);
	}
	
	/**
	 * Sets the month of year field of this <tt>Date</tt> interpreted in UTC.
	 * Month numbers are 0 indexed, with January being 0 and December being 11.
	 *
	 * @param month the new month
	 */
	public void setUTCMonth(int month) {
		setUTCMonth(month, (int)getUTCDay());
	}
	
	/**
	 * Sets the month of year and day of month fields of this <tt>Date</tt>
	 * interpreted in UTC. Month numbers are 0 indexed, with
	 * January being 0 and December being 11.
	 *
	 * @param month the new month
	 * @param day the new day of month
	 */
	public void setUTCMonth(int month, int day) {
		setField(getUTC(true), Calendar.MONTH, month);
		setUTCDate(day);
	}
	
	/**
	 * Sets the year field of this <tt>Date</tt>
	 * interpreted in UTC.
	 *
	 * @param year the new year
	 */
	public void setUTCFullYear(int year) {
		setUTCFullYear(year, (int)getUTCMonth());
	}

	/**
	 * Sets the year and month fields of this <tt>Date</tt>
	 * interpreted in UTC.
	 *
	 * @param year the new year
	 * @param month the new month
	 */
	public void setUTCFullYear(int year, int month) {
		setUTCFullYear(year, month, (int)getUTCDate());
	}

	/**
	 * Sets the year, month and day of month fields of this <tt>Date</tt>
	 * interpreted in UTC.
	 *
	 * @param year the new year
	 * @param month the new month
	 * @param day the new day of month
	 */
	public void setUTCFullYear(int year, int month, int day) {
		setField(getUTC(true), Calendar.YEAR, year);
		setUTCMonth(month, day);
	}
	
	/**
	 * Sets the year field of this date in UTC. If <tt>0 &lt;= year &lt;= 99</tt>,
	 * then the year is actually set to <tt>1900 + year</tt>, otherwise it is
	 * directly set to the specified year.
	 *
	 * @deprecated use <tt>setFullYear</tt> or <tt>setUTCFullYear()</tt> instead
	 * @param year a int.
	 */
	@Deprecated()
	public void setYear(int year) {
		if(year >= 0 && year <= 99){
			year += 1900;
		}
		setField(getUTC(true), Calendar.YEAR, year);
	}

	/**
	 * Returns a <tt>String</tt> representation of this <tt>Date</tt>.
	 * The contents of the <tt>String</tt> are implementation-dependent, but
	 * are intended to represent the <tt>Date</tt> in GMT in
	 * a convenient, human-readable form.
	 *
	 * @deprecated use <tt>toUTCString()</tt> instead
	 * @return a human readable version of this <tt>Date</tt> in UTC
	 */
	@Deprecated
	public String toGMTString() {
		return toUTCString();
	}

	/**
	 * Returns a <tt>String</tt> representation of the "date" portion of this
	 * <tt>Date</tt> (ie: without time) in the current time zone and in the host
	 * environment's current locale. The contents of the String are
	 * implementation-dependent, but are intended to be in a convenient,
	 * human-readable form.
	 *
	 * @return a human readable version of the "date" portion of this <tt>Date</tt> in the current locale
	 */
	public String toLocaleDateString() {
		if (getCalendar(false) != null) {
			return DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault()).format(calendar.getTime());
		}
		return null;
	}

	/**
	 * Returns a <tt>String</tt> representation of the "time" portion of this
	 * <tt>Date</tt> (ie: without date) in the current time zone and in the host
	 * environment's current locale.  The contents of the <tt>String</tt> are
	 * implementation-dependent, but are intended to be in a convenient,
	 * human-readable form.
	 *
	 * @return a human readable version of the "time" portion of this <tt>Date</tt> in the current locale
	 */
	public String toLocaleTimeString() {
		if (getCalendar(false) != null) {
			return DateFormat.getTimeInstance(DateFormat.FULL, Locale.getDefault()).format(calendar.getTime());
		}
		return null;
	}

	/**
	 * Returns a <tt>String</tt> representation of this <tt>Date</tt> in the
	 * host environment's current locale. The contents of the <tt>String</tt>
	 * are implementation-dependent, but are intended to represent the
	 * <tt>Date</tt> in the current time zone in a convenient, human-readable
	 * form.
	 *
	 * <strong>NOTE:</strong> For any Date value <tt>d</tt> whose milliseconds
	 * amount is zero, the result of <tt>Date.parse(d.toString())</tt> is equal
	 * to <tt>d.valueOf()</tt>. See 15.9.4.2.
	 *
	 * @return a human readable version of this <tt>Date</tt>, in the current locale
	 */
	public String toLocaleString() {
		if (getCalendar(false) != null) {
			return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.getDefault()).format(
					calendar.getTime());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Returns a <tt>String</tt> representation of this <tt>Date</tt>.
	 * The contents of the <tt>String</tt> are implementation-dependent, but
	 * are intended to represent the <tt>Date</tt> in the current time zone in
	 * a convenient, human-readable form.
	 *
	 * <strong>NOTE:</strong> For any Date value <tt>d</tt> whose milliseconds
	 * amount is zero, the result of <tt>Date.parse(d.toString())</tt> is equal
	 * to <tt>d.valueOf()</tt>. See 15.9.4.2.
	 */
	@Override
	public String toString() {
		if (getCalendar(false) != null) {
			return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US).format(
					calendar.getTime());
		}
		return null;
	}
	
	/**
	 * Returns a <tt>String</tt> representation of the "date" portion of this
	 * <tt>Date</tt> (ie: without time) in the current time zone. The contents
	 * of the String are implementation-dependent, but are intended to be in a
	 * convenient, human-readable form.
	 *
	 * @return a human readable version of the "date" portion of this <tt>Date</tt>, in the local time zone
	 */
	public String toDateString(){
		if (getCalendar(false) != null) {
			return DateFormat.getDateInstance(DateFormat.FULL, Locale.US).format(calendar.getTime());
		}
		return null;
	}

	/**
	 * Returns a <tt>String</tt> representation of the "time" portion of this
	 * <tt>Date</tt> (ie: without date) in the current time zone.  The contents
	 * of the <tt>String</tt> are implementation-dependent, but are intended to
	 * be in a convenient, human-readable form.
	 *
	 * @return a human readable version of the "time" portion of this <tt>Date</tt>, in the local time zone
	 */
	public String toTimeString() {
		if (getCalendar(false) != null) {
			return DateFormat.getTimeInstance(DateFormat.FULL, Locale.US).format(calendar.getTime());
		}
		return null;
	}

	/**
	 * Returns a <tt>String</tt> representation of this <tt>Date</tt>.
	 * The contents of the <tt>String</tt> are implementation-dependent, but
	 * are intended to represent the <tt>Date</tt> in UTC in
	 * a convenient, human-readable form.
	 *
	 * <strong>NOTE:</strong> For any Date value <tt>d</tt> whose milliseconds
	 * amount is zero, the result of <tt>Date.parse(d.toString())</tt> is equal
	 * to <tt>d.valueOf()</tt>. See 15.9.4.2.
	 *
	 * @return a human readable version of this <tt>Date</tt> in UTC
	 */
	public String toUTCString() {
		if (getUTC(false) != null) {
			return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US).format(utc.getTime());
		}
		return null;
	}
	
	/**
	 * Return this <tt>Date</tt> in UTC formatted using the ISO-8601 format
	 * (described in the class documentation). All fields are present in the String.
	 * The time zone is always UTC, denoted by the suffix Z. If the time value
	 * of this object is not a finite Number a Error exception is thrown.
	 *
	 * @return this <tt>Date</tt> in UTC formatted using the ISO-8601 format
	 */
	@BrowserCompatibility("IE:9+")
	public String toISOString() {
		// TODO: code it
		return null;
	}
	
	/**
	 * Return this <tt>Date</tt> formatted so that it can be used directly as a
	 * JSON value. As the format for JSON dates is ISO-8601, this method is
	 * equivalent to <tt>toISOString().</tt>
	 *
	 * @param key ignored
	 * @return this <tt>Date</tt> formatted in the JSON date format
	 */
	public String toJSON(String key) {
		return toISOString();
	}

	/**
	 * Computes the date from the given arguments interpreting them as UTC with
	 * the <tt>day</tt> field set to 1, and the <tt>hours</tt>, <tt>minutes</tt>,
	 * <tt>seconds</tt> and <tt>milliseconds</tt> fields set to 0 and returns
	 * the corresponding time value.
	 *
	 * <p>The <tt>UTC</tt> function differs from the <tt>Date</tt> constructor
	 * in two ways: it returns a time value as a <tt>Number</tt>, rather than
	 * creating a <tt>Date</tt> object, and it interprets the arguments in UTC
	 * rather than as local time.
	 *
	 * @param year the year (in UTC time zone)
	 * @param month the month of the year (in UTC time zone)
	 * @return the UTC time value corresponding to the given arguments
	 */
	public static double UTC(int year, int month) {
		return UTC(year, month, 1);
	}
	
	/**
	 * Computes the date from the given arguments interpreting them as UTC with
	 * the <tt>hours</tt>, <tt>minutes</tt>, <tt>seconds</tt> and
	 * <tt>milliseconds</tt> fields set to 0 and returns the corresponding time
	 * value.
	 *
	 * <p>The <tt>UTC</tt> function differs from the <tt>Date</tt> constructor
	 * in two ways: it returns a time value as a <tt>Number</tt>, rather than
	 * creating a <tt>Date</tt> object, and it interprets the arguments in UTC
	 * rather than as local time.
	 *
	 * @param year the year (in UTC time zone)
	 * @param month the month of the year (in UTC time zone)
	 * @param day the day of the month (in UTC time zone)
	 * @return the UTC time value corresponding to the given arguments
	 */
	public static double UTC(int year, int month, int day) {
		return UTC(year, month, day, 0);
	}

	/**
	 * Computes the date from the given arguments interpreting them as UTC with
	 * the <tt>minutes</tt>, <tt>seconds</tt> and <tt>milliseconds</tt> fields
	 * set to 0 and returns the corresponding time value.
	 *
	 * <p>The <tt>UTC</tt> function differs from the <tt>Date</tt> constructor
	 * in two ways: it returns a time value as a <tt>Number</tt>, rather than
	 * creating a <tt>Date</tt> object, and it interprets the arguments in UTC
	 * rather than as local time.
	 *
	 * @param year the year (in UTC time zone)
	 * @param month the month of the year (in UTC time zone)
	 * @param day the day of the month (in UTC time zone)
	 * @param hours the hour of the day (in UTC time zone)
	 * @return the UTC time value corresponding to the given arguments
	 */
	public static double UTC(int year, int month, int day, int hours) {
		return UTC(year, month, day, hours, 0);
	}

	/**
	 * Computes the date from the given arguments interpreting them as UTC with
	 * the <tt>seconds</tt> and <tt>milliseconds</tt> fields set to 0 and
	 * returns the corresponding time value.
	 *
	 * <p>The <tt>UTC</tt> function differs from the <tt>Date</tt> constructor
	 * in two ways: it returns a time value as a <tt>Number</tt>, rather than
	 * creating a <tt>Date</tt> object, and it interprets the arguments in UTC
	 * rather than as local time.
	 *
	 * @param year the year (in UTC time zone)
	 * @param month the month of the year (in UTC time zone)
	 * @param day the day of the month (in UTC time zone)
	 * @param hours the hour of the day (in UTC time zone)
	 * @param minutes the minute of the hour (in UTC time zone)
	 * @return the UTC time value corresponding to the given arguments
	 */
	public static double UTC(int year, int month, int day, int hours, int minutes) {
		return UTC(year, month, day, hours, minutes, 0);
		
	}
	
	/**
	 * Computes the date from the given arguments interpreting them as UTC with
	 * the <tt>milliseconds</tt> field set to 0 and returns the corresponding time
	 * value.
	 *
	 * <p>The <tt>UTC</tt> function differs from the <tt>Date</tt> constructor
	 * in two ways: it returns a time value as a <tt>Number</tt>, rather than
	 * creating a <tt>Date</tt> object, and it interprets the arguments in UTC
	 * rather than as local time.
	 *
	 * @param year the year (in UTC time zone)
	 * @param month the month of the year (in UTC time zone)
	 * @param day the day of the month (in UTC time zone)
	 * @param hours the hour of the day (in UTC time zone)
	 * @param minutes the minute of the hour (in UTC time zone)
	 * @param seconds the second of the minute (in UTC time zone)
	 * @return the UTC time value corresponding to the given arguments
	 */
	public static double UTC(int year, int month, int day, int hours, int minutes, int seconds) {
		return UTC(year, month, day, hours, minutes, seconds, 0);
	}
	
	/**
	 * Computes the date from the given arguments interpreting them as UTC and
	 * returns the corresponding time value.
	 *
	 * <p>The <tt>UTC</tt> function differs from the <tt>Date</tt> constructor
	 * in two ways: it returns a time value as a <tt>Number</tt>, rather than
	 * creating a <tt>Date</tt> object, and it interprets the arguments in UTC
	 * rather than as local time.
	 *
	 * @param year the year (in UTC time zone)
	 * @param month the month of the year (in UTC time zone)
	 * @param day the day of the month (in UTC time zone)
	 * @param hours the hour of the day (in UTC time zone)
	 * @param minutes the minute of the hour (in UTC time zone)
	 * @param seconds the second of the minute (in UTC time zone)
	 * @param ms the milliseconds of the second (in UTC time zone)
	 * @return the UTC time value corresponding to the given arguments
	 */
	public static double UTC(int year, int month, int day, int hours, int minutes, int seconds, int ms) {
		return new Date(year, month, day, hours, minutes, seconds, ms).getTime();
	}
	
	/**
	 * Returns the time value designating the UTC date and time of at the
	 * moment <tt>now</tt> is called.
	 *
	 * @return the time value designating the UTC date and time of at the moment <tt>now</tt> is called.
	 */
	@BrowserCompatibility("IE:9+")
	public static double now(){
		return new Date().getTime();
	}

	/**
	 * Returns the time value associated to this <tt>Date</tt>
	 *
	 * @return the time value associated to this <tt>Date</tt>
	 */
	public double valueOf() {
		return getTime();
	}
}
