package org.stjs.generator.writer.methods;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

public class MethodsGeneratorTest extends AbstractStjsTest {
	@Test
	public void testPublicInstanceMethod() {
		assertCodeContains(Methods1.class, //
				"class Methods1 {" + //
						"method(arg1,arg2){return 0;}");
	}

	@Test
	public void testPrivateInstanceMethod() {
		// same as public
		assertCodeContains(Methods2.class, "class Methods2 { method(arg1, arg2){");
	}

	@Test
	public void testPublicStaticMethod() {
		assertCodeContains(Methods3.class, //
				"class Methods3 {" + //
						"static method(arg1,arg2){");
	}

	@Test
	public void testPrivateStaticMethod() {
		assertCodeContains(Methods4.class, //
				"class Methods4 {" + //
						"static method(arg1, arg2){");
	}

	@Test
	public void testMainMethod() {
		// should generate the call to the main method
		assertCodeContains(Methods5.class, "if (!stjs.mainCallDisabled) Methods5.main();");
	}

	@Test
	public void testConstructor() {
		assertCodeContains(Methods6.class, "class Methods6 { constructor(arg){} }");
	}

	@Test
	public void testSpecialThis() {
		// the special parameter THIS should not be added
		assertCodeContains(Methods7.class, "method(THIS, arg2){");
	}

	@Test
	public void testAdapter() {
		assertCodeContains(Methods8.class, "(10).toFixed(2)");
	}

	@Test
	public void testAdapterForStatic() {
		assertCodeContains(Methods14.class, "let x = (String).fromCharCode(65,66,67)");
	}

	@Test
	public void testVarArgsMethod3() {
		// only one var arg argument is allowed and the name should be "arguments" -> like the js variable
		assertCodeContains(Methods11.class, "method(..._arguments){}");
	}

	@Test
	public void testVarArgsMethod4Native() {
		assertCodeContains(Methods11_b.class, "class Methods11_b { test(props){}");

		assertCodeDoesNotContain(Methods11_b.class, "prototype.method=function");
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
		assertCodeContains(Methods15.class, "abstract class Methods15 {" //
				+ "abstract doSomething();" //
				+ "abstract doSomethingElse();" //
				+ "}");
	}

	@Test
	public void testInterfaceMethods() {
		// the class only contains interface methods, therefore nothing must be generated
		assertCodeContains(Methods15b.class, "interface Methods15b {" //
				+ "doSomething();" //
				+ "doSomethingElse();" //
				+ "}");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testSynchronizedMethod() {
		// synchronized is forbidden
		generate(Methods16.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testWrongName() {
		// keywords are forbidden
		generate(Methods17.class);
	}
}
