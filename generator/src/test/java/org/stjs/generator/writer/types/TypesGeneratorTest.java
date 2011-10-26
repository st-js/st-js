package org.stjs.generator.writer.types;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;


public class TypesGeneratorTest {
	@Test
	public void testClassDeclaration() {
		assertCodeContains(Types1.class, "var Types1 = function(){};");
	}

}
