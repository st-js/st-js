package org.stjs.generator.writer.variables;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class VariablesGeneratorTest extends AbstractStjsTest {
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
		assertCodeContains(Variables5.class, "y = this._x;");
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

	@Test
	public void testStatic_variable_to_store_singleton_order_do_not_matter() throws Exception {
		Assert.assertEquals("This is a value returned by the static method", execute(Variables10_static_variable_to_store_singleton_order_do_not_matter.class));
	}

}
