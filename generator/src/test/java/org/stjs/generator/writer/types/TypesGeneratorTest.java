package org.stjs.generator.writer.types;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

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

	@Test(expected = JavascriptFileGenerationException.class)
	public void testUseForbiddenTypes2() {
		// ArrayList is not allowed
		generate(Types6.class);
	}

	@Test
	public void testUseForbiddenTypes3() {
		// LinkedList is allowed - presence of LinkedList.stjs
		generate(Types7.class);
	}
}
