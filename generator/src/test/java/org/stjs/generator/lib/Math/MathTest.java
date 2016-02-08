package org.stjs.generator.lib.Math;

import org.junit.Test;
import org.stjs.generator.lib.number.Number1;
import org.stjs.generator.lib.number.Number3_NumberValueOf;
import org.stjs.generator.lib.number.Number4_overloaded_methods;
import org.stjs.generator.lib.number.Number5_IntValue;
import org.stjs.generator.lib.number.Number6_toString;
import org.stjs.generator.utils.AbstractStjsTest;

import static org.junit.Assert.assertEquals;

public class MathTest extends AbstractStjsTest {

	@Test
	public void testMin() {
		assertEquals(true, execute(Math1_min.class));
	}

	@Test
	public void testMax() {
		assertEquals(true, execute(Math2_max.class));
	}

	@Test
	public void testAbs() {
		assertEquals(true, execute(Math3_abs.class));
	}

	@Test
	public void testRound() {
		assertEquals(true, execute(Math4_round.class));
	}

}
