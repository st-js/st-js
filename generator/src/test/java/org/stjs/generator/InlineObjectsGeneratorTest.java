package org.stjs.generator;

import static org.stjs.generator.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.inlineObjects.InlineObjects1;
import test.generator.inlineObjects.InlineObjects2;

public class InlineObjectsGeneratorTest {
	@Test
	public void testInlineObject() {
		assertCodeContains(InlineObjects1.class, "o = {a:1, b:\"x\"}");
	}

	@Test
	public void testInlineObjectAndOtherStatements() {
		// the other statement should be put outside (TODO !?)
		assertCodeContains(InlineObjects2.class, "o = {a:1, b:\"x\"}");
	}

}
