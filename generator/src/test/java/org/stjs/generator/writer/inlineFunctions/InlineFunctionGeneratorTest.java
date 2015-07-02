package org.stjs.generator.writer.inlineFunctions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class InlineFunctionGeneratorTest extends AbstractStjsTest {
	@Test
	public void testInlineFunction() {
		assertCodeContains(InlineFunctions1.class, "method(function(arg){arg=arg+1;})");
	}

	@Test
	public void testInlineFunctionWithJavaFuncAnnotation() {
		assertCodeContains(InlineFunctions1.class, "method(function(arg){arg=arg+1;})");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testInterfaceAndParamForbidden() {
		assertCodeContains(InlineFunctions2.class, "stjs.extend(function(){}, null, [FunctionInterface],");
	}

	@Test
	public void testInterfaceAndParam() {
		assertCodeContains(InlineFunctions2b.class,
				"stjs.extend(function InlineFunctions2b$1(){},  null, [FunctionInterface2], function(constructor, prototype){"
						+ "prototype.test=2; prototype.$invoke=function(arg){arg=arg+1;}");
	}

	@Test
	public void testInterfaceTwoMethods() {
		assertCodeContains(InlineFunctions3.class, "stjs.extend(function InlineFunctions3$1(){}, null, [FunctionInterface2], ");
		assertCodeContains(InlineFunctions3.class, "prototype.$invoke=function(arg){arg=arg+1;};"
				+ "prototype.$invoke2=function(arg2){arg2=arg2+1;};");
	}

	@Test
	public void testInlineFunctionAssign() {
		assertCodeContains(InlineFunctions4.class, "func =  function(arg){arg=arg+1;}");
	}

	@Test
	public void testInlineFunctionWithAbstractClass() {
		assertCodeContains(InlineFunctions5.class, "method(function(){})");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testImplementInlinefunction() {
		// implement is forbidden
		generate(InlineFunctions6.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testAccessOuterScope() {
		generate(InlineFunctions7.class);
	}

	@Test
	public void testUsingTHISParam() {
		// assertCodeContains(InlineFunctions8.class, "method(function(){})");
		double n = executeAndReturnNumber(InlineFunctions8.class);
		assertEquals(10.0, n, 0);
	}

}
