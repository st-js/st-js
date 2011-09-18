package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;

import test.generator.inlineObjects.InlineObjects1;
import test.generator.inlineObjects.InlineObjects2;
import test.generator.inlineObjects.InlineObjects3;

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

	@Test
	public void testStatementsInsideInlineFunctions() {
		// make sure the prop:value syntax doesn't go further
		assertCodeContains(InlineObjects3.class, "x=2");
	}

}
