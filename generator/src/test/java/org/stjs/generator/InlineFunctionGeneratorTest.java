package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

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
		assertCodeContains(InlineFunctions2.class, "stjs.extend(_InlineType, FunctionInterface);");
		assertCodeContains(InlineFunctions2.class, "_InlineType.prototype.test = 2; "
				+ "_InlineType.prototype.run=function(arg){arg=arg+1;}");
	}

	@Test
	public void testInterfaceTwoMethods() {
		assertCodeContains(InlineFunctions3.class, "stjs.extend(_InlineType, FunctionInterface2);");
		assertCodeContains(InlineFunctions3.class, "_InlineType.prototype.run=function(arg){arg=arg+1;}"
				+ " _InlineType.prototype.run2=function(arg2){arg2=arg2+1;}");
	}

	@Test
	public void testInlineFunctionAssign() {
		assertCodeContains(InlineFunctions4.class, "func =  function(arg){arg=arg+1;}");
	}
}
