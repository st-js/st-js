package org.stjs.generator.writer.inlineFunctions;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;

public class InlineFunctionGeneratorTest {
	@Test
	public void testInlineFunction() {
		assertCodeContains(InlineFunctions1.class, "method(function(arg){arg=arg+1;})");
	}

	@Test(expected = JavascriptGenerationException.class)
	public void testInterfaceAndParamForbidden() {
		assertCodeContains(InlineFunctions2.class, "stjs.extend(_InlineType, FunctionInterface);");
	}

	@Test
	public void testInterfaceAndParam() {
		assertCodeContains(InlineFunctions2b.class, "stjs.extend(_InlineType, FunctionInterface2);");
		assertCodeContains(InlineFunctions2b.class, "_InlineType.prototype.test = 2; "
				+ "_InlineType.prototype.$invoke=function(arg){arg=arg+1;}");
	}

	@Test
	public void testInterfaceTwoMethods() {
		assertCodeContains(InlineFunctions3.class, "stjs.extend(_InlineType, FunctionInterface2);");
		assertCodeContains(InlineFunctions3.class, "_InlineType.prototype.$invoke=function(arg){arg=arg+1;};"
				+ " _InlineType.prototype.$invoke2=function(arg2){arg2=arg2+1;};");
	}

	@Test
	public void testInlineFunctionAssign() {
		assertCodeContains(InlineFunctions4.class, "func =  function(arg){arg=arg+1;}");
	}

	@Test
	public void testInlineFunctionWithAbstractClass() {
		assertCodeContains(InlineFunctions5.class, "method(function(){})");
	}

}
