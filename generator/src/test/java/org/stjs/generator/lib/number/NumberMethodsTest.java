package org.stjs.generator.lib.number;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stjs.generator.MultipleFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

public class NumberMethodsTest extends AbstractStjsTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void testParseInt() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("You are trying to call a method that exists only in Java. Called 'Integer.parseInt()'");
		assertEquals(123.0, executeAndReturnNumber(Number1.class), 0);
	}

	@Test
	public void testIntValue() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("You are trying to call a method that exists only in Java. Called 'Double.prototype.intValue()'");
		assertEquals(123.0, executeAndReturnNumber(Number2.class), 0);
	}

	@Test
	public void testValueOf() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("You are trying to call a method that exists only in Java. Called 'Integer.valueOf()'");
		assertEquals(123.0, executeAndReturnNumber(Number3.class), 0);
	}
}
