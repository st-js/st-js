package org.stjs.generator.exec.aserts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.GeneratorTestHelper;

public class AssertsTest {
	@Test
	public void testAssertArgEquals() {
		assertEquals(2.0, GeneratorTestHelper.executeAndReturnNumber(Asserts1.class), 0);
	}

	@Test
	public void testAssertArgNotNull() {
		assertEquals(2.0, GeneratorTestHelper.executeAndReturnNumber(Asserts2.class), 0);
	}

	@Test
	public void testAssertArgTrue() {
		assertEquals(2.0, GeneratorTestHelper.executeAndReturnNumber(Asserts3.class), 0);
	}

	@Test
	public void testAssertStateEquals() {
		assertEquals(2.0, GeneratorTestHelper.executeAndReturnNumber(Asserts4.class), 0);
	}

	@Test
	public void testAssertStateNotNull() {
		assertEquals(2.0, GeneratorTestHelper.executeAndReturnNumber(Asserts5.class), 0);
	}

	@Test
	public void testAssertStateTrue() {
		assertEquals(2.0, GeneratorTestHelper.executeAndReturnNumber(Asserts6.class), 0);
	}
}
