package org.stjs.generator.writer.innerTypes;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class InnerTypesGeneratorTest extends AbstractStjsTest {
    @Test
    public void testCreateInstanceInnerType() {
        assertCodeContains(InnerTypes1.class, "new InnerTypes1.InnerType(this)");
    }

    @Test
    public void testCreateStaticInnerType() {
        assertCodeContains(InnerTypes2.class, "new InnerTypes2.InnerType()");
    }

    @Test
    public void testDeclarationAndAccessInnerTypeInstanceMethod() {
        assertCodeContains(InnerTypes3.class,
                "stjs.extend(constructor.InnerType, null, [], function(constructor, prototype){prototype.innerMethod=function()");
        assertCodeContains(InnerTypes3.class, "new InnerTypes3.InnerType(this).innerMethod()");
        assertCodeContains(InnerTypes3.class, "var x = new InnerTypes3.InnerType(this)");
        assertCodeDoesNotContain(InnerTypes3.class, "function(constructor, prototype){InnerTypes3.InnerType=");
    }

    @Test
    public void testDeclarationAndAccessInnerTypeInstanceField() {
        assertCodeContains(InnerTypes4.class,
                "stjs.extend(constructor.InnerType, null, [], function(constructor, prototype){prototype.innerField=0");
        assertCodeContains(InnerTypes4.class, "new InnerTypes4.InnerType().innerField");
        assertCodeDoesNotContain(InnerTypes4.class, "function(constructor,prototype){InnerTypes4.InnerType=");
    }

    @Test
    public void testInheritance() {
        assertCodeContains(InnerTypes5.class, "stjs.extend(constructor.InnerType, MySuperClass, [], ");
    }

    @Test
    public void testCallToQualifiedOuterType() {
        assertCodeContains(InnerTypes6.class, "var m = this._outerClass$0._n;");
        assertCodeContains(InnerTypes6.class, "" +
                "    constructor.InnerType = function(outerClass$0) {\n" +
                "        this._outerClass$0 = outerClass$0;\n" +
                "    };");
    }

    @Test
    public void testCallFieldToQualifiedOuterType() {
        assertCodeContains(InnerTypes6a.class, "var m = this._outerClass$0._n;");
    }

    @Test
    public void testCallMethodOuterType() {
        assertCodeContains(InnerTypes6b.class, "var m = this._outerClass$0.method();");
    }

    @Test
    public void testCallMethodToQualifiedOuterType() {
        assertCodeContains(InnerTypes6c.class, "var m = this._outerClass$0.method();");
    }

    @Test
    public void testCallMethodToQualifiedOuterTypeExecution() {
        Object result = execute(InnerTypes6d_execution.class);
        Assert.assertEquals("MyClass$InnerClass,MyClass$InnerClass,MyClass", result);
    }

    @Test
    public void testExternalAccessToInnerType() {
        assertCodeContains(InnerTypes7.class, "new InnerTypes4.InnerType()");
    }

    @Test
    public void testExternalAndQualifiedAccessToInnerType() {
        assertCodeContains(InnerTypes8.class, "new InnerTypes4.InnerType()");
    }

    @Test
    public void testQualifiedFieldAccess() {
        assertCodeContains(InnerTypes9.class, "n = InnerTypes9.InnerType.innerField");
    }

    @Test
    public void testNonStaticInnerType() {
        // for non-static inner classes the constructor contains as first parameter the type of the outer type
        assertCodeContains(InnerTypes10.class, "" +
                "    constructor.InnerClass = function(outerClass$0, p) {\n" +
                "        this._outerClass$0 = outerClass$0;");
    }

    @Test
    public void testNonStaticInnerEnum() {
        // for non-static inner classes the constructor contains as first parameter the type of the outer type
        // also enum has first two params name and ordinal
        assertCodeContains(InnerTypes11.class,
                // Block 1
                "" +
                        "    constructor.InnerEnum = function() {\n" +
                        "        JavaEnum.call(this);\n" +
                        "    };",

                // Block 2
                "" +
                        "        constructor.a = new InnerTypes11.InnerEnum();\n" +
                        "        constructor.a._name = \"a\";\n" +
                        "        constructor.a._ordinal = 0;\n" +
                        "        constructor._values = [constructor.a];");

    }

    @Test
    public void testDeadCode() {
        // the compiler will not generate the code inside the if (dead code), so the inner type may not be found
        assertCodeDoesNotContain(InnerTypes12.class, "$invoke");
    }

    @Test
    public void testDeadCode2() {
        // check bug where inner types where not correctly detected
        assertCodeContains(InnerTypes13.class, "" +
                "    constructor.Button = function(content, handler) {};\n" +
                "    constructor.Button = stjs.extend(constructor.Button, null, [], null, {}, {}, \"InnerTypes13.Button\");");
    }

    @Test
    public void testInnerInsideInner() {
        String code = generate(InnerTypes15.class);
        assertCodeContains(code,
                // Validation Block
                "var InnerTypes15 = function(){};" + "InnerTypes15 = stjs.extend(InnerTypes15, null, [], function(constructor, prototype){",

                // Validation Block
                "var deep = new InnerTypes15.Inner.InnerDeep()",

                // Validation Block
                "    constructor.Inner = function() {};\n" +
                "    constructor.Inner = stjs.extend(constructor.Inner, null, [], function(constructor, prototype) {",

                // Validation Block
                "        constructor.InnerDeep = function() {};\n" +
                "        constructor.InnerDeep = stjs.extend(constructor.InnerDeep, null, [], function(constructor, prototype) {\n" +
                "            prototype._b = null;\n");
    }

    @Test
    public void testInnerInsideAnonymous() {
        String code = generate(InnerTypes16.class);
        assertCodeContains(code, "var InnerTypes16 = function(){};" + "InnerTypes16 = stjs.extend(InnerTypes16, null, [], function(constructor, prototype){");
        assertCodeContains(code, "var o = new (stjs.extend(function InnerTypes16$1(){}, Object, [], function(constructor, prototype){");
        assertCodeContains(code, "" +
                "            prototype.denver = function() {\n" +
                "                var deep = new InnerTypes16.InnerTypes16$1.InnerDeep(this);\n" +
                "            };\n" +
                "            constructor.InnerDeep = function(outerClass$1) {\n" +
                "                this._outerClass$1 = outerClass$1;\n" +
                "            };\n" +
                "            constructor.InnerDeep = stjs.extend(constructor.InnerDeep, null, [], function(constructor, prototype) {\n" +
                "                prototype._a = null;");
    }

    @Test
    public void testEnumInsideInner() {
        String code = generate(InnerTypes17.class);
        assertCodeContains(code, "var InnerTypes17 = function(){};" + "InnerTypes17 = stjs.extend(InnerTypes17, null, [], function(constructor, prototype){");
        assertCodeContains(code, "var deep = InnerTypes17.Inner._Enum.a;");
        assertCodeContains(code, "stjs.extend(constructor.Inner, null, [], function(constructor, prototype){");
        assertCodeContains(code, "constructor.Enum = stjs.extend(constructor.Enum, JavaEnum");
    }

    @Test
    public void testAnonymousInsideAnonymous() {
        String code = generate(InnerTypes18.class);
        assertCodeContains(code, "var o = new (stjs.extend(function InnerTypes18$1(){}, Object, [], function(constructor, prototype){");
        assertCodeContains(code, "var o2 = new (stjs.extend(function InnerTypes18$1$1(){}, Object, [], function(constructor, prototype){");
    }

    @Test
    public void testAnonymousInsideInner() {
        String code = generate(InnerTypes19.class);
        assertCodeContains(code, "" +
                "    constructor.Inner = function(outerClass$0) {\n" +
                "        this._outerClass$0 = outerClass$0;\n" +
                "    };\n" +
                "    constructor.Inner = stjs.extend(constructor.Inner, null, [], function(constructor, prototype) {");
        assertCodeContains(code, "return new (stjs.extend(function InnerTypes19$Inner$1(){}, Object, [], function(constructor, prototype){");
    }

    @Test
    public void testInnerConstantAssignment() {
        Object result = execute(InnerTypes20.class);
        assertNotNull(result);
        assertEquals(2, ((Number) result).intValue());

        assertCodeContains(InnerTypes20.class, "" +
                "    constructor.Holder = function() {};\n" +
                "    constructor.Holder = stjs.extend(constructor.Holder, null, [], function(constructor, prototype) {\n" +
                "        constructor._VALUE = 2;\n" +
                "    }, {}, {}, \"InnerTypes20.Holder\");\n" +
                "    constructor._currentValue = InnerTypes20.Holder._VALUE;\n");
    }

    @Test
    public void testAnonymousTypeMethodExecution() {
        Object result = execute(InnerTypes21.class);
        assertNotNull(result);
        assertEquals(5, ((Number) result).intValue());
    }

    @Test
    public void testCallPrivateMethodFromAnonymous() {
        assertCodeContains(InnerTypes22.class, "return this._privateMethod()");
    }

    @Test
    public void testCallOuterMethodFromAnonymousInit() {
        assertCodeContains(InnerTypes23.class, "x: this.outerMethod()");
    }

    @Test
    public void testStaticInnerClassDoNotReceiveRefToParent() {
        assertCodeContains(InnerTypes24_static_inner_class_do_not_receive_ref_to_parent.class,
                "" +
                        "        var obj = new InnerTypes24_static_inner_class_do_not_receive_ref_to_parent.Inner();",

                "" +
                        "        constructor.Inner = function() {\n" +
                        "        };\n");


    }
}
