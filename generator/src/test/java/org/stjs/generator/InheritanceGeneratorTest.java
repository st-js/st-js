package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeDoesNotContain;

import org.junit.Test;

import test.generator.inheritance.Inheritance1;
import test.generator.inheritance.Inheritance2;
import test.generator.inheritance.Inheritance3;

public class InheritanceGeneratorTest {

	@Test
	public void testImplements() {
		assertCodeDoesNotContain(Inheritance1.class, "stjs.extend(Inheritance1, MyInterface);");
	}

	@Test
	public void testExtends() {
		assertCodeContains(Inheritance2.class, "stjs.extend(Inheritance2, MySuperClass);");
	}

	@Test
	public void testAccessProtectedField() {
		// the this. prefix should be added for fields from the super class too
		assertCodeContains(Inheritance3.class, "return this.field;");
	}
}
