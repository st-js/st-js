package org.stjs.generator.lib.number;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.GeneratorTestHelper;

public class NumberMethodsTest {

	@Test
	public void testParseInt() {
		assertEquals(123.0, GeneratorTestHelper.execute(Number1.class));
	}

	@Test
	public void testIntValue() {
		assertEquals(123.0, GeneratorTestHelper.execute(Number2.class));
	}

	@Test
	public void testValueOf() {
		assertEquals(123.0, GeneratorTestHelper.execute(Number3.class));
	}
}
