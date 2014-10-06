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

	@Test
	public void testPush03() {
		// tests push on a sparse array store
		Array<Integer> x = $array();
		x.$length(10000);

		int push = x.push(0, 1, 2, 3, 4);

		assertEquals(10005, push);
		assertEquals(10005, x.$length());
		assertEquals(0, x.$get(10000).intValue());
		assertEquals(1, x.$get(10001).intValue());
		assertEquals(2, x.$get(10002).intValue());
		assertEquals(3, x.$get(10003).intValue());
		assertEquals(4, x.$get(10004).intValue());
	}

	@Test
	public void testPush04() {
		// tests push on a PackedArrayStore, with holes in the array
		Array<Integer> x = $array();
		x.$length(10);

		int push = x.push(0, 1, 2, 3, 4);

		assertEquals(15, push);
		assertEquals(15, x.$length());
		assertEquals(0, x.$get(10).intValue());
		assertEquals(1, x.$get(11).intValue());
		assertEquals(2, x.$get(12).intValue());
		assertEquals(3, x.$get(13).intValue());
		assertEquals(4, x.$get(14).intValue());
	}
}
