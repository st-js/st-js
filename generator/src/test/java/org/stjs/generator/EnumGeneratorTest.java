package org.stjs.generator;

import static org.stjs.generator.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.enums.Enums1;
import test.generator.enums.Enums2;
import test.generator.enums.Enums3;
import test.generator.enums.Enums4;
import test.generator.enums.Enums5;
import test.generator.enums.Enums6;

public class EnumGeneratorTest {
	@Test
	public void testSimpleEnumDeclaration() {
		assertCodeContains(Enums1.class, "Enums1 = {a:\"a\", b:\"b\", c:\"c\"}");
	}

	@Test
	public void testEnumReference() {
		assertCodeContains(Enums2.class, "Enums2.a");
	}

	@Test
	public void testSwitchEnums() {
		assertCodeContains(Enums3.class, "Enums1 = {a:\"a\", b:\"b\", c:\"c\"}");
	}

	@Test
	public void testEnumWithFieldsDeclaration() {
		assertCodeContains(Enums4.class, "xxx");
	}

	@Test
	public void testEnumOrdinal() {
		assertCodeContains(Enums5.class, "xxx");
	}

	@Test
	public void testEnumValues() {
		assertCodeContains(Enums6.class, "xxx");
	}
}
