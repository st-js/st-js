package org.stjs.generator.writer.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class EnumGeneratorTest extends AbstractStjsTest {
	@Test
	public void testSimpleEnumDeclaration() {
		assertCodeContains(Enums1.class, "enum Enums1 {a, b, c}");
	}

	@Test
	public void testEnumReference() {
		assertCodeContains(Enums2.class, "Enums2.Value.a");
		assertCodeContains(Enums2.class, "constructor.Value = Enums2_Value;");
		assertCodeContains(Enums2.class, "enum Enums2_Value {a, b, c}");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testEnumWithFieldsDeclaration() {
		// fields are not supported
		generate(Enums9.class);
	}

	@Test
	public void testEnumOrdinal() {
		assertCodeContains(Enums5.class, "Enums5.Value.a.ordinal()");
		assertCodeContains(Enums5.class, "constructor.Value = Enums5_Value;");
		assertCodeContains(Enums5.class, "enum Enums5_Value {a, b, c}");
	}

	@Test
	public void testEnumValues() {
		assertEquals("a:0b:1c:2", execute(Enums6.class));
	}

	@Test
	public void testEnumDeclarationInInterface() {
		assertCodeContains(Enums8.class, "constructor.MyEnum = Enums8_MyEnum;");
		assertCodeContains(Enums8.class, "enum Enums8_MyEnum {a, b, c}");
	}

	@Test
	public void testEnumsNamespace() {
		assertCodeContains(EnumsNamespace.class, "stjs.ns(\"my.enums\");");
		assertCodeContains(EnumsNamespace.class, "my.enums.EnumsNamespace = my_enums_EnumsNamespace");
		assertCodeContains(EnumsNamespace.class, "enum my_enums_EnumsNamespace {A, B, C}");
	}
}
