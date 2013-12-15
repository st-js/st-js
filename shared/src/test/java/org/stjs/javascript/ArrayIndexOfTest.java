package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.stjs.javascript.JSCollections.$array;

import org.junit.Test;

//============================================
//Tests section 15.4.4.14 of the ECMA-262 Spec
//============================================

public class ArrayIndexOfTest {

	@Test
	public void testIndexOf01() {
		Array<Object> a = new Array<Object>();
		a.$set(100, 1);
		a.$set(99999, "");
		a.$set(10, new Object());
		a.$set(5555, 5.5);
		a.$set(123456, "str");
		a.$set(5, 1e+305);

		assertEquals(100, a.indexOf(1));
		assertEquals(99999, a.indexOf(""));
		assertEquals(123456, a.indexOf("str"));
		assertEquals(5, a.indexOf(1e+305));
		assertEquals(5555, a.indexOf(5.5));

		assertEquals(-1, a.indexOf(true));
		assertEquals(-1, a.indexOf(5));
		assertEquals(-1, a.indexOf("str1"));
		assertEquals(-1, a.indexOf(null));
		assertEquals(-1, a.indexOf(new Object()));
	}

	@Test
	public void testIndexOf02() {
		assertEquals(-1, $array().indexOf(42));
	}

	@Test
	public void testIndexOf03() {
		Array<Integer> a = $array(0, 1, 2, 3, 4);
		assertEquals(0, a.indexOf(0, 0));
		assertEquals(2, a.indexOf(2, 1));
		assertEquals(2, a.indexOf(2, 2));
		assertEquals(4, a.indexOf(4, 2));
		assertEquals(4, a.indexOf(4, 4));
		assertEquals(-1, a.indexOf(1, -3));
		assertEquals(-1, a.indexOf(0, -4));
	}

	@Test
	public void testIndexOf04() {
		Array<Integer> arr = $array(0, 1, 2, 3, 4);
		// 'fromIndex' will be set as 0 if not passed by default
		assertEquals(arr.indexOf(0), arr.indexOf(0, 0));
		assertEquals(arr.indexOf(2), arr.indexOf(2, 0));
		assertEquals(arr.indexOf(4), arr.indexOf(4, 0));
	}

	@Test
	public void testIndexOf05() {
		Array<Integer> a = $array(1, 2, 3);
		assertEquals(-1, a.indexOf(1, 5));
		assertEquals(-1, a.indexOf(1, 3));
		assertEquals(-1, a.indexOf(1, 2));
		assertEquals(2, a.indexOf(3, 2));
		assertEquals(1, a.indexOf(2, 1));
		assertEquals(-1, a.indexOf(1, 1));
		assertEquals(-1, $array().indexOf(1, 0));
		assertEquals(-1, a.indexOf(2, -1));
		assertEquals(1, a.indexOf(2, -2));
		assertEquals(0, a.indexOf(1, -3));
	}

	@Test
	public void testIndexOf06() {
		Array<Object> a = $array(new Object(), "true", null, 0, false, null, 1, "str", 0, 1, true, false, true, false);
		assertEquals(10, a.indexOf(true));
		assertEquals(4, a.indexOf(false));
	}

	@Test
	public void testIndexOf07() {
		Array<Object> a = new Array<Object>("NaN", null, 0, false, null, "false", Double.NaN);
		// NaN is equal to nothing, including itself.
		assertEquals(-1, a.indexOf(Double.NaN));
	}

	@Test
	public void testIndexOf08() {
		Array<Object> a = new Array<Object>(false, null, null, "0", new Object(), -1.33333333333333333, "str", -0,
				true, +0, 1, 1, 0, false, -(4 / 3), -(4 / 3));
		assertEquals(14, a.indexOf(-(4 / 3)));
		assertEquals(7, a.indexOf(0));
		assertEquals(7, a.indexOf(-0));
		assertEquals(10, a.indexOf(1));
	}
}
