package org.stjs.generator;

import static org.stjs.generator.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.GeneratorTestHelper.assertCodeDoesNotContain;

import org.junit.Test;

import test.generator.inheritance.Inheritance1;
import test.generator.inheritance.Inheritance2;

public class InheritanceGeneratorTest {

	@Test
	public void testImplements() {
		assertCodeDoesNotContain(Inheritance1.class, "stjs.extend(Inheritance1, MyInterface);");
	}

	@Test
	public void testExtends() {
		assertCodeContains(Inheritance2.class, "stjs.extend(Inheritance2, MySuperClass);");
	}

}
