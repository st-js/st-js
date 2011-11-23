package org.stjs.generator.writer.globalScope;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeDoesNotContain;

import org.junit.Test;


public class GlobalScopeGeneratorTest {
	@Test
	public void testGlobalScopeGeneration() {
		assertCodeDoesNotContain(Globals.class, "Globals.field");
		assertCodeDoesNotContain(Globals.class, "Globals.method");

		assertCodeContains(Globals.class, "Globals.prototype.instanceMethod");
		assertCodeContains(Globals.class, "Globals.prototype.instanceField");
	}

	@Test
	public void testQualifiedCall() {
		assertCodeContains(GlobalScope1.class, "test=function(){method();}");
	}

	@Test
	public void testCallWithStaticImport() {
		assertCodeContains(GlobalScope2.class, "test=function(){method();}");
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
