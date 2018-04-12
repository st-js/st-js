package org.stjs.generator.exec.statements;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stjs.generator.MultipleFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

public class StatementTest extends AbstractStjsTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void testTypeOf() {
		assertEquals(0.0, executeAndReturnNumber(Statements1.class), 0);
	}

	@Test
	public void testGetClass() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("You can't use the 'getClass' method.");

		assertEquals(0.0, executeAndReturnNumber(Statements2.class), 0);
	}

	@Test
	public void testCountElements() {
		assertEquals(2.0, executeAndReturnNumber(Statements3.class), 0);
	}
}
