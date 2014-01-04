package org.stjs.generator.writer.typeDesc;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

public class TypeDescTest {
	@Test
	public void testBasicField() {
		assertCodeContains(TypeDesc1.class, "{});");
	}

	@Test
	public void testArrayOfBasicField() {
		assertCodeContains(TypeDesc2.class, "{x:{name:\"Array\", arguments:[null]}});");
	}

	@Test
	public void testNonBasicField() {
		assertCodeContains(TypeDesc3.class, "{x:\"Date\"});");
	}

	@Test
	public void testArrayOfNonBasicField() {
		assertCodeContains(TypeDesc4.class, "{x:{name:\"Array\", arguments:[\"Date\"]}});");
	}

	@Test
	public void testMapOfNonBasicField() {
		assertCodeContains(TypeDesc5.class, "{x:{name:\"Map\", arguments:[null,\"Date\"]}});");
	}

	@Test
	public void testEnum() {
		assertCodeContains(TypeDesc6.class, "{x:{name:\"Enum\", arguments:[\"TypeDesc6.Type\"]}});");
	}
}
