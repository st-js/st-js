package org.stjs.generator.writer.literals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class LiteralGeneratorTest extends AbstractStjsTest {
	@Test
	public void testHexaNumbers() {
		assertCodeContains(Literal1.class, "65535");
	}

	@Test
	public void testNegativeHexaNumbers() {
		assertCodeContains(Literal1a.class, "-65535");
	}

	@Test
	public void testFloatNumbers() {
		assertCodeContains(Literal2.class, "field=2.0;");
	}

	@Test
	public void testDoubleNumbers() {
		assertCodeContains(Literal3.class, "method(0.01);");
	}

	@Test
	public void testBoolean() {
		assertCodeContains(Literal4.class, "field=false;");
	}
}
