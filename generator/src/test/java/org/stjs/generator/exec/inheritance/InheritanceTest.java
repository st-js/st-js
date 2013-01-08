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

	/** 
	 * Verifies that .js files generated with ST-JS 1.2 (and therefore using the 1.2 version 
	 * of stjs.extends) will still run correctly when executed with the current version. 
	 */
	@Test
	public void testExtendCompatibilityWith12() {
		assertEquals("4 2 WXYZ undefined", GeneratorTestHelper.execute("src/test/resources/javascript/Inheritance-generated-with-1.2.js"));
	}
}
