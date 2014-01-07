package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSGlobal.NaN;

import org.junit.Test;

//============================================
//Tests section 15.4.4.15 of the ECMA-262 Spec
//============================================

public class ArrayLastIndexOfTest {

	@Test
	public void testLastIndexOf01() {
		// lastIndexOf returns -1 if 'length' is 0 (empty array)
		assertEquals(-1, $array().lastIndexOf(42));
		assertEquals(-1, $array().lastIndexOf(1, 0));
	}

	/**
	 * @path ch15/15.4/15.4.4/15.4.4.15/15.4.4.15-5-6.js
	 * @description Array.prototype.lastIndexOf when 'fromIndex' isn't passed
	 */

	@Test
	public void testLastIndexOf02() {
		Array<Integer> arr = $array(0, 1, 2, 3, 4);
		// 'fromIndex' will be set as 4 if not passed by default
		assertEquals(arr.lastIndexOf(0), arr.lastIndexOf(0, 4));
		assertEquals(arr.lastIndexOf(2), arr.lastIndexOf(2, 4));
		assertEquals(arr.lastIndexOf(4), arr.lastIndexOf(4, 4));
	}

	@Test
	public void testLastIndexOf03() {
		assertEquals(-1, $array(0, 100).lastIndexOf(100, 0));// verify fromIndex is not more than 0
		assertEquals(0, $array(200, 0).lastIndexOf(200, 0)); // verify fromIndex is not less than 0
	}

	@Test
	public void testLastIndexOf04() {
		assertEquals(0, $array(0, 1, 2, 3, 4).lastIndexOf(0, 0));
		assertEquals(0, $array(0, 1, 2, 3, 4).lastIndexOf(0, 2));
		assertEquals(2, $array(0, 1, 2, 3, 4).lastIndexOf(2, 2));
		assertEquals(2, $array(0, 1, 2, 3, 4).lastIndexOf(2, 4));
		assertEquals(4, $array(0, 1, 2, 3, 4).lastIndexOf(4, 4));

		assertEquals(-1, $array(1, 2, 3).lastIndexOf(3, 1));
		assertEquals(1, $array(1, 2, 3).lastIndexOf(2, 1));

		assertEquals(1, $array(1, 2, 3).lastIndexOf(2, -2));
		assertEquals(-1, $array(1, 2, 3).lastIndexOf(2, -3));

		assertEquals(3, $array(1, 2, 3, 4).lastIndexOf(4, -1));
		assertEquals(-1, $array(1, 2, 3, 4).lastIndexOf(3, -3));
		assertEquals(-1, $array(1, 2, 3, 4).lastIndexOf(2, -4));
	}

	@Test
	public void testLastIndexOf05() {
		Object obj = new Object();
		Array<Object> a = new Array<Object>(false, true, false, obj, false, true, "true", null, 0, null, 1, "str", 0, 1);
		assertEquals(5, a.lastIndexOf(true));
		assertEquals(4, a.lastIndexOf(false));
	}

	@Test
	public void testLastIndexOf06() {
		Object obj = new Object();
		Integer one = 1;
		Double _float = -(4.0 / 3.0);
		Array<Object> a = new Array<Object>(+0, true, 0, -0, false, null, null, "0", obj, _float, -(4.0 / 3.0),
				-1.3333333333333, "str", one, 1, false);

		assertEquals(10, a.lastIndexOf(-(4.0 / 3.0)));
		assertEquals(3, a.lastIndexOf(0)); // a[3] = -0, but using === -0 and 0 are equal
		assertEquals(3, a.lastIndexOf(-0)); // a[3] = -0
		assertEquals(14, a.lastIndexOf(1));
	}

	@Test
	public void testLastIndexOf07() {
		Object obj = new Object() {
			public String toString() {
				return "false";
			}
		};
		String szFalse = "false";
		Array<Object> a = new Array<Object>(szFalse, "false", "false1", null, 0, false, null, 1, obj, 0);

		assertEquals(1, a.lastIndexOf("false"));
	}

	@Test
	public void testLastIndexOf08() {
		Object obj = new Object() {
			public String toString() {
				return null;
			}
		};
		String _null = null;
		Array<Object> a = new Array<Object>(true, null, 0, false, null, 1, "str", 0, 1, null, true, false, null, _null,
				"null", "str", obj);
		assertEquals(13, a.lastIndexOf(null));
	}

	@Test
	public void testLastIndexOf09() {
		Number _NaN = NaN;
		Array<Object> a = new Array<Object>("NaN", _NaN, NaN, null, 0, false, null, "false");
		assertEquals(-1, a.lastIndexOf(NaN)); // NaN matches nothing, not even itself
	}

	@Test
	public void testLastIndexOf10() {
		// undefined property wouldn't be called
		Array<Integer> a = $array();
		a.$set(0, 0);
		a.$set(2, 2);
		assertEquals(-1, a.lastIndexOf(null));
	}
}
