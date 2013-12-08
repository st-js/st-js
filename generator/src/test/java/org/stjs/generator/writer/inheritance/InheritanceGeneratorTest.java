package org.stjs.generator.writer.inheritance;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

public class InheritanceGeneratorTest {

	@Test
	public void testImplements() {
		assertCodeContains(Inheritance1.class, "stjs.extend(Inheritance1, null, [MyInterface],");
	}

	@Test
	public void testExtends() {
		assertCodeContains(Inheritance2.class, "stjs.extend(Inheritance2, MySuperClass, [],");
	}

	@Test
	public void testAccessProtectedField() {
		// the this. prefix should be added for fields from the super class too
		assertCodeContains(Inheritance3.class, "return this.field;");
	}

	@Test
	public void testExtendsMore() {
		assertCodeContains(Inheritance4.class, "stjs.extend(Inheritance4, null, [MyInterface, MyInterface2],");
	}

	@Test
	public void testAbstractAndGeneric() {
		assertCodeContains(Inheritance5.class, "stjs.extend(Inheritance5, null, [MyInterface3],");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testImplementsSyntheticType() {
		generate(Inheritance6.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testExtendsSyntheticType() {
		generate(Inheritance9.class);
	}

	@Test
	public void testExtendsEmptyContructor() {
		// check that the super constructor is called for empty constructor in the child class
		assertCodeContains(Inheritance7.class, "MySuperClass.call(this);");
	}

	@Test
	public void testExtendsInnerClass() {
		assertCodeContains(Inheritance8.class, "stjs.extend(Inheritance8, MyClass1.MyInnerClass, [],");
	}
}
