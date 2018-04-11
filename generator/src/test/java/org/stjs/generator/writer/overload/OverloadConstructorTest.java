package org.stjs.generator.writer.overload;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class OverloadConstructorTest extends AbstractStjsTest {
	@Test
	public void testSkipNativeGeneration() {
		assertCodeDoesNotContain(Overload1c.class, "n = 10");
	}

	@Test
	public void testOverloadDifferentParamNumber() {
		// check that no other method is generated
		assertCodeContains(Overload2c.class, "class Overload2c { constructor(param1, param2){} }");
	}

	@Test
	public void testMoreGenericType() {
		// check that no other method is generated
		assertCodeContains(Overload3c.class, "class Overload3c { constructor(param1){} }");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testLessGenericType() {
		generate(Overload4c.class);
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testTwoWithBody() {
		generate(Overload5c.class);
	}

	@Test
	public void testVarArgs() {
		// check that no other method is generated
		assertCodeContains(Overload6c.class, "class Overload6c { constructor(..._arguments){} }");
	}
}
