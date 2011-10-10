package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;

import test.generator.methods.Methods1;
import test.generator.methods.Methods10;
import test.generator.methods.Methods11;
import test.generator.methods.Methods2;
import test.generator.methods.Methods3;
import test.generator.methods.Methods4;
import test.generator.methods.Methods5;
import test.generator.methods.Methods6;
import test.generator.methods.Methods7;
import test.generator.methods.Methods8;
import test.generator.methods.Methods9;

public class MethodsGeneratorTest {
	@Test
	public void testPublicInstanceMethod() {
		assertCodeContains(Methods1.class, "Methods1.prototype.method = function(arg1,arg2){return 0;};");
	}

	@Test
	public void testPrivateInstanceMethod() {
		// same as public
		assertCodeContains(Methods2.class, "Methods2.prototype.method = function(arg1,arg2){");
	}

	@Test
	public void testPublicStaticMethod() {
		assertCodeContains(Methods3.class, "Methods3.method = function(arg1,arg2){");
	}

	@Test
	public void testPrivateStaticMethod() {
		assertCodeContains(Methods4.class, "Methods4.method = function(arg1,arg2){");
	}

	@Test
	public void testMainMethod() {
		// should generate the call to the main method
		assertCodeContains(Methods5.class, "if (!stjs.mainCallDisabled) Methods5.main();");
	}

	@Test
	public void testConstructor() {
		assertCodeContains(Methods6.class, "Methods6=function(arg){");
	}

	@Test
	public void testSpecialThis() {
		// the special parameter THIS should not be added
		assertCodeContains(Methods7.class, "Methods7.prototype.method=function(arg2){");
	}

	@Test
	public void testAdapter() {
		assertCodeContains(Methods8.class, "(10).toFixed(2)");
	}
	@Test(expected = JavascriptGenerationException.class)
	public void testVarArgsMethod1() {
		// only one var arg argument is allowed and the name should be "arguments" -> like the js variable
		generate(Methods9.class);
	}

	@Test(expected = JavascriptGenerationException.class)
	public void testVarArgsMethod2() {
		// only one var arg argument is allowed and the name should be "arguments" -> like the js variable
		generate(Methods10.class);
	}

	@Test
	public void testVarArgsMethod3() {
		// only one var arg argument is allowed and the name should be "arguments" -> like the js variable
		assertCodeContains(Methods11.class, "Methods11.prototype.method=function(arguments){}");
	}
}
