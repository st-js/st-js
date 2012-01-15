package org.stjs.generator.lib.string;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.GeneratorTestHelper;

public class StringMethodsTest {

	@Test
	public void testStartsWith() {
		assertEquals(true, GeneratorTestHelper.execute(String1.class));
	}

	@Test
	public void testStartsWithOffset() {
		assertEquals(true, GeneratorTestHelper.execute(String2.class));
	}

	@Test
	public void testEndsWith() {
		assertEquals(true, GeneratorTestHelper.execute(String3.class));
	}

	@Test
	public void testMatches() {
		assertEquals(true, GeneratorTestHelper.execute(String4.class));
	}

	@Test
	public void testNotMatches() {
		assertEquals(false, GeneratorTestHelper.execute(String5.class));
	}

	@Test
	public void testCompareTo() {
		assertEquals(-1.0, GeneratorTestHelper.execute(String6.class));
	}

	@Test
	public void testCompareToIgnoreCase() {
		assertEquals(-1.0, GeneratorTestHelper.execute(String7.class));
	}

	@Test
	public void testEqualsIgnoreCase() {
		assertEquals(true, GeneratorTestHelper.execute(String8.class));
	}

	@Test
	public void testCodePointAt() {
		assertEquals(98, GeneratorTestHelper.execute(String9.class));
	}

	@Test
	public void testReplaceAll() {
		assertEquals("xbcx", GeneratorTestHelper.execute(String10.class));
	}

	@Test
	public void testReplaceFirst() {
		assertEquals("xbca", GeneratorTestHelper.execute(String11.class));
	}

	@Test
	public void testRegionMatches() {
		assertEquals(true, GeneratorTestHelper.execute(String12.class));
	}

	@Test
	public void testRegionMatchesIgnoreCase() {
		assertEquals(true, GeneratorTestHelper.execute(String13.class));
	}
}
