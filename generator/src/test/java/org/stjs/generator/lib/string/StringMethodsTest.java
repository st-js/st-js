package org.stjs.generator.lib.string;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class StringMethodsTest extends AbstractStjsTest {

	@Test
	public void testStartsWith() {
		assertEquals(true, executeAndReturnBoolean(String1.class));
	}

	@Test
	public void testStartsWithOffset() {
		assertEquals(true, executeAndReturnBoolean(String2.class));
	}

	@Test
	public void testEndsWith() {
		assertEquals(true, executeAndReturnBoolean(String3.class));
	}

	@Test
	public void testMatches() {
		assertEquals(true, executeAndReturnBoolean(String4.class));
	}

	@Test
	public void testNotMatches() {
		assertEquals(false, executeAndReturnBoolean(String5.class));
	}

	@Test
	public void testCompareTo() {
		assertEquals(-1.0, executeAndReturnNumber(String6.class), 0);
	}

	@Test
	public void testCompareToIgnoreCase() {
		assertEquals(-1.0, executeAndReturnNumber(String7.class), 0);
	}

	@Test
	public void testEqualsIgnoreCase() {
		assertEquals(true, executeAndReturnBoolean(String8.class));
	}

	@Test
	public void testCodePointAt() {
		assertEquals(98.0, executeAndReturnNumber(String9.class), 0);
	}

	@Test
	public void testReplaceAll() {
		assertEquals("xbcx", execute(String10.class));
	}

	@Test
	public void testReplaceFirst() {
		assertEquals("xbca", execute(String11.class));
	}

	@Test
	public void testRegionMatches() {
		assertEquals(true, executeAndReturnBoolean(String12.class));
	}

	@Test
	public void testRegionMatchesIgnoreCase() {
		assertEquals(true, executeAndReturnBoolean(String13.class));
	}
}
