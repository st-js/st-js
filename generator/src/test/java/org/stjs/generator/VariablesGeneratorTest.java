package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;

import test.generator.variables.Variables1;
import test.generator.variables.Variables2;
import test.generator.variables.Variables3;
import test.generator.variables.Variables4;

public class VariablesGeneratorTest {
	@Test
	public void testSimpleVariable() {
		assertCodeContains(Variables1.class, "var x;");
	}

	@Test
	public void testSimpleVariableAssigned() {
		assertCodeContains(Variables2.class, "var x = 2;");
	}

	@Test
	public void testMultipleVariableAssigned() {
		assertCodeContains(Variables3.class, "var x = 2, y = 3;");
	}

	@Test(expected = JavascriptGenerationException.class)
	public void testVariableWrongName() {
		// "var" is a wrong name
		generate(Variables4.class);
	}
}
