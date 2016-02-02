package org.stjs.generator.writer.enums;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class EnumsGeneratorTest extends AbstractStjsTest {
	@Test
	public void testSimpleEnumDeclaration() {
		assertCodeContains(SimpleEnum.class, "" +
				"    constructor.FIRST = new SimpleEnum()._constructor();\n" +
				"    constructor.FIRST._name = \"FIRST\";\n" +
				"    constructor.FIRST._ordinal = 0;\n" +
				"    constructor.SECOND = new SimpleEnum()._constructor();\n" +
				"    constructor.SECOND._name = \"SECOND\";\n" +
				"    constructor.SECOND._ordinal = 1;\n" +
				"    constructor.THIRD = new SimpleEnum()._constructor();\n" +
				"    constructor.THIRD._name = \"THIRD\";\n" +
				"    constructor.THIRD._ordinal = 2;\n" +
				"    constructor._values = [constructor.FIRST, constructor.SECOND, constructor.THIRD];");
	}

	@Test
	public void testEnumReference() {
		assertCodeContains(Enums2_reference.class, "" +
				"    constructor.Value = function () {};\n" +
				"    constructor.Value = stjs.extend(constructor.Value, stjs.Java.Enum, [], function(constructor, prototype) {\n" +
				"        prototype._constructor = function() {\n" +
				"            stjs.Java.Enum.prototype._constructor$String_int.call(this);\n" +
				"            return this;\n" +
				"        };");
		assertCodeContains(Enums2_reference.class, "" +
				"    prototype.main = function() {\n" +
				"        var x = Enums2_reference.Value.FIRST;\n" +
				"    };");
	}

	@Test
	public void testEnumOrdinal() {
		assertCodeContains(Enums5_ordinal.class, "SimpleEnum.FIRST.ordinal()");
		Assert.assertEquals(0, ((Number) execute(Enums5_ordinal.class)).intValue());
	}

	@Test
	public void testEnumName() {
		assertCodeContains(Enums6_name.class, "SimpleEnum.FIRST.name()");
		Assert.assertEquals("FIRST", execute(Enums6_name.class));
	}

	@Test
	public void testEnumDeclarationInInterface() {
		assertCodeContains(Enums8_interface_declaration.class, "" +
				"    constructor.InnerClass = stjs.extend(constructor.InnerClass, stjs.Java.Object, [Enums8_interface_declaration], function(constructor, prototype) {");

		assertCodeContains(Enums8_interface_declaration.class, "" +
				"        prototype.methodReturningEnum = function() {\n" +
				"            return SimpleEnum.FIRST;\n" +
				"        };\n");
	}

	@Test
	public void testEnumWithFieldsDeclaration() {
		assertCodeContains(Enums9_fields.class, "" +
				"    constructor.THIRD = new Enums9_fields()._constructor$int(3);\n" +
				"    constructor.THIRD._name = \"THIRD\";\n" +
				"    constructor.THIRD._ordinal = 2;\n" +
				"    constructor._values = [constructor.FIRST, constructor.SECOND, constructor.THIRD];\n" +
				"    prototype._value = 0;\n" +
				"    prototype.getValue = function() {\n" +
				"        return this._value;\n" +
				"    };\n");
	}

	@Test
	public void testEnumsNamespace() {
		assertCodeContains(Enums10_annotation_namespace.class, "" +
				"my.enums.Enums10_annotation_namespace = stjs.extend(my.enums.Enums10_annotation_namespace, stjs.Java.Enum, [], function(constructor, prototype)");
	}

	@Test
	public void testEnumConstructor() {
		assertCodeContains(Enums11_with_constructor.class, "" +
				"    prototype._constructor$int = function(initialValue) {\n" +
				"        stjs.Java.Enum.prototype._constructor$String_int.call(this);\n" +
				"        this._privateFieldValue = initialValue;\n" +
				"        return this;\n" +
				"    };");
		assertCodeContains(Enums11_with_constructor.class, "" +
				"    constructor.FIRST = new Enums11_with_constructor()._constructor$int(1);\n" +
				"    constructor.FIRST._name = \"FIRST\";\n" +
				"    constructor.FIRST._ordinal = 0;\n" +
				"    constructor.SECOND = new Enums11_with_constructor()._constructor$int(2);\n" +
				"    constructor.SECOND._name = \"SECOND\";\n" +
				"    constructor.SECOND._ordinal = 1;\n" +
				"    constructor.THIRD = new Enums11_with_constructor()._constructor$int(3);\n" +
				"    constructor.THIRD._name = \"THIRD\";\n" +
				"    constructor.THIRD._ordinal = 2;\n" +
				"    constructor._values = [constructor.FIRST, constructor.SECOND, constructor.THIRD];\n" +
				"    prototype._privateFieldValue = 0;");
	}

	@Test
	public void testEnumValues() {
		assertCodeContains(Enums12_for_values.class, "" +
				"        for (var index$v = 0; index$v < SimpleEnum.values().length; index$v++) {\n" +
				"            var v = SimpleEnum.values()[index$v];\n" +
				"        }\n");
	}

	@Test
	public void testEnumImplementInterface() {
		assertCodeContains(Enums13_implement_interface.class, "" +
				"var Enums13_implement_interface = function () {};\n" +
				"Enums13_implement_interface = stjs.extend(Enums13_implement_interface, stjs.Java.Enum, [KeyType], ");
	}

	@Test
	public void testEnumValuesShouldBeAvailableRightAfterDeclarationOfEntries() throws Exception {
		assertCodeContains(Enums14_inner_static_filed_using_enum_values.class, "" +
                "        constructor.THIRD._ordinal = 2;\n" +
                "        constructor._values = [constructor.FIRST, constructor.SECOND, constructor.THIRD];\n" +
                "        constructor.aValueFromMyEnum = Enums14_inner_static_filed_using_enum_values.MyEnum.values()[1];"
        );

		String result = (String) execute(Enums14_inner_static_filed_using_enum_values.class);
		Assert.assertEquals("SECOND", result);
	}

	@Test
	public void testCanGetEnumConstantsFromClass() throws Exception {
		String result = (String) execute(Enums14_getEnumConstants.class);
		Assert.assertEquals("FIRST,SECOND,THIRD,", result);
	}
}
