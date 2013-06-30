package org.stjs.generator.writer.globalScope;

import static org.junit.Assert.assertEquals;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeDoesNotContain;
import static org.stjs.generator.utils.GeneratorTestHelper.execute;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.GeneratorTestHelper;

public class GlobalScopeGeneratorTest {
	@Test
	public void testGlobalScopeGeneration() {
		assertCodeDoesNotContain(Globals.class, "field:");
		assertCodeDoesNotContain(Globals.class, "method:");
		assertCodeContains(Globals.class, "field=null");
		assertCodeContains(Globals.class, "method=function(");
		assertCodeContains(Globals.class, "one=null;two=null;");

		assertCodeContains(Globals.class, "prototype.instanceMethod=");
		assertCodeContains(Globals.class, "prototype.instanceField=");
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
	public void testFieldOfGlobalInstance() {
		assertCodeContains(GlobalScope6.class, "global.instanceField");
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
}
