package org.stjs.generator.plugin.java8.writer.interfaces;

import static org.junit.Assert.assertEquals;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.execute;

import org.junit.Test;

public class InterfaceGeneratorTest {
	@Test
	public void testStaticMethod() {
		assertCodeContains(Interface1.class, "constructor.staticMethod=function(){return 1;}");
	}

	@Test
	public void testDefaultMethod() {
		assertEquals(1, execute(Class2.class));
	}
}
