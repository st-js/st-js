package org.stjs.generator.exec.statements;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class StatementTest extends AbstractStjsTest {

	@Test
	public void testTypeOf() {
		assertEquals(0.0, executeAndReturnNumber(Statements1.class), 0);
	}

}
