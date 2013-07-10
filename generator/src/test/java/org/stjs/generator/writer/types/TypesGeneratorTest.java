package org.stjs.generator.writer.types;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

public class TypesGeneratorTest {
	@Test
	public void testClassDeclaration() {
		assertCodeContains(Types1.class, "var Types1 = function(){};");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testForbidArrays() {
		generate(Types2.class);
	}

}
