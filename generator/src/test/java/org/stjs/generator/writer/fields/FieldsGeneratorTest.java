package org.stjs.generator.writer.fields;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FieldsGeneratorTest extends AbstractStjsTest {
    @Test
    public void testPrimitiveIntInstanceField() {
        assertCodeContains(Fields1.class, "prototype.x = 0;");
    }

    @Test
    public void testWrapperIntInstanceField() {
        assertCodeContains(Fields1a.class, "prototype.x = null;");
    }

    @Test
    public void testPrimitiveDoubleField() {
        assertCodeContains(Fields1b.class, "prototype.x = 0.0;");
    }

    @Test
    public void testInstanceFieldAssigned() {
        assertCodeContains(Fields2.class, "prototype.x = 2;");
    }

    @Test
    public void testInstanceFieldAssignedNegative() {
        assertCodeContains(Fields2b.class, "prototype.x = -2;");
    }

    @Test
    public void testMultipleInstanceField() {
        assertCodeContains(Fields3.class, "prototype.x = 2; prototype.y = 3;");
    }

    @Test
    public void testStaticField() {
        assertCodeContains(Fields4.class, "constructor.x = 2;");
    }

    @Test
    public void testAllowStaticFieldInit() {
        assertCodeContains(Fields7.class, "constructor.x = {};");
    }

    @Test
    public void testInstanceFieldInitWithNonLiterals() {
        assertCodeContains(Fields8.class, "{\n" +
                "    this.y = this.x;\n" +
                "}");

        assertCodeContains(Fields8.class, "{\n" +
                "    prototype.x = 2;\n" +
                "    prototype.y = 0;\n" +
                "}");
    }

    @Test
    public void testParameterizedType() {
        assertCodeContains(Fields9.class, "prototype.field = null;");
    }

    @Test
    public void testGeneric() {
        assertCodeContains(Fields10.class, "prototype.field = null;");
    }

    @Test
    public void testAccessOuterStaticField() {
        assertCodeContains(Fields11.class, "a = Fields11.FIELD;");
    }

    @Test
    public void testPrototypeProperty() {
        assertCodeContains(Fields12.class, "clazz=(String).prototype;");
    }

    @Test
    public void testPrivateFinalBooleanBug() {
        assertCodeContains(Fields13.class, "prototype._value = false;");
    }

    @Test
    public void testNonPublicFieldWithPublicMethodOfSameName() {
        assertCodeContains(Fields27_non_public_field_with_public_method.class, "" +
                "    prototype._isCool = 0;\n" +
                "    prototype.isCool = function() {\n" +
                "        return this._isCool;\n" +
                "    };");
    }

    @Test
    public void testNoModifiersFieldExpectedToNotBePublic() {
        assertCodeContains(Fields26_non_public_prefix.class, "" +
                "    prototype._packageField = null;\n" +
                "    prototype._privateField = null;\n" +
                "    prototype.publicField = null;\n" +
                "    prototype.getThisPackageField = function() {\n" +
                "        var myFields25nonpublicprefix = new Fields26_non_public_prefix();\n" +
                "        myFields25nonpublicprefix._packageField = \"test\";\n" +
                "        return this._packageField;\n" +
                "    };\n" +
                "    prototype.getThisPrivateField = function() {\n" +
                "        return this._privateField;\n" +
                "    };\n" +
                "    prototype.getThisPublicField = function() {\n" +
                "        return this.publicField;\n" +
                "    };\n" +
                "    prototype.getPackageField = function() {\n" +
                "        return this._packageField;\n" +
                "    };\n" +
                "    prototype.getPrivateField = function() {\n" +
                "        return this._privateField;\n" +
                "    };\n" +
                "    prototype.getPublicField = function() {\n" +
                "        return this.publicField;\n" +
                "    };\n" +
                "    constructor.InnerClass = function(outerClass$0, parent) {\n" +
                "        this._outerClass$0 = outerClass$0;\n" +
                "        this._parent = parent;\n" +
                "    };\n" +
                "    constructor.InnerClass = stjs.extend(constructor.InnerClass, null, [], function(constructor, prototype) {\n" +
                "        prototype._innerPackageField = null;\n" +
                "        prototype._innerPrivateField = null;\n" +
                "        prototype.innerPublicField = null;\n" +
                "        prototype._parent = null;\n" +
                "        prototype.getThisPackageField = function() {\n" +
                "            var myFields25nonpublicprefix = new Fields26_non_public_prefix();\n" +
                "            myFields25nonpublicprefix._packageField = \"test\";\n" +
                "            return this._innerPackageField;\n" +
                "        };\n" +
                "        prototype.getThisPrivateField = function() {\n" +
                "            return this._innerPrivateField;\n" +
                "        };\n" +
                "        prototype.getThisPublicField = function() {\n" +
                "            return this.innerPublicField;\n" +
                "        };\n" +
                "        prototype.getPackageField = function() {\n" +
                "            return this._parent._packageField;\n" +
                "        };\n" +
                "        prototype.getPrivateField = function() {\n" +
                "            return this._parent._privateField;\n" +
                "        };\n" +
                "        prototype.getPublicField = function() {\n" +
                "            return this._parent.publicField;\n" +
                "        };");
    }

    @Test
    public void testStaticFieldsDependencies() {
        Object result = execute(Fields14.class);
        assertNotNull(result);
        assertEquals(2, ((Number) result).intValue());
    }

    @Test
    public void testTemplateSetter1() {
        assertCodeContains(Fields15.class, "this.set(\"field\", n)");
    }

    @Test
    public void testTemplateSetter2() {
        assertCodeContains(Fields16.class, "f.set(\"field\", n)");
    }

    @Test
    public void testTemplateSetter3() {
        assertCodeContains(Fields15a.class, "Fields15a.set(\"field\", n)");
    }

    @Test
    public void testTemplateGlobalSetter1() {
        assertCodeContains(Fields17.class, "stjs.setField(this, \"field\", n)");
    }

    @Test
    public void testTemplateGlobalSetter2() {
        assertCodeContains(Fields18.class, "stjs.setField(f, \"field\", n)");
    }

    @Test
    public void testTemplatePostIncrement() {
        assertCodeContains(Fields19.class, "this.set(\"field\", this.get(\"field\") + 1, true)");
    }

    @Test
    public void testTemplatePreIncrement() {
        assertCodeContains(Fields20.class, "this.set(\"field\", this.get(\"field\") + 1)");
    }

    @Test
    public void testTemplateGlobalPostIncrement() {
        assertCodeContains(Fields21.class, "stjs.setField(this, \"field\", stjs.getField(this, \"field\") + 1, true)");
    }

    @Test
    public void testTemplateAddAssign() {
        assertCodeContains(Fields22.class, "this.set(\"field\", this.get(\"field\")  | (2))");
    }

    @Test
    public void testTemplateGlobalDivideAssign() {
        assertCodeContains(Fields23.class, "stjs.setField(this, \"field\", stjs.trunc(stjs.getField(this, \"field\") / (2)))");
    }

    @Test
    public void testTemplateIdentifier() {
        assertCodeContains(Fields24.class, "this.get(\"field\")");
    }

    @Test
    public void testTemplateMemberSelect() {
        assertCodeContains(Fields25.class, "obj.get(\"field\")");
    }

    @Test
    public void testArray() {
        assertCodeContains(Fields28_array.class, "" +
                "    this._aBooleanArray = [true, false];\n" +
                "    this._aStringArray = [\"a\", \"b\", \"c\", \"d\", \"e\"];\n" +
                "    this._anIntTwoDimensArray = [[0, 1], [2, 3]];\n" +
                "    this._aCharArray = ['n', 'o', 't', ' ', 'a', ' ', 'S', 't', 'r', 'i', 'n', 'g'];\n" +
                "    this._anObjectArray = [new Fields28_array.SimpleObject(this, this._anIntTwoDimensArray), new Fields28_array.SimpleObject(this, [[0]])];\n" +
                "    this._aFloatArray = [2.0, 3.6969];\n" +
                "    this._aCollectionArray = [];\n" +
                "    this._anInterfaceArray = [new (stjs.extend(function Fields28_array$1() {}, null, [Fields28_array.SimpleInterface], function(constructor, prototype) {\n" +
                "        prototype.doNothing = function() {};\n" +
                "    }, {}, {}, \"Fields28_array.Fields28_array$1\"))()];\n" +
                "    this._anIntThreeDimensArrayInitialized = [[[0]]];");
    }

    @Test
    public void testArrayLoop() {
        assertCodeContains(Fields29_array_loop.class, "" +
                "        this._anIntArray = [0, 2, 4, 6, 8, 10];\n" +
                "        for (var i = 0; i < this._anIntArray.length; i++) {\n" +
                "            this._anIntArray[i] = i;\n" +
                "        }");

        assertCodeContains(Fields29_array_loop.class, "" +
                "        var sum = 0;\n" +
                "        for (var index$element = 0; index$element < this._anIntArray.length; index$element++) {\n" +
                "            var element = this._anIntArray[index$element];\n" +
                "            sum += element;\n" +
                "        }");
    }

    @Test
    public void testTwoDimensArrayLoop() {
        assertCodeContains(Fields30_two_dimens_array_loop.class, "" +
                "        this._anIntTwoDimensArray = [[0, 2], [4, 6], [8, 10]];\n" +
                "        for (var i = 0; i < this._anIntTwoDimensArray.length; i++) {\n" +
                "            this._anIntTwoDimensArray[i][0] = i;\n" +
                "        }");
        assertCodeContains(Fields30_two_dimens_array_loop.class, "" +
                "        var sum = 0;\n" +
                "        for (var index$element = 0; index$element < this._anIntTwoDimensArray.length; index$element++) {\n" +
                "            var element = this._anIntTwoDimensArray[index$element];\n" +
                "            sum += element[0];\n" +
                "        }");
    }


    @Test
    public void testProtectedMethodInParent() {
        assertCodeContains(Fields31_protected_parent.class, "return this._parent._getProtectedField();");
    }

    @Test
    public void testCreatedFixedArraySize() {
        assertCodeContains(Fields31_create_fixed_array_size.class, "" +
                "    this._aStringArray = stjs.createJavaArray(10);\n" +
                "    this._aStringArray = stjs.createJavaArray(this.constructor._getSize2());\n" +
                "    this._aDoubleStringArray = stjs.createJavaArray(200, 300);\n" +
                "    this._aDoubleStringArray = stjs.createJavaArray(this.constructor._getSize2(), 300);\n" +
                "    this._aDoubleStringArray = stjs.createJavaArray(this.constructor._getSize2(), this.constructor._getSize3());\n");
    }

    @Test
    public void testInitializeFixedArray2Dimensions() {
        String executeResult = (String) execute(Fields32_array2Dimensions.class);

        Assert.assertEquals("[null,null,null][null,null,null]", executeResult);
    }


    @Test
    public void testInitializeObjectField() {
        assertCodeContains(Fields33_object_field_initializer.class, "{\n" +
                "    this._privateObject = new Object();\n" +
                "    this.publicObject = new Object();\n" +
                "    this._defaultedValue = Fields33_object_field_initializer._DEFAULT_VALUE;\n" +
                "};");
        assertCodeContains(Fields33_object_field_initializer.class, "{\n" +
                "    constructor._DEFAULT_VALUE = 3;\n" +
                "    prototype._privateObject = null;\n" +
                "    prototype.publicObject = null;\n" +
                "    prototype._defaultedValue = 0;\n" +
                "    prototype.method = function() {};\n" +
                "}");
    }

}
