package org.stjs.generator;

import static org.stjs.generator.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.inlineFunctions.InlineFunctions1;
import test.generator.inlineFunctions.InlineFunctions2;
import test.generator.inlineFunctions.InlineFunctions3;
import test.generator.inlineFunctions.InlineFunctions4;

public class InlineFunctionGeneratorTest {
	@Test
	public void testInlineFunction() {
		assertCodeContains(InlineFunctions1.class, "method(function(arg){arg=arg+1;})");
	}

	@Test
	public void testInterfaceAndParam() {
		// this should not generate an inline function (TODO -> what should it do!?)
		assertCodeContains(InlineFunctions2.class, "xmethod(function(arg){arg=arg+1;})");
	}

	@Test
	public void testInterfaceTwoMethods() {
		// this should not generate an inline function (TODO -> what should it do!?)
		assertCodeContains(InlineFunctions3.class, "xmethod(function(arg){arg=arg+1;})");
	}

	@Test
	public void testInlineFunctionAssign() {
		assertCodeContains(InlineFunctions4.class, "func =  function(arg){arg=arg+1;}");
	}
}
