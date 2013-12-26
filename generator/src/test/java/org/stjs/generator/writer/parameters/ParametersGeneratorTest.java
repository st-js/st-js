package org.stjs.generator.writer.parameters;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

public class ParametersGeneratorTest {
	@Test
	public void testSimpleParam() {
		assertCodeContains(Parameters1.class, "function(arg)");
	}

	@Test
	public void testMoreParams() {
		assertCodeContains(Parameters2.class, "function(arg1, arg2, arg3)");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testParamWrongName() {
		// "var" is a wrong name
		generate(Parameters4.class);
	}

	@Test
	public void testGenericParams() {
		assertCodeContains(Parameters5.class, "function(arg)");
	}

	@Test
	public void testVarArgsLength() {
		assertCodeContains(Parameters6.class, "n = arguments.length");
	}
}
