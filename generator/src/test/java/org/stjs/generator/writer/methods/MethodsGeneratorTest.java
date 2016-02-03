package org.stjs.generator.writer.methods;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

import java.util.HashSet;
import java.util.Set;

public class MethodsGeneratorTest extends AbstractStjsTest {
    @Test
    public void testPublicInstanceMethod() {
        assertCodeContains(Methods1.class, "" + //
                "stjs.extend(Methods1, stjs.Java.Object, [], function(constructor, prototype) {\n" +
                "    prototype.method = function(arg1, arg2) {\n" +
                "        return 0;\n" +
                "    };");
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
                "stjs.extend(Methods3, stjs.Java.Object, [], function(constructor, prototype){" + //
                        "constructor.method = function(arg1,arg2){");
    }

    @Test
    public void testPrivateStaticMethod() {
        assertCodeContains(Methods4.class, //
                "stjs.extend(Methods4, stjs.Java.Object, [], function(constructor, prototype){" + //
                        "constructor._method = function(arg1,arg2){");
    }

    @Test
    public void testMainMethod() {
        // should generate the call to the main method
        assertCodeContains(Methods5.class, "if (!stjs.mainCallDisabled) Methods5.main();");
    }

    @Test
    public void testConstructor() {
        assertCodeContains(Methods6.class, "prototype._constructor$String = function(arg) {");
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
    public void testVarArgsParameters() {
        assertCodeContains(Methods10_basic_varargs.class, "var invokedWithVarArgAsFirstParameters = obj.methodVarArgAsFirstParameter([\"A\", \"B\", \"C\"]);");
        assertCodeContains(Methods10_basic_varargs.class, "var invokedWithVargArgAsSecondParameters = obj.methodVarArgAsSecondParameter(0, [\"1\", \"2\", \"3\"]);");

        assertCodeContains(Methods10_basic_varargs.class, "var missingVarArgInvokedWithAsFirstParameters = obj.methodVarArgAsFirstParameter([]);");
        assertCodeContains(Methods10_basic_varargs.class, "var missingVarArgInvokedWithAsSecondParameters = obj.methodVarArgAsSecondParameter(0, []);");

        assertCodeContains(Methods10_basic_varargs.class, "var invokedStaticMethodVarArgAsSecondParameters = Methods10_basic_varargs.staticMethodVarArgAsSecondParameter(0, [\"H\", \"I\", \"J\"]);");
        assertCodeContains(Methods10_basic_varargs.class, "var invokedStaticMethodVarArgAsSecondParameters = Methods10_basic_varargs.staticMethodVarArgAsSecondParameter(0, [\"H\", \"I\", \"J\"]);");

        assertCodeContains(Methods10_basic_varargs.class, "var arrayAsVarArgInvokedWithAsFirstParameters = obj.methodVarArgAsFirstParameter(stringArray);");
        assertCodeContains(Methods10_basic_varargs.class, "var arrayAsVarArgInvokedWithAsSecondParameters = obj.methodVarArgAsSecondParameter(0, stringArray);");

        Object result = execute(Methods10_basic_varargs.class);
        Assert.assertEquals("A,B,C-1,2,3---D,E,F-H,I,J-K,L,M-N,O,P.", result);
    }

    @Test
    public void testVarArgsConstructor() {
        assertCodeContains(Methods10b_constructor_vararg.class, "new ClassWithVarArgInConstructorAsSingleParameter()._constructor$Array$String([\"A\", \"B\", \"C\"]);");
        assertCodeContains(Methods10b_constructor_vararg.class, "new ClassWithVarArgInConstructorAsSecondParameter()._constructor$int_Array$String(0, [\"A\", \"B\", \"C\"])");
    }

    @Test
    public void testVarArgsMethod3() {
        // only one var arg argument is allowed and the name should be "arguments" -> like the js variable
        assertCodeContains(Methods11a_varArgs.class, "prototype.method=function(_arguments){}");
    }

    @Test
    public void testVarArgsMethod4Native() {
        assertCodeContains(Methods11b_varArgs.class, "prototype.test=function(props){}");

        assertCodeDoesNotContain(Methods11b_varArgs.class, "prototype.method=function");
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
        assertCodeContains(Methods15.class, "stjs.extend(Methods15, stjs.Java.Object, [], function(constructor, prototype){" //
                + "prototype._doSomething=function(){};" //
                + "prototype._doSomethingElse=function(){};" //
                + "}, {}, {}, \"Methods15\");");
    }

    @Test
    public void testInterfaceMethods() {
        // the class only contains abstract methods, therefore nothing must be generated
        assertCodeContains(Methods15b.class, "stjs.extend(Methods15b, stjs.Java.Object, [], function(constructor, prototype){" //
                + "prototype.doSomething=function(){};" //
                + "prototype.doSomethingElse=function(){};" //
                + "}, {}, {}, \"Methods15b\");");
    }

    @Test
    public void testSynchronizedMethod() {
        assertCodeContains(Methods16.class,
                new GeneratorConfigurationBuilder().setSynchronizedAllowed(true).build(),
                "" +
                        "stjs.extend(Methods16, stjs.Java.Object, [], function(constructor, prototype)" +
                        "{ prototype.method = function() {" +
                        "for (var i = 0; i < 10; i++) {}" +
                        "}" +
                        ";}");
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
                "    constructor.SubClassClass = function () {\n" +
                "        Methods27_overrride_and_overload_in_subclass_no_conflict.BaseClass.call(this);\n" +
                "    };\n" +
                "    constructor.SubClassClass = stjs.extend(constructor.SubClassClass, Methods27_overrride_and_overload_in_subclass_no_conflict.BaseClass, [Methods27_overrride_and_overload_in_subclass_no_conflict.Interface1], function(constructor, prototype) {\n" +
                "        prototype.getMessage = function() {\n" +
                "            return \"Hello world from Mars!\";\n" +
                "        };\n" +
                "        prototype.getMessage$String = function(userName) {\n" +
                "            return \"Hello \" + userName + \"!\";\n" +
                "        };\n");
    }

    @Test
    public void testOverloadNameConflict_Subclass_with_same_name_method_but_different_signature() {
        assertCodeContains(Methods28_overloadNameConflict_Subclass_with_same_name_method_but_different_signatures.class, "" +
                "    constructor.BaseClass = stjs.extend(constructor.BaseClass, stjs.Java.Object, [], function(constructor, prototype) {\n" +
                "        prototype.aMethod1 = function(i) {};\n" +
                "        prototype.aMethod2 = function(i) {};\n" +
                "    }");
        assertCodeContains(Methods28_overloadNameConflict_Subclass_with_same_name_method_but_different_signatures.class, "" +
                "    constructor.SubClass = stjs.extend(constructor.SubClass, Methods28_overloadNameConflict_Subclass_with_same_name_method_but_different_signatures.BaseClass, [], function(constructor, prototype) {\n" +
                "        prototype.aMethod1$String = function(s) {};\n" +
                "        prototype.aMethod2 = function(i) {};\n" +
                "        prototype.aMethod2$String = function(s) {};\n" +
                "    }");
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
                "    constructor.Interface1 = stjs.extend(constructor.Interface1, stjs.Java.Object, [], function(constructor, prototype) {\n" +
                "        prototype.aMethod = function(i) {};\n" +
                "    }");
        assertCodeContains(Methods30_overloadNameConflict_Resolved_by_JSOverloadName_annotation.class, "" +
                "    constructor.Interface2 = stjs.extend(constructor.Interface2, stjs.Java.Object, [], function(constructor, prototype) {\n" +
                "        prototype.aMethodWithString = function(s) {};\n" +
                "    }");
        assertCodeContains(Methods30_overloadNameConflict_Resolved_by_JSOverloadName_annotation.class, "" +
                "    constructor.SubClass = stjs.extend(constructor.SubClass, stjs.Java.Object, [Methods30_overloadNameConflict_Resolved_by_JSOverloadName_annotation.Interface1, Methods30_overloadNameConflict_Resolved_by_JSOverloadName_annotation.Interface2], function(constructor, prototype) {\n" +
                "        prototype.aMethod = function(i) {};\n" +
                "        prototype.aMethodWithString = function(s) {};\n" +
                "    }");
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
                "    }");
    }

    @Test
    public void testMethodsOverloadName_with_generic_typed_class() {
        assertCodeContains(Methods32b_overloadName_with_generic_typed_class.class, "" +
                "    constructor.SubClassGeneric = stjs.extend(constructor.SubClassGeneric, Methods32b_overloadName_with_generic_typed_class.GenericInterface, [], function(constructor, prototype) {\n" +
                "        prototype.aMethod = function(t) {};\n");
    }

    @Test
    public void testMethodsOverloadName_extends_baseclass_with_typed_vars_and_override_concrete_method() {
        assertCodeContains(Methods32c_overloadName_extends_baseclass_with_typed_vars_and_override_concrete_method.class, "" +
                "    constructor.SpecificType = stjs.extend(constructor.SpecificType, Methods32c_overloadName_extends_baseclass_with_typed_vars_and_override_concrete_method.AbstractClass, [], function(constructor, prototype) {\n" +
                "        prototype._aMethod = function(item) {};\n" +
                "    }");
        assertCodeContains(Methods32c_overloadName_extends_baseclass_with_typed_vars_and_override_concrete_method.class, "" +
                "    constructor.AbstractClass = stjs.extend(constructor.AbstractClass, stjs.Java.Object, [], function(constructor, prototype) {\n" +
                "        prototype._aMethod = function(item) {};\n" +
                "    }");
    }

    @Test
    public void testMethodsOverloadName_override_method_declaring_typedvar_type() {
        assertCodeContains(Methods32d_overloadName_override_method_declaring_typedvar_type.class, "" +
                "    constructor.TheInterface = stjs.extend(constructor.TheInterface, stjs.Java.Object, [], function(constructor, prototype) {\n" +
                "        prototype.add = function(list, value) {};");
        assertCodeContains(Methods32d_overloadName_override_method_declaring_typedvar_type.class, "" +
                "    constructor.TheClass = stjs.extend(constructor.TheClass, stjs.Java.Object, [Methods32d_overloadName_override_method_declaring_typedvar_type.TheInterface], function(constructor, prototype) {\n" +
                "        prototype.add = function(list, value) {};");
    }

    @Test
    public void testMethodsOverloadName_with_specific_typed_class_including_generics() {
        assertCodeContains(Methods32e_overloadName_with_specific_typed_class_including_generics.class,
                "" +
                        "    constructor.GenericInterface = stjs.extend(constructor.GenericInterface, stjs.Java.Object, [], function(constructor, prototype) {\n" +
                        "        prototype.aMethod = function(t) {};\n" +
                        "    }",

                "" +
                        "    prototype.method = function() {\n" +
                        "        return new (stjs.extend(function Methods32e_overloadName_with_specific_typed_class_including_generics$1() {}, stjs.Java.Object, [Methods32e_overloadName_with_specific_typed_class_including_generics.GenericInterface], function(constructor, prototype) {\n" +
                        "            prototype.aMethod = function(dto) {\n" +
                        "                return 1234;\n" +
                        "            };\n" +
                        "        }");
    }


    @Test
    public void testMethodsChangeMethodNameByConfig() {
        assertCodeContains(Methods33a_methodNameChangedByConfig.class,
                new GeneratorConfigurationBuilder()
                        .renamedMethodSignature("org.stjs.generator.writer.methods.Methods33a_methodNameChangedByConfig.methodNameWithoutParametersToBeChangedByConfig", "methodNameWithoutParametersHasBeenChangedByConfig")
                        .renamedMethodSignature("org.stjs.generator.writer.methods.Methods33a_methodNameChangedByConfig.methodNameWithSingleParameterToBeChangedByConfig$String", "methodNameWithSingleParameterHasBeenChangedByConfig")
                        .renamedMethodSignature("org.stjs.generator.writer.methods.Methods33a_methodNameChangedByConfig.methodNameWithMultipleParametersToBeChangedByConfig$String_int", "methodNameWithMultipleParametersHasBeenChangedByConfig")
                        .build(),
                "" +
                        "    prototype.methodNameWithoutParametersHasBeenChangedByConfig = function() {};\n" +
                        "    prototype.methodNameWithSingleParameterHasBeenChangedByConfig = function(s) {};\n" +
                        "    prototype.methodNameWithMultipleParametersHasBeenChangedByConfig = function(s, i) {};\n"
                );
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

    @Test
    public void testStjsArray_push_varArgs() throws Exception {
        assertCodeContains(Methods34_StjsArray_push_varArgs.class, "" +
                "        stjsArray.push(\"a\");\n" +
                "        stjsArray.push(\"b\", \"c\");\n");
    }

    @Test
    public void test_early_return_in_constructor() throws Exception {
        assertCodeContains(Methods35_early_return_in_constructor.class, "" +
                "    prototype._constructor$boolean = function(b) {\n" +
                "        (function() {\n" +
                "            this.trace.push(\"before if\");\n" +
                "            if (b) {\n" +
                "                this.trace.push(\"inside if and before early return\");\n" +
                "                return;\n" +
                "            }\n" +
                "            this.trace.push(\"last line of constructor\");\n" +
                "        }.call)(this);\n" +
                "        return this;\n" +
                "    };\n");

        Assert.assertEquals("before if, last line of constructor ### before if, inside if and before early return", execute(Methods35_early_return_in_constructor.class));
    }
}
