package org.stjs.generator.writer.globalScope;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeDoesNotContain;

import org.junit.Test;


public class GlobalScopeGeneratorTest {
	@Test
	public void testGlobalScopeGeneration() {
		assertCodeDoesNotContain(Globals.class, "field:");
		assertCodeDoesNotContain(Globals.class, "method:");
		assertCodeContains(Globals.class, "field=null");
		assertCodeContains(Globals.class, "method=function(");
		assertCodeContains(Globals.class, "one=null;two=null;");

		assertCodeContains(Globals.class, "instanceMethod:");
		assertCodeContains(Globals.class, "instanceField:");
	}

	@Test
	public void testQualifiedCall() {
		assertCodeContains(GlobalScope1.class, "test:function(){method();}");
	}

	@Test
	public void testCallWithStaticImport() {
		assertCodeContains(GlobalScope2.class, "test:function(){method();}");
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
}
