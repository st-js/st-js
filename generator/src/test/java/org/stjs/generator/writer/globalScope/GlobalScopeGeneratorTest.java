package org.stjs.generator.writer.globalScope;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;


public class GlobalScopeGeneratorTest {
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

}
