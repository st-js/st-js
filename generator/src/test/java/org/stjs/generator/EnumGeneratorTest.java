package org.stjs.generator;

import static org.stjs.generator.GeneratorTestHelper.assertCodeContains;

import org.junit.Ignore;
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
		assertCodeContains(Enums1.class, "Enums1 = stjs.enumeration(\"a\", \"b\", \"c\");");
	}

	@Test
	public void testEnumReference() {
		assertCodeContains(Enums2.class, "Enums2.Value.a");
	}

	@Ignore
	public void testSwitchEnums() {
		// TODO should be able to resolve first the type of the switch expression
		assertCodeContains(Enums3.class, "case Enum3.Value.a");
	}

	@Ignore
	public void testEnumWithFieldsDeclaration() {
		// TODO the enums cannot yet have other members
		assertCodeContains(Enums4.class, "xxx");
	}

	@Test
	public void testEnumOrdinal() {
		assertCodeContains(Enums5.class, "Enums5.Value.a.ordinal()");
	}

	@Test
	public void testEnumValues() {
		assertCodeContains(Enums6.class, "for(var v in Enums6.Value.values())");
	}
}
