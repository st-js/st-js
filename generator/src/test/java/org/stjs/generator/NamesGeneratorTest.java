package org.stjs.generator;

import static org.stjs.generator.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.names.Names1;
import test.generator.names.Names10;
import test.generator.names.Names2;
import test.generator.names.Names3;
import test.generator.names.Names4;
import test.generator.names.Names5;
import test.generator.names.Names6;
import test.generator.names.Names7;
import test.generator.names.Names8;
import test.generator.names.Names9;

public class NamesGeneratorTest {
	@Test
	public void testQualifyStaticField() {
		assertCodeContains(Names1.class, "Names1.MY_CONSTANT");
	}

	@Test
	public void testQualifyStaticMethod() {
		assertCodeContains(Names2.class, "Names2.staticMethod()");
	}

	@Test
	public void shouldDelegateToDefaultBehaviorIfQNameIsNotFound() {
		assertCodeContains(Names3.class, "Names3.field.method()");
	}

	@Test
	public void testCallGenericMethod() {
		assertCodeContains(Names4.class, "Names4.genericMethod(2)");
	}

	@Test
	public void testSpecialThis() {
		// the special parameter THIS should be transformed in this
		assertCodeContains(Names5.class, "return this.field");
	}

	@Test
	public void testScopeTwoMethods() {
		assertCodeContains(Names6.class, "Names6.field.get().method()");
	}

	@Test
	public void testStaticMethodWithClass() {
		assertCodeContains(Names7.class, "Names7.staticMethod()");
	}

	@Test
	public void testStaticMethodWithClassAndPackage() {
		assertCodeContains(Names8.class, "Names8.staticMethod()");
	}

	@Test
	public void testStaticMethodInnerClass() {
		assertCodeContains(Names9.class, "Names9.Inner.staticMethod()");
	}

	@Test
	public void testThatInInner() {
		assertCodeContains(Names10.class, "that.method()");
	}
}
