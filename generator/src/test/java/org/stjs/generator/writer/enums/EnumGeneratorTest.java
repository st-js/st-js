package org.stjs.generator.writer.enums;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class EnumGeneratorTest extends AbstractStjsTest {
	@Test
	public void testSimpleEnumDeclaration() {
		assertCodeContains(Enums1.class, "Enums1 = stjs.enumeration(\"a\", \"b\", \"c\");");
	}

	@Test
	public void testEnumReference() {
		assertCodeContains(Enums2.class, "Enums2.Value.a");
		assertCodeContains(Enums2.class, "constructor.Value = stjs.enumeration(\"a\", \"b\", \"c\");");
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
		assertCodeContains(Enums5.class, "constructor.Value = stjs.enumeration(\"a\", \"b\", \"c\");");
	}

	@Test
	public void testEnumValues() {
		assertCodeContains(Enums6.class, "" +
				"        for (var index$v in Enums6.Value.values()) {\n" +
				"            var v = Enums6.Value.values()[index$v];\n" +
				"        }\n");
		assertCodeContains(Enums6.class, "constructor.Value = stjs.enumeration(\"a\", \"b\", \"c\");");
	}

	@Test
	public void testEnumDeclarationInInterface() {
		assertCodeContains(Enums8.class, "constructor.MyEnum = stjs.enumeration(\"a\", \"b\", \"c\");");
	}

	@Test
	public void testEnumsNamespace() {
		assertCodeContains(EnumsNamespace.class, //
				"stjs.ns(\"my.enums\"); " + //
						"my.enums.EnumsNamespace = stjs.enumeration");
	}
}
