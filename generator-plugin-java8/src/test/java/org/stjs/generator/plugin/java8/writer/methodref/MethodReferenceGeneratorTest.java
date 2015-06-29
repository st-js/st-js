package org.stjs.generator.plugin.java8.writer.methodref;

import org.stjs.generator.utils.AbstractStjsTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MethodReferenceGeneratorTest extends AbstractStjsTest {
	@Test
	public void testStaticMethodRef() {
		assertCodeContains(MethodRef1.class, "calculate(MethodRef1.inc)");
		assertEquals(1, ((Number)execute(MethodRef1.class)).intValue());
	}

	@Test
	public void testInstanceMethodRef() {
		assertCodeContains(MethodRef2.class,
				"calculate(function(){return MethodRef2.prototype.inc2.call(arguments[0], arguments[1]);}, new MethodRef2(), 1)");
		assertEquals(3, ((Number)execute(MethodRef2.class)).intValue());
	}

	@Test
	public void testInstanceWithTargetMethodRef() {
		assertCodeContains(MethodRef3.class, "calculate(stjs.bind(ref, \"inc2\"), 1)");
		assertEquals(4, ((Number)execute(MethodRef3.class)).intValue());
	}

	@Test
	public void testNewMethodRef() {
		assertCodeContains(MethodRef4.class, "calculate(function(){return new MethodRef4(arguments[0]);}, 1)");
		assertEquals(1, ((Number)execute(MethodRef4.class)).intValue());
	}

	@Test
	public void testUsageOfThisMethodRef() {
		assertCodeContains(MethodRef5.class, "calculate(stjs.bind(this, \"method\"))");
	}

	@Test
	public void testUsageOFieldMethodRef() {
		assertCodeContains(MethodRef6.class, "calculate(stjs.bind(this.field, \"method\"))");
	}

	@Test
	public void testUsageOMethodMethodRef() {
		assertCodeContains(MethodRef7.class, "calculate(stjs.bind(this.method2(), \"method\"))");
	}

	@Test
	public void testUsageOfChainMethodMethodRef() {
		assertCodeContains(MethodRef8.class, "calculate(stjs.bind(this.x.x.method2(), \"method\"))");
	}
}
