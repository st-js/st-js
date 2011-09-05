package org.stjs.generator;

import static org.stjs.generator.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.methods.Methods1;
import test.generator.methods.Methods2;
import test.generator.methods.Methods3;
import test.generator.methods.Methods4;
import test.generator.methods.Methods5;

public class MethodsGeneratorTest {
	@Test
	public void testPublicInstanceMethod() {
		assertCodeContains(Methods1.class, "Methods1.prototype.method = function(arg1,arg2){");
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
		assertCodeContains(Methods5.class, "Methods5.main();");
	}
}
