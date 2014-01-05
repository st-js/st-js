package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.stjs.javascript.JSCollections.$array;

import org.junit.Test;

// ============================================
// Tests section 15.4.4.12 of the ECMA-262 Spec
// ============================================

public class ArraySpliceTest {

	@Test
	public void testSplice01() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(0, 3);

		assertArrayEquals($array(0, 1, 2), arr);
		assertArrayEquals($array(3), x);
	}

	@Test
	public void testSplice02() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(0, 3, 4, 5);

		assertArrayEquals($array(0, 1, 2), arr);
		assertArrayEquals($array(4, 5, 3), x);
	}

	@Test
	public void testSplice03() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(0, 4);

		assertArrayEquals($array(0, 1, 2, 3), arr);
		assertArrayEquals(new Array<Integer>(), x);
	}

	@Test
	public void testSplice04() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(1, 3, 4, 5);

		assertArrayEquals($array(1, 2, 3), arr);
		assertArrayEquals($array(0, 4, 5), x);
	}

	@Test
	public void testSplice05() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(0, 5);

		assertArrayEquals($array(0, 1, 2, 3), arr);
		assertArrayEquals(new Array<Integer>(), x);
	}

	@Test
	public void testSplice06() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(1, 4, 4, 5);

		assertArrayEquals($array(1, 2, 3), arr);
		assertArrayEquals($array(0, 4, 5), x);
	}

	@Test
	public void testSplice07() {
		Array<Integer> x = $array(0, 1);
		Array<Integer> arr = x.splice(-2, -1);

		assertArrayEquals(new Array<Integer>(), arr);
		assertArrayEquals($array(0, 1), x);
	}

	@Test
	public void testSplice08() {
		Array<Integer> x = $array(0, 1);
		Array<Integer> arr = x.splice(-1, -1);

		assertArrayEquals(new Array<Integer>(), arr);
		assertArrayEquals($array(0, 1), x);
	}

	@Test
	public void testSplice09() {
		Array<Integer> x = $array(0, 1);
		Array<Integer> arr = x.splice(-2, -1, 2, 3);

		assertArrayEquals(new Array<Integer>(), arr);
		assertArrayEquals($array(2, 3, 0, 1), x);
	}

	@Test
	public void testSplice10() {
		Array<Integer> x = $array(0, 1);
		Array<Integer> arr = x.splice(-1, -1, 2, 3);

		assertArrayEquals(new Array<Integer>(), arr);
		assertArrayEquals($array(0, 2, 3, 1), x);
	}

	@Test
	public void testSplice11() {
		Array<Integer> x = $array(0, 1);
		Array<Integer> arr = x.splice(-3, -1, 2, 3);

		assertArrayEquals(new Array<Integer>(), arr);
		assertArrayEquals($array(2, 3, 0, 1), x);
	}

	@Test
	public void testSplice12() {
		Array<Integer> x = $array(0, 1);
		Array<Integer> arr = x.splice(0, -1);

		assertArrayEquals(new Array<Integer>(), arr);
		assertArrayEquals($array(0, 1), x);
	}

	@Test
	public void testSplice13() {
		Array<Integer> x = $array(0, 1);
		Array<Integer> arr = x.splice(2, -1);

		assertArrayEquals(new Array<Integer>(), arr);
		assertArrayEquals($array(0, 1), x);
	}

	@Test
	public void testSplice14() {
		Array<Integer> x = $array(0, 1);
		Array<Integer> arr = x.splice(0, -1, 2, 3);

		assertArrayEquals(new Array<Integer>(), arr);
		assertArrayEquals($array(2, 3, 0, 1), x);
	}

	@Test
	public void testSplice15() {
		Array<Integer> x = $array(0, 1);
		Array<Integer> arr = x.splice(2, -1, 2, 3);

		assertArrayEquals(new Array<Integer>(), arr);
		assertArrayEquals($array(0, 1, 2, 3), x);
	}

	@Test
	public void testSplice16() {
		Array<Integer> x = $array(0, 1);
		Array<Integer> arr = x.splice(3, -1, 2, 3);

		assertArrayEquals(new Array<Integer>(), arr);
		assertArrayEquals($array(0, 1, 2, 3), x);
	}

	@Test
	public void testSplice17() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(-4, 3);

		assertArrayEquals($array(0, 1, 2), arr);
		assertArrayEquals($array(3), x);
	}

	@Test
	public void testSplice18() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(-4, 3, 4, 5);

		assertArrayEquals($array(0, 1, 2), arr);
		assertArrayEquals($array(4, 5, 3), x);
	}

	@Test
	public void testSplice19() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(-5, 4);

		assertArrayEquals($array(0, 1, 2, 3), arr);
		assertArrayEquals(new Array<Integer>(), x);
	}

	@Test
	public void testSplice20() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(-3, 3, 4, 5);

		assertArrayEquals($array(1, 2, 3), arr);
		assertArrayEquals($array(0, 4, 5), x);
	}

	@Test
	public void testSplice21() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(-9, 5);

		assertArrayEquals($array(0, 1, 2, 3), arr);
		assertArrayEquals(new Array<Integer>(), x);
	}

	@Test
	public void testSplice22() {
		Array<Integer> x = $array(0, 1, 2, 3);
		Array<Integer> arr = x.splice(-3, 4, 4, 5);

		assertArrayEquals($array(1, 2, 3), arr);
		assertArrayEquals($array(0, 4, 5), x);
	}

	private static <T> void assertArrayEquals(Array<T> expected, Array<T> actual) {
		assertEquals(expected.$length(), actual.$length());
		for (int i = 0; i < expected.$length(); i++) {
			assertEquals(expected.$get(i), actual.$get(i));
		}
	}
}
