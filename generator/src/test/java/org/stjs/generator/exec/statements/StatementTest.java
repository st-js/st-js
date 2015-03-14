package org.stjs.generator.exec.statements;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.GeneratorTestHelper;

public class StatementTest {

	@Test
	public void testTypeOf() {
		assertEquals(0.0, GeneratorTestHelper.executeAndReturnNumber(Statements1.class), 0);
	}

}
