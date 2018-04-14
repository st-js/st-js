package org.stjs.generator.writer.parameters;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

public class ParametersGeneratorTest extends AbstractStjsTest {
	@Test
	public void testSimpleParam() {
		assertCodeContains(Parameters1.class, "method(arg)");
	}

	@Test
	public void testMoreParams() {
		assertCodeContains(Parameters2.class, "method(arg1, arg2, arg3)");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testParamWrongName() {
		// "var" is a wrong name
		generate(Parameters4.class);
	}

	@Test
	public void testGenericParams() {
		assertCodeContains(Parameters5.class, "method(arg)");
	}

	@Test
	public void testVarArgsLength() {
		assertCodeContains(Parameters6.class, "n = arguments.length");
	}

	@Test
	public void testVarArgsAfter() {
		assertCodeContains(Parameters7.class, "method(a, b, ...other): void {");
	}
}
