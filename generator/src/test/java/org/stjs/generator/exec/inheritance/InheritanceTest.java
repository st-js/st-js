package org.stjs.generator.exec.inheritance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class InheritanceTest extends AbstractStjsTest {

	@Test
	public void testReenteringSuper() {
		assertEquals(4.0, executeAndReturnNumber(Inheritance1.class), 0);
	}

	@Test
	public void testCopyPrototype() {
		assertEquals(2.0, executeAndReturnNumber(Inheritance2.class), 0);
	}

	@Test
	public void testInstanceofSuperClass() {
		assertEquals(1.0, executeAndReturnNumber(Inheritance3.class), 0);
	}

	@Test
	public void testInstanceofInterface() {
		assertEquals(1.0, executeAndReturnNumber(Inheritance4.class), 0);
	}

	@Test
	public void testInstanceofSuperInterface() {
		assertEquals(1.0, executeAndReturnNumber(Inheritance5.class), 0);
	}
}
