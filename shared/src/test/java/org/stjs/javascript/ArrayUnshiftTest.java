package org.stjs.javascript;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

//============================================
//Tests section 15.4.4.13 of the ECMA-262 Spec
//============================================

public class ArrayUnshiftTest {

	@Test
	public void testUnshift01() {
		Array<Integer> x = new Array<Integer>();
		int len = x.unshift(1);

		assertEquals(1, len);
		assertEquals(1, x.$get(0).intValue());

		len = x.unshift();

		assertEquals(1, len);
		assertEquals(null, x.$get(1));

		len = x.unshift(-1);

		assertEquals(-1, x.$get(0).intValue());
		assertEquals(2, x.$length());
	}

	@Test
	public void testUnshift02() {
		Array<Object> x = JSCollections.$array();
		x.$set(0, 0);
		int len = x.unshift(true, Double.POSITIVE_INFINITY, "NaN", "1", -1);

		assertEquals(6, len);
		assertEquals(0, x.$get(5));
		assertEquals(true, x.$get(0));
		assertEquals(Double.POSITIVE_INFINITY, x.$get(1));
		assertEquals("NaN", x.$get(2));
		assertEquals("1", x.$get(3));
		assertEquals(-1, x.$get(4));
		assertEquals(6, x.$length());
	}
}
