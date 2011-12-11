package org.stjs.generator.exec.inheritance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.GeneratorTestHelper;

public class InheritanceTest {

	@Test
	public void testReenteringSuper() {
		assertEquals(4.0, GeneratorTestHelper.execute(Inheritance1.class));
	}

	@Test
	public void testCopyPrototype() {
		assertEquals(2.0, GeneratorTestHelper.execute(Inheritance2.class));
	}

}
