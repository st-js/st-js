package org.stjs.generator.writer.enums;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

public class EnumGeneratorTest {
	@Test
	public void testSimpleEnumDeclaration() {
		assertCodeContains(Enums1.class, "Enums1 = stjs.enumeration(\"a\", \"b\", \"c\");");
	}

	@Test
	public void testEnumReference() {
		assertCodeContains(Enums2.class, "Enums2.Value.a");
		assertCodeContains(Enums2.class, "Enums2.Value = stjs.enumeration(\"a\", \"b\", \"c\");");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testEnumWithFieldsDeclaration() {
		// fields are not supported
		generate(Enums9.class);
	}

	@Test
	public void testEnumOrdinal() {
		assertCodeContains(Enums5.class, "Enums5.Value.a.ordinal()");
		assertCodeContains(Enums5.class, "Enums5.Value = stjs.enumeration(\"a\", \"b\", \"c\");");
	}

	@Test
	public void testEnumValues() {
		assertCodeContains(Enums6.class, "for(var v in Enums6.Value.values())");
		assertCodeContains(Enums6.class, "Enums6.Value = stjs.enumeration(\"a\", \"b\", \"c\");");
	}

	@Test
	public void testEnumDeclarationInInterface() {
		assertCodeContains(Enums8.class, "Enums8.MyEnum = stjs.enumeration(\"a\", \"b\", \"c\");");
	}

	@Test
	public void testEnumsNamespace() {
		assertCodeContains(EnumsNamespace.class, //
				"stjs.ns(\"my.enums\"); " + //
						"my.enums.EnumsNamespace = stjs.enumeration");
	}
}
