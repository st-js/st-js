package org.stjs.generator.lib.number;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.GeneratorTestHelper;

public class NumberMethodsTest {

	@Test
	public void testParseInt() {
		assertEquals(123.0, GeneratorTestHelper.executeAndReturnNumber(Number1.class), 0);
	}

	@Test
	public void testIntValue() {
		assertEquals(123.0, GeneratorTestHelper.executeAndReturnNumber(Number2.class), 0);
	}

	@Test
	public void testValueOf() {
		assertEquals(123.0, GeneratorTestHelper.executeAndReturnNumber(Number3.class), 0);
	}
}
