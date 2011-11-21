package org.stjs.generator.writer.enums;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;

public class EnumGeneratorTest {
	@Test
	public void testSimpleEnumDeclaration() {
		assertCodeContains(Enums1.class, "Enums1 = stjs.enumeration(\"a\", \"b\", \"c\");");
	}

	@Test
	public void testEnumReference() {
		assertCodeContains(Enums2.class, "Enums2.Value.a");
	}

	@Test(expected = JavascriptGenerationException.class)
	public void testEnumWithFieldsDeclaration() {
		// fields are not supported
		generate(Enums4.class);
	}

	@Test
	public void testEnumOrdinal() {
		assertCodeContains(Enums5.class, "Enums5.Value.a.ordinal()");
	}

	@Test
	public void testEnumValues() {
		assertCodeContains(Enums6.class, "for(var v in Enums6.Value.values())");
	}

	@Test
	public void testEnumDeclarationInInterface() {
		assertCodeContains(Enums8.class, "Enums8.MyEnum = stjs.enumeration(\"a\", \"b\", \"c\");");
	}
}
