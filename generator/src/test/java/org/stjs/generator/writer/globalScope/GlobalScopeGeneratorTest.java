package org.stjs.generator.writer.globalScope;

import static org.junit.Assert.assertEquals;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeDoesNotContain;
import static org.stjs.generator.utils.GeneratorTestHelper.execute;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

public class GlobalScopeGeneratorTest {
	@Test
	public void testGlobalScopeGeneration() {
		assertCodeDoesNotContain(Globals.class, "stjs.extends");
		assertCodeDoesNotContain(Globals.class, "Globals");
		assertCodeContains(Globals.class, "field=null");
		assertCodeContains(Globals.class, "method=function(");
		assertCodeContains(Globals.class, "one=0;two=0;");
		assertCodeContains(Globals.class, "main()");
		assertCodeContains(Globals.class, "(function(){var n = method();})()");
	}

	@Test
	public void testQualifiedCall() {
		assertCodeContains(GlobalScope1.class, "prototype.test=function(){method();};");
	}

	@Test
	public void testCallWithStaticImport() {
		assertCodeContains(GlobalScope2.class, "prototype.test=function(){method();};");
	}

	@Test
	public void testQualifiedField() {
		assertCodeContains(GlobalScope3.class, "s = field");
	}

	@Test
	public void testFieldWithStaticImport() {
		assertCodeContains(GlobalScope4.class, "s = field");
	}

	@Test
	public void testForAdapters() {
		assertCodeContains(GlobalScope5.class, "replace");
	}

	@Test
	public void testMethodWithStarImport() {
		assertCodeContains(GlobalScope7.class, "n = method()");
	}

	@Test
	public void testGlobalMainMethod() {
		Object result = execute(GlobalScope8.class);
		// We must do the weird (Number).intValue() because for some reason the execution returns the
		// integer 2 when run from eclipse, but return the double 2.0 when run from maven...
		assertEquals(2, ((Number) result).intValue());
	}

	@Test
	public void testFullyQualifiedField() {
		assertCodeContains(GlobalScope9.class, "s = field");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testInstanceMembersNotAllowded1() {
		generate(GlobalScope10.class);
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testInstanceMembersNotAllowded2() {
		generate(GlobalScope11.class);
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testInner() {
		generate(GlobalScope12.class);
	}
}
