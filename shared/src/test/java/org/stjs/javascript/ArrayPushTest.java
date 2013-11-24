package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.stjs.javascript.JSCollections.$array;

import org.junit.Test;

// ===========================================
// Tests section 15.4.4.7 of the ECMA-262 Spec
// ===========================================
// The arguments are appended to the end of the array, in
// the order in which they appear. The new length of the array is returned
// as the result of the call

public class ArrayPushTest {

	@Test
	public void testPush01() {
		Array<Integer> x = $array();
		int push = x.push(1);

		assertEquals(1, push);
		assertEquals(1, x.$get(0).intValue());

		push = x.push();

		assertEquals(1, push);
		assertEquals(null, x.$get(1));

		push = x.push(-1);

		assertEquals(2, push);
		assertEquals(-1, x.$get(1).intValue());
		assertEquals(2, x.$length());
	}

	@Test
	public void testPush02() {
		Array<Object> x = $array();

		x.$set(0, 0);
		int push = x.push(true, Double.POSITIVE_INFINITY, "NaN", "1", -1);

		assertEquals(6, push);
		assertEquals(6, x.$length());
		assertEquals(0, x.$get(0));
		assertSame(Boolean.TRUE, x.$get(1));
		assertEquals(Double.POSITIVE_INFINITY, x.$get(2));
		assertEquals("NaN", x.$get(3));
		assertEquals("1", x.$get(4));
		assertEquals(-1, x.$get(5));
	}
}
