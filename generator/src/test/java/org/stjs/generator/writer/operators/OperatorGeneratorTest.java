package org.stjs.generator.writer.operators;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

public class OperatorGeneratorTest {
	@Test
	public void testXor() {
		assertCodeContains(Operator1.class, "test(x^y)");
	}

	@Test
	public void testBugNot() {
		assertCodeContains(Operator2.class, "if (!this.func())");
	}
}
