package org.stjs.generator.writer.operators;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class OperatorGeneratorTest extends AbstractStjsTest {
	@Test
	public void testXor() {
		assertCodeContains(Operator1.class, "test(x^y)");
	}

	@Test
	public void testBugNot() {
		assertCodeContains(Operator2.class, "if (!this._func())");
	}
}
