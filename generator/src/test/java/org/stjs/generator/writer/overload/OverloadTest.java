package org.stjs.generator.writer.overload;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeDoesNotContain;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

public class OverloadTest {
	@Test
	public void testSkipNativeGeneration() {
		assertCodeDoesNotContain(Overload1.class, "method");
	}

	@Test
	public void testOverloadDifferentParamNumber() {
		// check that no other method is generated
		assertCodeContains(Overload2.class, "{prototype.method=function(param1, param2){};}");
	}

	@Test
	public void testMoreGenericType() {
		// check that no other method is generated
		assertCodeContains(Overload3.class, "{prototype.method=function(param1){};}");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testLessGenericType() {
		generate(Overload4.class);
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testTwoWithBody() {
		generate(Overload5.class);
	}

	@Test
	public void testVarArgs() {
		// check that no other method is generated
		assertCodeContains(Overload6.class, "{prototype.method=function(_arguments){};}");
	}
}
