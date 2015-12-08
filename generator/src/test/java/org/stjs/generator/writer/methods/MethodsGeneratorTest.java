package org.stjs.generator.writer.methods;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.GeneratorConfiguration;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class MethodsGeneratorTest extends AbstractStjsTest {
	@Test
	public void testPublicInstanceMethod() {
		assertCodeContains(Methods1.class, //
				"stjs.extend(Methods1, null, [], function(constructor, prototype){" + //
						"prototype.method = function(arg1,arg2){return 0;}");
	}

	@Test
	public void testPrivateInstanceMethod() {
		// same as public
		assertCodeContains(Methods2.class, "" +
				"prototype._privateMethod = function(arg1, arg2) {\n" +
				"        return 0;\n" +
				"    };\n" +
				"    prototype.method = function() {\n" +
				"        return this._privateMethod(\"\", \"\");\n" +
				"    };");
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
						"constructor._method = function(arg1,arg2){");
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
		assertCodeContains(Methods7.class, "prototype.method=function(THIS, arg2){");
	}

	@Test
	public void testAdapter() {
		assertCodeContains(Methods8.class, "(10).toFixed(2)");
	}

	@Test
	public void testAdapterForStatic() {
		assertCodeContains(Methods14.class, "var x = (String).fromCharCode(65,66,67)");
	}

	@Test
	public void testVarArgsMethod1() {
		assertCodeContains(Methods9.class, "prototype.method = function(params) {};");
	}

	@Test
	public void testVarArgsMethod2() {
		Object result = execute(Methods10_basic_varargs.class);
		Assert.assertEquals("123", result);
	}

	@Test
	public void testVarArgsMethod3() {
		// only one var arg argument is allowed and the name should be "arguments" -> like the js variable
		assertCodeContains(Methods11.class, "prototype.method=function(_arguments){}");
	}

	@Test
	public void testVarArgsMethod4Native() {
		assertCodeContains(Methods11_b.class, "prototype.test=function(props){}");

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
		assertCodeContains(Methods15.class, "stjs.extend(Methods15, null, [], function(constructor, prototype){" //
				+ "prototype._doSomething=function(){};" //
				+ "prototype._doSomethingElse=function(){};" //
				+ "}, {}, {});");
	}

	@Test
	public void testInterfaceMethods() {
		// the class only contains abstract methods, therefore nothing must be generated
		assertCodeContains(Methods15b.class, "stjs.extend(Methods15b, null, [], function(constructor, prototype){" //
				+ "prototype.doSomething=function(){};" //
				+ "prototype.doSomethingElse=function(){};" //
				+ "}, {}, {});");
	}

	@Test
	public void testSynchronizedMethod() {
		GeneratorConfiguration configuration = new GeneratorConfigurationBuilder().setSynchronizedAllowed(true).build();
		assertCodeContains(Methods16.class, "stjs.extend(Methods16, null, [], function(constructor, prototype)" +
				"{ prototype.method = function() {" +
				"for (var i = 0; i < 10; i++) {}" +
				"}" +
				";}", configuration);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testWrongName() {
		// keywords are forbidden
		generate(Methods17.class);
	}

	@Test
	public void testOverloadMethod() {
		assertCodeContains(Methods18_overload.class, "" +
				"    prototype._overloadMethod = function() {};\n" +
				"    prototype.overloadMethodWithString = function(firstParam) {\n" +
				"        this._overloadMethod();\n" +
				"    };\n" +
				"    prototype.overloadMethod$String_int = function(firstParam, secondParam) {\n" +
				"        this.overloadMethodWithString(firstParam);\n" +
				"    };\n" +
				"    prototype.overloadMethod$String_int_CustomClass = function(firstParam, secondParam, thirdParam) {\n" +
				"        this.overloadMethod$String_int(firstParam, secondParam);\n" +
				"    };");
	}

	@Test
	public void testOverloadMethodAutoGenerated() {
		assertCodeContains(Methods19_overload_auto_generated.class, "" +
				"    prototype.overloadMethod$String = function(firstParam) {\n" +
				"        this._overloadMethod();\n" +
				"    };\n" +
				"    prototype.overloadMethod$String_int = function(firstParam, secondParam) {\n" +
				"        this.overloadMethod$String(firstParam);\n" +
				"    };\n" +
				"    prototype.overloadMethod$String_int_CustomClass = function(firstParam, secondParam, thirdParam) {\n" +
				"        this.overloadMethod$String_int(firstParam, secondParam);\n" +
				"    };");
	}

	@Test
	public void testOverloadMethodWithVarargs() {
		assertCodeContains(Methods20_overload_with_varargs.class, "" +
				"    prototype.overloadMethod$String_Array$int = function(firstParam, secondParams) {};\n" +
				"    prototype.overloadMethod$String_Array$String = function(firstParam, secondParams) {};\n" +
				"    prototype.overloadMethod$String_Array$boolean = function(firstParam, secondParams) {};\n" +
				"    prototype.overloadMethod$String_ArrayString = function(firstParam, secondParams) {};\n");
	}
}
