package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.stjs.javascript.JSCollections.$array;

import org.junit.Test;

// ===========================================
// Tests section 15.4.4.9 of the ECMA-262 Spec
// ===========================================
// The first element of the array is removed from the array and
// returned
// If length equal zero, call the [[Put]] method of this object
// with arguments "length" and 0 and return undefined

public class ArrayShiftTest {

	@Test
	public void testShift01() {
		Array<Object> x = $array();
		Object shift = x.shift();

		assertNull(shift);
		assertEquals(0, x.$length());
	}

	@Test
	public void testShift02() {
		Array<Integer> x = $array(1, 2, 3);
		x.$length(0);
		Integer shift = x.shift();

		assertNull(shift);
		assertEquals(0, x.$length());
	}

	@Test
	public void testShift03() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Integer shift = x.shift();

		assertEquals(0, shift.intValue());
		assertEquals(3, x.$length());
		assertEquals(1, x.$get(0).intValue());
		assertEquals(2, x.$get(1).intValue());
	}

	@Test
	public void testShift04() {
		Array<Integer> x = $array();
		x.$set(0, 0);
		x.$set(3, 3);
		Integer shift = x.shift();

		assertEquals(0, shift.intValue());
		assertEquals(3, x.$length());
		assertEquals(null, x.$get(0));
		assertEquals(null, x.$get(12));

		x.$length(1);
		shift = x.shift();

		assertNull(shift);
		assertEquals(0, x.$length());
	}
}
