package org.stjs.generator.writer.overload;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class OverloadConstructorTest extends AbstractStjsTest {
    @Test
    public void testSkipNativeGeneration() {
        assertCodeDoesNotContain(Overload1c.class, "n = 10");
    }

    @Test
    public void testOverloadDifferentParamNumber() {
        // check that no other method is generated
        assertCodeContains(Overload2c.class, "prototype._constructor$int_int = function(param1, param2)");
    }

    @Test
    public void testMoreGenericType() {
        // check that no other method is generated
        assertCodeContains(Overload3c.class, "" +
                "{\n" +
                "    prototype._constructor$Object = function(param1) {\n" +
                "        return this;\n" +
                "    };\n" +
                "}");
    }

    @Test
    public void testVarArgs() {
        // check that no other method is generated
        assertCodeContains(Overload6c.class, "" +
                "    prototype._constructor$Array$Object = function(_arguments) {\n" +
                "        return this;\n" +
                "    };\n");
    }

    @Test
    public void testOverloadMultipleConstructors() {
        assertCodeContains(Overload7_multiple_constructors.class, "" +
                "var Overload7_multiple_constructors = function() {\n" +
                "    this._param1 = \"test\";" +
                "    this._param2 = [];\n" +
                "};");
        assertCodeContains(Overload7_multiple_constructors.class, "" +
                "    prototype._constructor = function() {\n" +
                "        this._constructor$String(\"ok\");\n" +
                "        this._param2.push(\"1\");\n" +
                "        return this;\n" +
                "    };\n" +
                "    prototype._constructor$String = function(param1) {\n" +
                "        this._constructor$String_Array(param1, null);\n" +
                "        this._param2.push(\"2\");\n" +
                "        return this;\n" +
                "    };\n" +
                "    prototype._constructor$String_Array = function(param1, param2) {\n" +
                "        this._param1 = param1;\n" +
                "        if (param2 != null) {\n" +
                "            this._param2 = param2;\n" +
                "        }\n" +
                "        this._param2.push(\"3\");\n" +
                "        return this;\n" +
                "    };");
        assertCodeContains(Overload7_multiple_constructors.class, "" +
                "        var clazz = new Overload7_multiple_constructors()._constructor$String(\"2\");");
    }

    @Test
    public void testOverloadMultipleConstructorsCall() {
        // Make sure to re-generate the class used in the test
        generate(Overload7_multiple_constructors.class);

        assertCodeContains(Overload8_multiple_constructors_call.class, "" +
                "        var multipleConstructors = new Overload7_multiple_constructors()._constructor();\n" +
                "        return multipleConstructors.getArray().toString();\n");

        Object result = execute(Overload8_multiple_constructors_call.class);
        Assert.assertEquals("3,2,1", result);
    }

    @Test
    public void testOverloadMultipleConstructorsCallSuperConstructor() throws Exception {
        String code = generate(Overload10_multiple_constructors_call_super.class);
        assertCodeContains(code, "" +
                "    constructor.BaseClass = stjs.extend(constructor.BaseClass, stjs.Java.Object, [], function(constructor, prototype) {\n" +
                "        prototype._constructor = function() {\n" +
                "            this._trace.push(\"BaseClass()\");\n" +
                "            return this;\n" +
                "        };\n" +
                "        prototype._constructor$int = function(i) {\n" +
                "            this._trace.push(\"BaseClass(int \" + i + \")\");\n" +
                "            return this;\n" +
                "        };\n");

        // A
        assertCodeContains(code, "" +
                "        prototype._constructor = function() {\n" +
                "            this._trace.push(\"BaseClass()\");\n" +
                "            return this;\n" +
                "        };\n"
        );

        // B
        assertCodeContains(code, "" +
                "       constructor.SubClassB = stjs.extend(constructor.SubClassB, Overload10_multiple_constructors_call_super.BaseClass, [], function(constructor, prototype) {\n" +
                "           prototype._constructor$String = function(s) {\n" +
                "               Overload10_multiple_constructors_call_super.BaseClass.prototype._constructor.call(this);\n" +
                "               this._trace.push(\"SubClassB(String \" + s + \")\");\n" +
                "               return this;\n" +
                "           };\n"
        );

        Assert.assertEquals(
                "BaseClass() ### BaseClass(int 111) ### BaseClass(int 999) SubClassA(String AAA) ### BaseClass(int 222) SubClassA(int 222) ### BaseClass() SubClassB(String BBB)",
                execute(Overload10_multiple_constructors_call_super.class)
        );

    }
}
