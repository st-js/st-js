package org.stjs.generator.exec.ints;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class IntsTest extends AbstractStjsTest {

	@Test
	public void testCastInt() {
		assertEquals(2.0, executeAndReturnNumber(Ints1.class), 0);
	}

	@Test
	public void testCastIntLonger() {
		assertEquals(1413492112445.0, executeAndReturnNumber(Ints1b.class), 0);
	}

	@Test
	public void testCastLong() {
		assertEquals(2.0, executeAndReturnNumber(Ints2.class), 0);
	}

	@Test
	public void testIntegerDivision() {
		assertEquals(2.0, executeAndReturnNumber(Ints3.class), 0);
	}

	@Test
	public void testDoubleDivision1() {
		assertEquals(2.5, executeAndReturnNumber(Ints4.class), 0);
	}

	@Test
	public void testDoubleDivision2() {
		assertEquals(2.5, executeAndReturnNumber(Ints5.class), 0);
	}

	@Test
	public void testIntegerDivisionAndAssign() {
		assertEquals(2.0, executeAndReturnNumber(Ints6.class), 0);
	}

	@Test
	public void testIntegerDivisionAndAssign2() {
		assertEquals(2.0, executeAndReturnNumber(Ints7.class), 0);
	}
}
