package org.stjs.generator.writer.enums;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class EnumsGeneratorTest extends AbstractStjsTest {
	@Test
	public void testSimpleEnumDeclaration() {
		assertCodeContains(SimpleEnum.class, "" +
				"    constructor.FIRST = new SimpleEnum();\n" +
				"    constructor.FIRST._name = \"FIRST\";\n" +
				"    constructor.FIRST._ordinal = 0;\n" +
				"    constructor.SECOND = new SimpleEnum();\n" +
				"    constructor.SECOND._name = \"SECOND\";\n" +
				"    constructor.SECOND._ordinal = 1;\n" +
				"    constructor.THIRD = new SimpleEnum();\n" +
				"    constructor.THIRD._name = \"THIRD\";\n" +
				"    constructor.THIRD._ordinal = 2;\n" +
				"    constructor._values = [constructor.FIRST, constructor.SECOND, constructor.THIRD];");
	}

	@Test
	public void testEnumReference() {
		assertCodeContains(Enums2_reference.class, "" +
				"   constructor.Value = function() {\n" +
				"        JavaEnum.call(this);\n" +
				"    };");
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
				"    constructor.InnerClass = stjs.extend(constructor.InnerClass, null, [Enums8_interface_declaration], function(constructor, prototype) {\n" +
				"        prototype.methodReturningEnum = function() {\n" +
				"            return SimpleEnum.FIRST;\n" +
				"        };\n" +
				"    }, {}, {});");
	}

	@Test
	public void testEnumWithFieldsDeclaration() {
		assertCodeContains(Enums9_fields.class, "" +
				"    constructor.THIRD = new Enums9_fields(3);\n" +
				"    constructor.THIRD._name = \"THIRD\";\n" +
				"    constructor.THIRD._ordinal = 2;\n" +
				"    prototype._value = 0;\n" +
				"    prototype.getValue = function() {\n" +
				"        return this._value;\n" +
				"    };");
	}

	@Test
	public void testEnumsNamespace() {
		assertCodeContains(Enums10_annotation_namespace.class, "" +
				"my.enums.Enums10_annotation_namespace = stjs.extend(my.enums.Enums10_annotation_namespace, JavaEnum, [], function(constructor, prototype)");
	}

	@Test
	public void testEnumConstructor() {
		assertCodeContains(Enums11_with_constructor.class, "" +
				"var Enums11_with_constructor = function(initialValue) {\n" +
				"    JavaEnum.call(this);\n" +
				"    this._privateFieldValue = initialValue;\n" +
				"};");
		assertCodeContains(Enums11_with_constructor.class, "" +
				"    constructor.FIRST = new Enums11_with_constructor(1);\n" +
				"    constructor.FIRST._name = \"FIRST\";\n" +
				"    constructor.FIRST._ordinal = 0;\n" +
				"    constructor.SECOND = new Enums11_with_constructor(2);\n" +
				"    constructor.SECOND._name = \"SECOND\";\n" +
				"    constructor.SECOND._ordinal = 1;\n" +
				"    constructor.THIRD = new Enums11_with_constructor(3);\n" +
				"    constructor.THIRD._name = \"THIRD\";\n" +
				"    constructor.THIRD._ordinal = 2;\n" +
				"    prototype._privateFieldValue = 0;");
	}

	@Test
	public void testEnumValues() {
		assertCodeContains(Enums12_for_values.class, "" +
				"        for (var index$v in SimpleEnum.values()) {\n" +
				"            var v = SimpleEnum.values()[index$v];\n" +
				"        }\n");
	}

	@Test
	public void testEnumImplementInterface() {
		assertCodeContains(Enums13_implement_interface.class, "" +
				"var Enums13_implement_interface = function(keyStr) {\n" +
				"    JavaEnum.call(this);\n" +
				"    this._keyStr = keyStr;\n" +
				"};\n" +
				"Enums13_implement_interface = stjs.extend(Enums13_implement_interface, JavaEnum, [KeyType], function(constructor, prototype) {\n" +
				"    constructor.NONE = new Enums13_implement_interface(\"none\");\n" +
				"    constructor.NONE._name = \"NONE\";\n" +
				"    constructor.NONE._ordinal = 0;\n" +
				"    constructor.FIRST = new Enums13_implement_interface(\"first\");\n" +
				"    constructor.FIRST._name = \"FIRST\";\n" +
				"    constructor.FIRST._ordinal = 1;\n" +
				"    constructor.SECOND = new Enums13_implement_interface(\"second\");\n" +
				"    constructor.SECOND._name = \"SECOND\";\n" +
				"    constructor.SECOND._ordinal = 2;\n" +
				"    constructor.THIRD = new Enums13_implement_interface(\"third\");\n" +
				"    constructor.THIRD._name = \"THIRD\";\n" +
				"    constructor.THIRD._ordinal = 3;\n" +
				"    prototype._keyStr = null;\n" +
				"    prototype.getKey = function() {\n" +
				"        return this._keyStr;\n" +
				"    };\n" +
				"    constructor._values = [constructor.NONE, constructor.FIRST, constructor.SECOND, constructor.THIRD];");
	}
}
