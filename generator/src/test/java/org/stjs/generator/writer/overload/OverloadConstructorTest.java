package org.stjs.generator.writer.overload;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeDoesNotContain;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

public class OverloadConstructorTest {
	@Test
	public void testSkipNativeGeneration() {
		assertCodeDoesNotContain(Overload1c.class, "n = 10");
	}

	@Test
	public void testOverloadDifferentParamNumber() {
		// check that no other method is generated
		assertCodeContains(Overload2c.class, "Overload2c=function(param1, param2){}");
	}

	@Test
	public void testMoreGenericType() {
		// check that no other method is generated
		assertCodeContains(Overload3c.class, "Overload3c=function(param1){}");
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
		assertCodeContains(Overload6c.class, "Overload6c=function(_arguments){}");
	}
}
