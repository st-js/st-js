package org.stjs.generator.plugin.java8.writer.interfaces;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class InterfaceGeneratorTest extends AbstractStjsTest {
	@Test
	public void testStaticMethod() {
		assertCodeContains(Interface1.class, "constructor.staticMethod=function(){return 1;}");
	}

	@Test
	public void testDefaultMethod() {
		assertEquals(1, executeAndReturnNumber(Class2.class), 0);
	}
}
