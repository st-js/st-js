package org.stjs.generator.writer.variables;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

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

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testVariableWrongName() {
		// "var" is a wrong name
		generate(Variables4.class);
	}

	@Test
	public void testKeepDeclarationLocation() {
		assertCodeContains(Variables5.class, "y = this.x;");
		assertCodeContains(Variables5.class, "k = x;");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testVariableUseFromGlobalScopeBeforeDeclaredInMethod() {
		assertCodeContains(Variables7.class, "y = x;");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testVariableDeclaredInMethodAndGlobalScopeQualified() {
		// the global "x" variable, after generation would in fact be replaced by the local variable
		generate(Variables8.class);
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testVariableDeclaredInMethodAndGlobalScope() {
		// the global "x" variable, after generation would in fact be replaced by the local variable
		generate(Variables8c.class);
	}

	@Test
	public void testVariableDeclaredInInnerTypeAndGlobalScope() {
		// this should not raise any error
		generate(Variables8b.class);
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testParamInMethodAndGlobalScope() {
		// the global "x" variable, after generation would in fact be replaced by the parameter
		generate(Variables9.class);
	}
}
