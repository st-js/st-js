package org.stjs.generator.exec.ints;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.GeneratorTestHelper;

public class IntsTest {

	@Test
	public void testCastInt() {
		assertEquals(2.0, GeneratorTestHelper.execute(Ints1.class));
	}

	@Test
	public void testCastLong() {
		assertEquals(2.0, GeneratorTestHelper.execute(Ints2.class));
	}

	@Test
	public void testIntegerDivision() {
		assertEquals(2.0, GeneratorTestHelper.execute(Ints3.class));
	}

	@Test
	public void testDoubleDivision() {
		assertEquals(2.5, GeneratorTestHelper.execute(Ints4.class));
	}

}
