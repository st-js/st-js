package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.stjs.javascript.JSGlobal.Array;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Tests for section 15.4.1 of the ECMA-262 specification
 */
public class ArrayConstructorTest {

	@Test
	public void testConstructor01() {
		// When the constructor of Array is passed exactly 1 parameter, then the first parameter
		// is treated as the length of the array
		Array<Integer> x = Array(2);
		assertEquals(2, x.$length());
		assertNull(x.$get(0));

		// should also work the same when using varargs
		Object length = 2;
		Array<Object> y = Array(length); // not the same constructor as above, Array(Object...)
		assertEquals(2, x.$length());
		assertNull(x.$get(0));
	}

	@Test
	public void testConstructor02() {
		// The length property of the newly constructed object;
		// is set to the number of arguments
		assertEquals(0, Array().$length());
		assertEquals(4, Array(0, 1, 0, 1).$length());
		assertEquals(2, Array(null, null).$length());
	}

	@Test
	public void testConstructor03() {
		// The 0 property of the newly constructed object is set to item0
		// (if supplied); the 1 property of the newly constructed object is set to item1
		// (if supplied); and, in general, for as many arguments as there are, the k property
		// of the newly constructed object is set to argument k, where the first argument is
		// considered to be argument number
		Array<Integer> x = Array( //
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, //
				10, 11, 12, 13, 14, 15, 16, 17, 18, 19, //
				20, 21, 22, 23, 24, 25, 26, 27, 28, 29, //
				30, 31, 32, 33, 34, 35, 36, 37, 38, 39, //
				40, 41, 42, 43, 44, 45, 46, 47, 48, 49, //
				50, 51, 52, 53, 54, 55, 56, 57, 58, 59, //
				60, 61, 62, 63, 64, 65, 66, 67, 68, 69, //
				70, 71, 72, 73, 74, 75, 76, 77, 78, 79, //
				80, 81, 82, 83, 84, 85, 86, 87, 88, 89, //
				90, 91, 92, 93, 94, 95, 96, 97, 98, 99 //
		);
		for (int i = 0; i < 100; i++) {
			assertEquals(i, (int) x.$get(i));
		}
	}

	@Test
	public void testConstructor04() {
		List<Integer> ints = Arrays.asList(0, 1, 2, 3);
		Array<Integer> arr = new Array<>(ints);

		assertEquals(ints.size(), arr.$length());
		for(int i = 0; i < ints.size(); i ++){
			assertEquals(ints.get(i), arr.$get(i));
		}
	}
}
