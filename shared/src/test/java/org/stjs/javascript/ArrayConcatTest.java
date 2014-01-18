package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSGlobal.Array;

import org.junit.Test;

//===================================================
// Tests for the section 15.4.4.4 of the ECMA-262 spec
// ===================================================
// When the concat method is called with zero or more arguments item1, item2,
// etc., it returns an array containing the array elements of the object followed by
// the array elements of each argument in order

public class ArrayConcatTest {

	@Test
	public void testConcat01() {
		Array<Integer> x = Array();
		Array<Integer> y = Array(0, 1);
		Array<Integer> z = Array(2, 3, 4);

		@SuppressWarnings("unchecked")
		Array<Integer> arr = x.concat(y, z);

		assertNotSame(x, arr);
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(1, arr.$get(1).intValue());
		assertEquals(2, arr.$get(2).intValue());
		assertEquals(3, arr.$get(3).intValue());
		assertEquals(4, arr.$get(4).intValue());
		assertEquals(5, arr.$length());
	}

	@Test
	public void testConcat02() {
		Array<Object> x = $array((Object) 0);
		Object y = new Object();

		Array<Object> arr = x.concat(y, -1, true, "NaN");

		assertNotSame(x, arr);
		assertEquals(Integer.valueOf(0), arr.$get(0));
		assertSame(y, arr.$get(1));
		assertEquals(Integer.valueOf(-1), arr.$get(2));
		assertEquals(Boolean.TRUE, arr.$get(3));
		assertEquals("NaN", arr.$get(4));
		assertEquals(5, arr.$length());
	}

	@Test
	public void testConcat03() {
		Array<Integer> x = $array();
		x.$set(1, 1); // x[0] remains unset
		Array<Integer> y = new Array<Integer>(2);
		// y[0] and y[1] remain unset
		Array<Integer> z = $array();

		@SuppressWarnings("unchecked")
		Array<Integer> arr = x.concat(z, y);

		// arr should be [,1,,]
		assertEquals(null, arr.$get(0));
		assertEquals(1, arr.$get(1).intValue());
		assertEquals(null, arr.$get(2));
		assertEquals(null, arr.$get(3));
		assertEquals(4, arr.$length());
	}
}
