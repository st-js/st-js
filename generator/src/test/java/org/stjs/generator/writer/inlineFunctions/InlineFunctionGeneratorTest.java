package org.stjs.generator.writer.inlineFunctions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class InlineFunctionGeneratorTest extends AbstractStjsTest {
	@Test
	public void testInlineFunction() {
		assertCodeContains(InlineFunctions1.class, "method((arg: number) => {arg=arg+1;})");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testInterfaceAndParamForbidden() {
		assertCodeContains(InlineFunctions2.class, "stjs.extend(function(){}, null, [FunctionInterface],");
	}

	@Test
	public void testInterfaceAndParam() {
		assertCodeContains(InlineFunctions2b.class,
				"InlineFunctions2b.method(new (class InlineFunctions2b_InlineFunctions2b$1 implements FunctionInterface2 {\n" +
						"  test: number = 2;\n" +
						"  $invoke(arg: number): void { arg = arg + 1; }");
	}

	@Test
	public void testInterfaceTwoMethods() {
		assertCodeContains(InlineFunctions3.class, "class InlineFunctions3_InlineFunctions3$1 implements FunctionInterface2 {\n" +
				"            $invoke(arg: number): void { arg = arg + 1; }\n" +
				"            $invoke2(arg2: number): void { arg2 = arg2 + 1; }\n" +
				"        }");
	}

	@Test
	public void testInlineFunctionAssign() {
		assertCodeContains(InlineFunctions4.class, "func = (arg: number) => {arg=arg+1;}");
	}

	@Test
	public void testInlineFunctionWithAbstractClass() {
		assertCodeContains(InlineFunctions5.class, "method(() => {})");
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
