package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.stjs.javascript.JSCollections.$array;

import org.junit.Test;

// ============================================
// Tests section 15.4.4.10 of the ECMA-262 Spec
// ============================================
//
// If start is positive, use min(start, length).
// If start is negative, use max(start + length, 0).
// If end is positive, use min(end, length)
// If end is negative, use max(end + length, 0)
//
// Note: we are not including tests that check for the correct behavior to slice()
// when the end or start parameters are not numbers, because our interface declaration
// does not allow that (and it seems like a bad practice anyway)

public class ArraySliceTest {

	@Test
	public void slice_simple() {
		Array<String> a = $array("1", "2", "3", "4");
		Array<String> b = a.slice(1, 2);

		assertEquals("1,2,3,4", a.toString()); // make sure I have sliced and not spliced
		assertEquals("2", b.toString());
	}

	@Test
	public void testSlice01() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(0, 3);

		assertEquals(3, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(1, arr.$get(1).intValue());
		assertEquals(2, arr.$get(2).intValue());
		assertEquals(null, arr.$get(3));
	}

	@Test
	public void testSlice02() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(3, 3);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}

	@Test
	public void testslice03() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(4, 3);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}

	@Test
	public void testslice04() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(5, 5);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}

	@Test
	public void testSlice05() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(3, 5);

		assertEquals(2, arr.$length());
		assertEquals(3, arr.$get(0).intValue());
		assertEquals(4, arr.$get(1).intValue());
		assertEquals(null, arr.$get(3));
	}

	@Test
	public void testSlice06() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(2, 4);

		assertEquals(2, arr.$length());
		assertEquals(2, arr.$get(0).intValue());
		assertEquals(3, arr.$get(1).intValue());
		assertEquals(null, arr.$get(3));
	}

	@Test
	public void testSlice07() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(3, 6);

		assertEquals(2, arr.$length());
		assertEquals(3, arr.$get(0).intValue());
		assertEquals(4, arr.$get(1).intValue());
		assertEquals(null, arr.$get(3));
	}

	@Test
	public void testSlice08() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(-3, 3);

		assertEquals(1, arr.$length());
		assertEquals(2, arr.$get(0).intValue());
		assertEquals(null, arr.$get(1));
	}

	@Test
	public void testSlice09() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(-1, 5);

		assertEquals(1, arr.$length());
		assertEquals(4, arr.$get(0).intValue());
		assertEquals(null, arr.$get(1));
	}

	@Test
	public void testSlice10() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(-5, 1);

		assertEquals(1, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(null, arr.$get(1));
	}

	@Test
	public void testSlice11() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(-9, 5);

		assertEquals(5, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(1, arr.$get(1).intValue());
		assertEquals(2, arr.$get(2).intValue());
		assertEquals(3, arr.$get(3).intValue());
		assertEquals(4, arr.$get(4).intValue());
		assertEquals(null, arr.$get(5));
	}

	@Test
	public void testSlice12() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(0, -2);

		assertEquals(3, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(1, arr.$get(1).intValue());
		assertEquals(2, arr.$get(2).intValue());
		assertEquals(null, arr.$get(3));
	}

	@Test
	public void testSlice13() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(1, -4);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}

	@Test
	public void testSlice14() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(0, -5);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}

	@Test
	public void testSlice15() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(4, -9);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}

	@Test
	public void testSlice16() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(-5, -2);

		assertEquals(3, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(1, arr.$get(1).intValue());
		assertEquals(2, arr.$get(2).intValue());
		assertEquals(null, arr.$get(3));
	}

	@Test
	public void testSlice17() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(-3, -1);

		assertEquals(2, arr.$length());
		assertEquals(2, arr.$get(0).intValue());
		assertEquals(3, arr.$get(1).intValue());
		assertEquals(null, arr.$get(2));
	}

	@Test
	public void testSlice18() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(-9, -1);

		assertEquals(4, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(1, arr.$get(1).intValue());
		assertEquals(2, arr.$get(2).intValue());
		assertEquals(3, arr.$get(3).intValue());
		assertEquals(null, arr.$get(4));
	}

	@Test
	public void testSlice19() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(-6, -6);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}

	@Test
	public void testSlice20() {
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		Array<Integer> arr = x.slice(-2);

		assertEquals(2, arr.$length());
		assertEquals(3, arr.$get(0).intValue());
		assertEquals(4, arr.$get(1).intValue());
		assertEquals(null, arr.$get(2));
	}
}
