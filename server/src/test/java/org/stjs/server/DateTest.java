package org.stjs.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;
import org.stjs.javascript.Date;

public class DateTest {

	@Test
	public void testDate() {
		Locale.setDefault(Locale.GERMANY);
		Date d = new Date(2011, 10, 10, 17, 10, 0, 0);
		assertEquals(10, (int) d.getDate());
		assertEquals(17, (int) d.getHours());
		assertEquals(16, (int) d.getUTCHours());

		d.setHours(18);
		assertEquals(18, (int) d.getHours());
		assertEquals(17, (int) d.getUTCHours());

		d.setUTCHours(18);
		assertEquals(19, (int) d.getHours());
		assertEquals(18, (int) d.getUTCHours());
	}

	@Test
	public void testInvalidDate() {
		Locale.setDefault(Locale.GERMANY);
		Date d = new Date("abc");
		assertTrue(Double.isNaN(d.getDate()));
		assertTrue(Double.isNaN(d.getUTCDate()));
	}

	@Test
	public void testParse() {
		Locale.setDefault(Locale.GERMANY);
		Date d = new Date("2011-11-10 10:00:00");
		assertEquals(10, (int) d.getDate());
	}
}
