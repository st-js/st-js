package org.stjs.generator.plugin.java8.writer.methodref;

import static org.junit.Assert.assertEquals;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.execute;

import org.junit.Test;

public class MethodReferenceGeneratorTest {
	@Test
	public void testStaticMethodRef() {
		assertCodeContains(MethodRef1.class, "calculate(MethodRef1.inc)");
		assertEquals(Integer.valueOf(1), execute(MethodRef1.class));
	}

	@Test
	public void testInstanceMethodRef() {
		assertCodeContains(MethodRef2.class,
				"calculate(function(){return MethodRef2.prototype.inc2.call(arguments[0], arguments[1]);}, new MethodRef2(), 1)");
		assertEquals(Integer.valueOf(3), execute(MethodRef2.class));
	}

	@Test
	public void testInstanceWithTargetMethodRef() {
		assertCodeContains(MethodRef3.class, "calculate(function(){return ref.inc2(arguments[0]);}, 1)");
		assertEquals(Integer.valueOf(4), execute(MethodRef3.class));
	}

	@Test
	public void testNewMethodRef() {
		assertCodeContains(MethodRef4.class, "calculate(function(){return new MethodRef4(arguments[0]);}, 1)");
		assertEquals(Integer.valueOf(1), execute(MethodRef4.class));
	}
}
