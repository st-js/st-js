package org.stjs.javascript;

import static java.lang.Double.POSITIVE_INFINITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.stjs.javascript.JSCollections.$array;

import org.junit.Test;

//===========================================
// Tests section 15.4.4.8 of the ECMA-262 Spec
// ===========================================
// The elements of the array are rearranged so as to reverse their order.
// The object is returned as the result of the call

public class ArrayReverseTest {

	@Test
	public void testReverse01() {
		Array<Integer> x = $array();
		Array<Integer> reverse = x.reverse();

		assertSame(x, reverse);
		assertEquals(0, x.$length());
	}

	@Test
	public void testReverse02() {
		Array<Integer> x = $array();
		x.$set(0, 1);
		Array<Integer> reverse = x.reverse();

		assertSame(x, reverse);
		assertEquals(1, x.$length());
		assertEquals(1, x.$get(0).intValue());
	}

	@Test
	public void testReverse03() {
		Array<Integer> x = $array(1, 2);
		Array<Integer> reverse = x.reverse();

		assertSame(x, reverse);
		assertEquals(2, x.$get(0).intValue());
		assertEquals(1, x.$get(1).intValue());
		assertEquals(2, x.$length());
	}

	@Test
	public void testReverse04() {
		Array<Object> x = $array();
		x.$set(0, true);
		x.$set(2, POSITIVE_INFINITY);
		x.$set(4, null);
		x.$set(5, null);
		x.$set(8, "NaN");
		x.$set(9, "-1");
		Array<Object> reverse = x.reverse();

		assertSame(x, reverse);
		assertEquals("-1", x.$get(0));
		assertEquals("NaN", x.$get(1));
		assertEquals(null, x.$get(2));
		assertEquals(null, x.$get(3));
		assertEquals(null, x.$get(4));
		assertEquals(null, x.$get(5));
		assertEquals(null, x.$get(6));
		assertEquals(POSITIVE_INFINITY, x.$get(7));
		assertEquals(null, x.$get(8));
		assertEquals(true, x.$get(9));

		x.$length(9);
		reverse = x.reverse();

		assertSame(x, reverse);
		assertEquals(null, x.$get(0));
		assertEquals(POSITIVE_INFINITY, x.$get(1));
		assertEquals(null, x.$get(2));
		assertEquals(null, x.$get(3));
		assertEquals(null, x.$get(4));
		assertEquals(null, x.$get(5));
		assertEquals(null, x.$get(6));
		assertEquals("NaN", x.$get(7));
		assertEquals("-1", x.$get(8));
	}

	@Test
	public void testReverse05(){
		// test reverse in a sparse store
		Array<Object> x = $array();
		x.$set(0, 0);
		x.$set(1, 1);
		x.$set(1000, 1000);

		Array<Object> reverse = x.reverse();

		assertSame(x, reverse);
		assertEquals(1000, x.$get(0));
		assertEquals(null, x.$get(1));
		assertEquals(null, x.$get(2));
		assertEquals(null, x.$get(998));
		assertEquals(1, x.$get(999));
		assertEquals(0, x.$get(1000));
	}
}
