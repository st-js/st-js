package org.stjs.generator;

import static org.stjs.generator.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.GeneratorTestHelper.generate;

import org.junit.Test;

import test.generator.inlineObjects.InlineObjects1;
import test.generator.inlineObjects.InlineObjects2;

public class InlineObjectsGeneratorTest {
	@Test
	public void testInlineObject() {
		assertCodeContains(InlineObjects1.class, "o = {a:1, b:\"x\"}");
	}

	@Test(expected = JavascriptGenerationException.class)
	public void testInlineObjectAndOtherStatements() {
		// other statements cannot be put inside the initializing blocks
		generate(InlineObjects2.class);
	}

}
