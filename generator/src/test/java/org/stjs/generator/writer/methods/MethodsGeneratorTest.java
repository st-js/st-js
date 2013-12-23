package org.stjs.generator.writer.methods;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

public class MethodsGeneratorTest {
	@Test
	public void testPublicInstanceMethod() {
		assertCodeContains(Methods1.class, //
				"stjs.extend(Methods1, null, [], function(constructor, prototype){" + //
						"prototype.method = function(arg1,arg2){return 0;}");
	}

	@Test
	public void testPrivateInstanceMethod() {
		// same as public
		assertCodeContains(Methods2.class, //
				"stjs.extend(Methods2, null, [], function(constructor, prototype){" + //
						"prototype.method = function(arg1,arg2){");
	}

	@Test
	public void testPublicStaticMethod() {
		assertCodeContains(Methods3.class, //
				"stjs.extend(Methods3, null, [], function(constructor, prototype){" + //
						"constructor.method = function(arg1,arg2){");
	}

	@Test
	public void testPrivateStaticMethod() {
		assertCodeContains(Methods4.class, //
				"stjs.extend(Methods4, null, [], function(constructor, prototype){" + //
						"constructor.method = function(arg1,arg2){");
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
		assertCodeContains(Methods7.class, "prototype.method=function(arg2){");
	}

	@Test
	public void testAdapter() {
		assertCodeContains(Methods8.class, "(10).toFixed(2)");
	}

	@Test
	public void testAdapterForStatic() {
		assertCodeContains(Methods14.class, "var x = (String).fromCharCode(65,66,67)");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testVarArgsMethod1() {
		// only one var arg argument is allowed and the name should be "arguments" -> like the js variable
		generate(Methods9.class);
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testVarArgsMethod2() {
		// only one var arg argument is allowed and the name should be "arguments" -> like the js variable
		generate(Methods10.class);
	}

	@Test
	public void testVarArgsMethod3() {
		// only one var arg argument is allowed and the name should be "arguments" -> like the js variable
		assertCodeContains(Methods11.class, "prototype.method=function(_arguments){}");
	}

	@Test
	public void testInterfaceImplResolution() {
		assertCodeContains(Methods12.class, "method(c);");
	}

	@Test
	public void testWildcardResolution() {
		assertCodeContains(Methods13.class, "m.parent().parent()");
	}

	@Test
	public void testAbstractMethod() {
		// the class only contains abstract methods, therefore nothing must be generated
		assertCodeContains(Methods15.class, "stjs.extend(Methods15, null, [], null, {});");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testSynchronizedMethod() {
		// synchronized is forbidden
		generate(Methods16.class);
	}
}
