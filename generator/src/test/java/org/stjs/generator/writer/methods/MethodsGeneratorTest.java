package org.stjs.generator.writer.methods;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.GeneratorConfiguration;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
        assertCodeContains(Methods10_basic_varargs.class, "" +
                "        var test = new Methods10_basic_varargs().method(0, []);\n" +
                "        return new Methods10_basic_varargs().method(0, [\"1\", \"2\", \"3\"]);");
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

    @Test
    public void testForbiddenConfigurationPrivateMethod() {
        String expectedForbiddenMethod = "org.stjs.generator.writer.methods.Methods21_forbidden_configuration_private_method._forbiddenPrivateMethod";
        testForbiddenConfiguration(expectedForbiddenMethod, Methods21_forbidden_configuration_private_method.class);
    }

    @Test
    public void testForbiddenConfigurationPublicMethod() {
        String expectedForbiddenMethod = "org.stjs.generator.writer.methods.Methods22_forbidden_configuration_public_method.forbiddenPublicMethod";
        testForbiddenConfiguration(expectedForbiddenMethod, Methods22_forbidden_configuration_public_method.class);
    }

    @Test
    public void testForbiddenConfigurationInnerClassMethod() {
        String expectedForbiddenMethod = "org.stjs.generator.writer.methods.Methods23_forbidden_configuration_inner_class_method.InnerClass.forbiddenMethod";
        testForbiddenConfiguration(expectedForbiddenMethod, Methods23_forbidden_configuration_inner_class_method.class);
    }

    @Test
    public void testForbiddenConfigurationOverloadedMethod() {
        String expectedForbiddenMethod = "org.stjs.generator.writer.methods.Methods24_forbidden_configuration_overloaded_method.forbiddenMethod$String";
        testForbiddenConfiguration(expectedForbiddenMethod, Methods24_forbidden_configuration_overloaded_method.class);
    }

    @Test
    public void testOverloadMethodWithBytes() {
        assertCodeContains(Methods25_overload_string_getBytes.class, "" +
                "        var bytes = \"test\".getBytes$String(\"UTF-8\");\n");
    }

    @Test
    public void testOverloadWithGenerics() {
        assertCodeContains(Methods26_overload_with_generics.class, "" +
                "    prototype.overloadMethod$Object = function(firstParam) {};\n" +
                "    prototype.overloadMethod$Object_int = function(firstParam, secondParam) {\n" +
                "        this.overloadMethod$Object(firstParam);\n" +
                "    };");
    }

    @Test
    public void testOverrideAndOverloadInSubClass_execute() throws Exception {
        String result = (String) execute(Methods27_overrride_and_overload_in_subclass_no_conflict.class);
        Assert.assertEquals("Hello a! - Hello world from Mars! - Hello world from Mars! - Hello world! - Hello world from Mars!", result);
    }

    @Test
    public void testOverrideAndOverloadInSubClass_generation() throws Exception {
        assertCodeContains(Methods27_overrride_and_overload_in_subclass_no_conflict.class, "" +
                "    constructor.SubClassClass = function(outerClass$0) {\n" +
                "        this._outerClass$0 = outerClass$0;\n" +
                "        Methods27_overrride_and_overload_in_subclass_no_conflict.BaseClass.call(this);\n" +
                "    };\n" +
                "    constructor.SubClassClass = stjs.extend(constructor.SubClassClass, Methods27_overrride_and_overload_in_subclass_no_conflict.BaseClass, [Methods27_overrride_and_overload_in_subclass_no_conflict.Interface1], function(constructor, prototype) {\n" +
                "        prototype.getMessage = function() {\n" +
                "            return \"Hello world from Mars!\";\n" +
                "        };\n" +
                "        prototype.getMessage$String = function(userName) {\n" +
                "            return \"Hello \" + userName + \"!\";\n" +
                "        };\n" +
                "    }, {}, {});\n");
    }

    @Test
    public void testOverloadNameConflict_Subclass_with_same_name_method_but_different_signature() {
        assertCodeContains(Methods28_overloadNameConflict_Subclass_with_same_name_method_but_different_signatures.class, "" +
                "    constructor.BaseClass = stjs.extend(constructor.BaseClass, null, [], function(constructor, prototype) {\n" +
                "        prototype.aMethod1 = function(i) {};\n" +
                "        prototype.aMethod2 = function(i) {};\n" +
                "    }, {}, {});");
        assertCodeContains(Methods28_overloadNameConflict_Subclass_with_same_name_method_but_different_signatures.class, "" +
                "    constructor.SubClass = stjs.extend(constructor.SubClass, Methods28_overloadNameConflict_Subclass_with_same_name_method_but_different_signatures.BaseClass, [], function(constructor, prototype) {\n" +
                "        prototype.aMethod1$String = function(s) {};\n" +
                "        prototype.aMethod2 = function(i) {};\n" +
                "        prototype.aMethod2$String = function(s) {};\n" +
                "    }, {}, {});\n");
    }

    @Test
    public void testOverloadNameConflict_2Supertypes_with_same_method_name_but_different_signatures() {
        try {
            generate(Methods29_overloadNameConflict_2Supertypes_with_same_method_name_but_different_signatures.class);
            Assert.fail("Should not get here. An exception was expected.");
        } catch (JavascriptFileGenerationException e) {
            Assert.assertEquals(
                    "Method name conflict for: [SubClass.aMethod(int)]. Class hierarchy contains methods with the same name [aMethod] but with different signatures: 'Interface2.aMethod(java.lang.String)'",
                    e.getMessage());
        }
    }

    @Test
    public void testOverloadNameConflict_Subclass_with_same_name_method_but_different_signatures() {
        assertCodeContains(Methods30_overloadNameConflict_Resolved_by_JSOverloadName_annotation.class, "" +
                "    constructor.Interface1 = stjs.extend(constructor.Interface1, null, [], function(constructor, prototype) {\n" +
                "        prototype.aMethod = function(i) {};\n" +
                "    }, {}, {});");
        assertCodeContains(Methods30_overloadNameConflict_Resolved_by_JSOverloadName_annotation.class, "" +
                "    constructor.Interface2 = stjs.extend(constructor.Interface2, null, [], function(constructor, prototype) {\n" +
                "        prototype.aMethodWithString = function(s) {};\n" +
                "    }, {}, {});");
        assertCodeContains(Methods30_overloadNameConflict_Resolved_by_JSOverloadName_annotation.class, "" +
                "    constructor.SubClass = stjs.extend(constructor.SubClass, null, [Methods30_overloadNameConflict_Resolved_by_JSOverloadName_annotation.Interface1, Methods30_overloadNameConflict_Resolved_by_JSOverloadName_annotation.Interface2], function(constructor, prototype) {\n" +
                "        prototype.aMethod = function(i) {};\n" +
                "        prototype.aMethodWithString = function(s) {};\n" +
                "    }, {}, {});");
    }

    @Test
    public void testMethodsOverloadNameError_subclass_trying_to_change_methodName_for_signature() {
        try {
            generate(Methods31_overloadNameError_subclass_trying_to_change_methodName_for_signature.class);
            Assert.fail("Should not get here. An exception was expected.");
        } catch (JavascriptFileGenerationException e) {
            Assert.assertEquals(
                    "Method name conflict for: [SubClass.aMethod(int) --> aMethodWithInt]. Class hierarchy contains methods with the same signature [aMethod(int)] but with different resolved name: ['Interface1.aMethod(int)' --> aMethod]",
                    e.getMessage());
        }
    }

    @Test
    public void testMethodsOverloadName_with_specific_typed_class() {
        assertCodeContains(Methods32a_overloadName_with_specific_typed_class.class, "" +
                "    constructor.SubClassSpecific = stjs.extend(constructor.SubClassSpecific, Methods32a_overloadName_with_specific_typed_class.GenericInterface, [], function(constructor, prototype) {\n" +
                "        prototype.aMethod = function(s) {};\n" +
                "    }, {}, {});");
    }

    @Test
    public void testMethodsOverloadName_with_generic_typed_class() {
        assertCodeContains(Methods32b_overloadName_with_generic_typed_class.class, "" +
                "    constructor.SubClassGeneric = stjs.extend(constructor.SubClassGeneric, Methods32b_overloadName_with_generic_typed_class.GenericInterface, [], function(constructor, prototype) {\n" +
                "        prototype.aMethod = function(t) {};\n");
    }

    @Test
    public void testMethodsChangeMethodNameByConfig() {
        assertCodeContains(Methods33a_methodNameChangedByConfig.class, "" +
                        "    prototype.methodNameWithoutParametersHasBeenChangedByConfig = function() {};\n" +
                        "    prototype.methodNameWithSingleParameterHasBeenChangedByConfig = function(s) {};\n" +
                        "    prototype.methodNameWithMultipleParametersHasBeenChangedByConfig = function(s, i) {};\n",
                new GeneratorConfigurationBuilder().renamedMethodSignatures(
                        new HashMap<String, String>() {{
                            put("org.stjs.generator.writer.methods.Methods33a_methodNameChangedByConfig.methodNameWithoutParametersToBeChangedByConfig", "methodNameWithoutParametersHasBeenChangedByConfig");
                            put("org.stjs.generator.writer.methods.Methods33a_methodNameChangedByConfig.methodNameWithSingleParameterToBeChangedByConfig$String", "methodNameWithSingleParameterHasBeenChangedByConfig");
                            put("org.stjs.generator.writer.methods.Methods33a_methodNameChangedByConfig.methodNameWithMultipleParametersToBeChangedByConfig$String_int", "methodNameWithMultipleParametersHasBeenChangedByConfig");
                        }})
                        .build());
    }

    private void testForbiddenConfiguration(String expectedForbiddenMethod, Class<?> clazz) {
        Set<String> forbiddenMethodInvocations = new HashSet<>();
        forbiddenMethodInvocations.add(expectedForbiddenMethod);

        try {
            generate(clazz, new GeneratorConfigurationBuilder().forbiddenMethodInvocations(forbiddenMethodInvocations).build());
            throw new RuntimeException("Invalid forbidden method invocations configuration, the test should've failed and get trapped by the catch.");
        } catch (JavascriptFileGenerationException e) {
            Assert.assertTrue("The expected error message wasn't present.", e.getMessage().contains("You cannot access methods that are listed as forbidden."));
            Assert.assertTrue("The expected forbidden method wasn't found: " + expectedForbiddenMethod, e.getMessage().contains(expectedForbiddenMethod));
        }
    }
}
