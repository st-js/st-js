package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeDoesNotContain;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;

import test.generator.inlineObjects.InlineObjects1;
import test.generator.inlineObjects.InlineObjects2;
import test.generator.inlineObjects.InlineObjects3;
import test.generator.inlineObjects.InlineObjects4;
import test.generator.inlineObjects.InlineObjects5;

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

	@Test
	public void testFieldsQualifiedWithThis() {
		// the "this." should be removed (otherwise is rhino who complains)
		assertCodeContains(InlineObjects4.class, "o={a:other.a}");
	}

	@Test
	public void testMockTypeConstructorCall() {
		// should call object constructor {} instead of new Pojo();
		assertCodeContains(InlineObjects5.class, "o={}");
		assertCodeDoesNotContain(InlineObjects5.class, "Pojo");
	}
}
