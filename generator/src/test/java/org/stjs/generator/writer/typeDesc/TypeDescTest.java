package org.stjs.generator.writer.typeDesc;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

public class TypeDescTest {
	@Test
	public void testBasicField() {
		assertCodeContains(TypeDesc1.class, "TypeDesc1.$typeDescription = {};");
	}

	@Test
	public void testArrayOfBasicField() {
		assertCodeContains(TypeDesc2.class, "TypeDesc2.$typeDescription = {\"x\":{name:\"Array\", arguments:[null]}};");
	}

	@Test
	public void testNonBasicField() {
		assertCodeContains(TypeDesc3.class, "TypeDesc3.$typeDescription = {\"x\":\"Date\"};");
	}

	@Test
	public void testArrayOfNonBasicField() {
		assertCodeContains(TypeDesc4.class,
				"TypeDesc4.$typeDescription = {\"x\":{name:\"Array\", arguments:[\"Date\"]}};");
	}

	@Test
	public void testMapOfNonBasicField() {
		assertCodeContains(TypeDesc5.class,
				"TypeDesc5.$typeDescription = {\"x\":{name:\"Map\", arguments:[null,\"Date\"]}};");
	}

}
