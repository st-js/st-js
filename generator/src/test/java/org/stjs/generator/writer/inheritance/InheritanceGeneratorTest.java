package org.stjs.generator.writer.inheritance;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class InheritanceGeneratorTest extends AbstractStjsTest {

	@Test
	public void testImplements() {
		assertCodeContains(Inheritance1.class, "class Inheritance1 implements MyInterface {");
	}

	@Test
	public void testExtends() {
		assertCodeContains(Inheritance2.class, "class Inheritance2 extends MySuperClass {");
	}

	@Test
	public void testAccessProtectedField() {
		// the this. prefix should be added for fields from the super class too
		assertCodeContains(Inheritance3.class, "return this.field;");
	}

	@Test
	public void testExtendsMore() {
		assertCodeContains(Inheritance4.class, "interface Inheritance4 extends MyInterface, MyInterface2 {");
	}

	@Test
	public void testAbstractAndGeneric() {
		assertCodeContains(Inheritance5.class, "abstract class Inheritance5 implements MyInterface3 {");
	}

	@Test
	public void testImplementsSyntheticType() {
		assertCodeContains(Inheritance6.class, "class Inheritance6 {");
	}

	@Test
	public void testExtendsSyntheticType() {
		assertCodeContains(Inheritance9.class, "class Inheritance9 {");
	}

	@Test
	public void testExtendsEmptyContructor() {
		// check that the super constructor is called for empty constructor in the child class
		assertCodeContains(Inheritance7.class, "super();");
	}

	@Test
	public void testExtendsInnerClass() {
		assertCodeContains(Inheritance8.class, "class Inheritance8 extends MyClass1_MyInnerClass {");
	}

	@Test
	public void testInterface() {
		assertCodeContains(MyInterface.class, "interface MyInterface {}");
	}

	@Test
	public void testInterfaceMembers() {
		assertCodeContains(MyInterface5.class, "interface MyInterface5 { someMethod(): void; someMethodWithParams(number, someArray): string; test: string; }");
	}
}
