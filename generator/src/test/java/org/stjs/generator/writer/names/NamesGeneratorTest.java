package org.stjs.generator.writer.names;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class NamesGeneratorTest extends AbstractStjsTest {
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
		// the special parameter THIS is no longer changed
		assertCodeContains(Names5.class, "return THIS._field");
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

	@Test
	public void testStaticAccessFromInstanceVar() {
		assertCodeContains(Names11.class, "n.constructor.staticMethod()");
	}

	@Test
	public void testStaticAccessFromInstanceNew() {
		assertCodeContains(Names12.class, "new Names12().constructor.staticMethod()");
	}

	@Test
	public void testStaticAccessFromInstanceMethodReturn() {
		assertCodeContains(Names13.class, "instance().constructor.staticMethod()");
	}

	@Test
	public void testStaticFieldAccessFromInstanceMethodReturn() {
		assertCodeContains(Names14.class, "instance().constructor.staticField");
	}
}
