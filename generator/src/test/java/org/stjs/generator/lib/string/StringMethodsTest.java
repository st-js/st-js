package org.stjs.generator.lib.string;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stjs.generator.MultipleFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

public class StringMethodsTest extends AbstractStjsTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();


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
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("The method 'String.prototype.compareTo()' only exists in Java.");
		assertEquals(-1.0, executeAndReturnNumber(String6.class), 0);
	}

	@Test
	public void testCompareToIgnoreCase() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("The method 'String.prototype.compareToIgnoreCase()' only exists in Java.");
		assertEquals(-1.0, executeAndReturnNumber(String7.class), 0);
	}

	@Test
	public void testEqualsIgnoreCase() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("The method 'String.prototype.equalsIgnoreCase()' only exists in Java.");
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
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("The method 'String.prototype.regionMatches()' only exists in Java.");
		assertEquals(true, executeAndReturnBoolean(String12.class));
	}

	@Test
	public void testRegionMatchesIgnoreCase() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("The method 'String.prototype.regionMatches()' only exists in Java.");
		assertEquals(true, executeAndReturnBoolean(String13.class));
	}
}
