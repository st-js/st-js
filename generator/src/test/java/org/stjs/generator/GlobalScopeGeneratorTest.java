package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.globalScope.GlobalScope1;
import test.generator.globalScope.GlobalScope2;
import test.generator.globalScope.GlobalScope3;
import test.generator.globalScope.GlobalScope4;
import test.generator.globalScope.GlobalScope5;

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
