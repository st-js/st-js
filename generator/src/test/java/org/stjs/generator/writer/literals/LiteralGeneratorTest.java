package org.stjs.generator.writer.literals;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

public class LiteralGeneratorTest {
	@Test
	public void testHexaNumbers() {
		assertCodeContains(Literal1.class, "0xFFFF");
	}

	@Test
	public void testNegativeHexaNumbers() {
		assertCodeContains(Literal1a.class, "-0xFFFF");
	}

	@Test
	public void testFloatNumbers() {
		assertCodeContains(Literal2.class, "field=2;");
	}
}
