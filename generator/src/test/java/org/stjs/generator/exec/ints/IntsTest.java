package org.stjs.generator.exec.ints;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.GeneratorTestHelper;

public class IntsTest {

	@Test
	public void testCastInt() {
		assertEquals(2.0, GeneratorTestHelper.executeAndReturnNumber(Ints1.class), 0);
	}

	@Test
	public void testCastIntLonger() {
		assertEquals(1413492112445.0, GeneratorTestHelper.executeAndReturnNumber(Ints1b.class), 0);
	}

	@Test
	public void testCastLong() {
		assertEquals(2.0, GeneratorTestHelper.executeAndReturnNumber(Ints2.class), 0);
	}

	@Test
	public void testIntegerDivision() {
		assertEquals(2.0, GeneratorTestHelper.executeAndReturnNumber(Ints3.class), 0);
	}

	@Test
	public void testDoubleDivision1() {
		assertEquals(2.5, GeneratorTestHelper.executeAndReturnNumber(Ints4.class), 0);
	}

	@Test
	public void testDoubleDivision2() {
		assertEquals(2.5, GeneratorTestHelper.executeAndReturnNumber(Ints5.class), 0);
	}

	@Test
	public void testIntegerDivisionAndAssign() {
		assertEquals(2.0, GeneratorTestHelper.executeAndReturnNumber(Ints6.class), 0);
	}

	@Test
	public void testIntegerDivisionAndAssign2() {
		assertEquals(2.0, GeneratorTestHelper.executeAndReturnNumber(Ints7.class), 0);
	}
}
