package org.stjs.generator.writer.inheritance;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeDoesNotContain;

import org.junit.Test;

public class InheritanceGeneratorTest {

	@Test
	public void testImplements() {
		assertCodeContains(Inheritance1.class, "stjs.extend(Inheritance1, MyInterface);");
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

	@Test
	public void testExtendsMore() {
		assertCodeContains(Inheritance4.class, "stjs.extend(Inheritance4, MyInterface, MyInterface2);");
	}

	@Test
	public void testAbstractAndGeneric() {
		assertCodeContains(Inheritance5.class, "stjs.extend(Inheritance5, MyInterface3);");
	}

	@Test
	public void testImplementsSyntheticType() {
		assertCodeDoesNotContain(Inheritance6.class, "stjs.extend");
	}
}
