package org.stjs.generator.exec.ints;

import static org.junit.Assert.*;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class IntsTest extends AbstractStjsTest {

	@Test
	public void testCastIntToShort() {
		short expected = IntToShort.method(IntToShort.MAX_INT);
		double expectedDouble = (double) expected;
		assertEquals(expectedDouble, executeAndReturnNumber(IntToShort.class), 0);
	}

	@Test
	public void testCastIntToChar() {
		char expected = IntToChar.method(IntToShort.MAX_INT);
		double expectedDouble = (double) expected;
		assertEquals(expectedDouble, executeAndReturnNumber(IntToChar.class), 0);
	}

	@Test
	public void testCastIntToByte() {
		byte expected = IntToByte.method(IntToByte.MAX_INT);
		double expectedDouble = (double) expected;
		assertEquals(expectedDouble, executeAndReturnNumber(IntToByte.class), 0);
	}

	@Test
	public void testCastIntToShortMin() {
		short expected = IntToShort.method(IntToShort.MIN_INT);
		double expectedDouble = (double) expected;
		assertEquals(expectedDouble, executeAndReturnNumber(IntToShortMin.class), 0);
	}

	@Test
	public void testCastLongToInt() {
		int expected = LongToInt.method(LongToInt.BIG_LONG);
		double expectedDouble = (double) expected;
		assertEquals(expectedDouble, executeAndReturnNumber(LongToInt.class), 0);
	}

	@Test
	public void testCastLongToShort() {
		int expected = LongToShort.method(LongToShort.BIG_LONG);
		double expectedDouble = (double) expected;
		assertEquals(expectedDouble, executeAndReturnNumber(LongToShort.class), 0);
	}

	@Test
	public void testCastLongToChar() {
		int expected = LongToChar.method(LongToChar.BIG_LONG);
		double expectedDouble = (double) expected;
		assertEquals(expectedDouble, executeAndReturnNumber(LongToChar.class), 0);
	}

	@Test
	public void testCastLongToByte() {
		int expected = LongToByte.method(LongToByte.BIG_LONG);
		double expectedDouble = (double) expected;
		assertEquals(expectedDouble, executeAndReturnNumber(LongToByte.class), 0);
	}

	@Test
	public void testCastLongToByteSmall() {
		int expected = LongToByteSmall.method(LongToByteSmall.SMALL_LONG);
		double expectedDouble = (double) expected;
		assertEquals(expectedDouble, executeAndReturnNumber(LongToByteSmall.class), 0);
	}

	@Test
	public void testCastCharToShort() {
		int expected = CharToShort.method(CharToShort.BIG_CHAR);
		double expectedDouble = (double) expected;
		assertEquals(expectedDouble, executeAndReturnNumber(CharToShort.class), 0);
	}

	@Test
	public void testCastShortToChar() {
		int expected = ShortToChar.method(ShortToChar.NEG_SHORT);
		double expectedDouble = (double) expected;
		assertEquals(expectedDouble, executeAndReturnNumber(ShortToChar.class), 0);
	}

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
