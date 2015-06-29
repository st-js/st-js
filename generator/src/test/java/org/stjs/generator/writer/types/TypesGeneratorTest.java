package org.stjs.generator.writer.types;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class TypesGeneratorTest extends AbstractStjsTest {
	@Test
	public void testClassDeclaration() {
		assertCodeContains(Types1.class, "var Types1 = function(){};");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testForbidArrays() {
		generate(Types2.class);
	}

	@Test
	public void testAllowedrrays() {
		// should not break in the annotation's array
		generate(Types3.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testUseForbiddenTypes() {
		generate(Types4.class);
	}

	@Test
	public void testExtendsException() {
		// should not break in the annotation's array
		generate(Types5.class);
	}
}
