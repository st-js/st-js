package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.stjs.javascript.JSCollections.$array;

import org.junit.Test;

public class ArrayDeleteTest {

	@Test
	public void testDelete01() {
		// delete non existing elements has no effect
		Array<Integer> x = $array();
		x.$delete(0);
		x.$delete(-1);
		x.$delete(1000);

		assertEquals(0, x.$length());
	}

	@Test
	public void testDelete02() {
		// delete existing elements leaves holes, and the holes do not
		// show in for-each loops
		Array<Integer> x = $array(0, 1, 2, 3, 4);

		x.$delete(0);
		x.$delete(4);
		x.$delete("2");

		assertEquals(5, x.$length());
		assertEquals(null, x.$get(0));
		assertEquals(1, x.$get(1).intValue());
		assertEquals(null, x.$get(2));
		assertEquals(3, x.$get(3).intValue());
		assertEquals(null, x.$get(4));

		for (String i : x) {
			assertFalse(i.equals("0") || i.equals("2") || i.equals("4"));
		}
	}

	@Test
	public void testDelete03() {
		// delete can remove non-array elements, and the deleted non-array elements do not
		// show up in for-each loops
		Array<Integer> x = $array();
		x.$set("-4", 0);
		x.$set("bar", 1);
		x.$set("denver", 2);

		x.$delete(-4);
		x.$delete("bar");

		assertEquals(0, x.$length());
		assertNull(x.$get("-4"));
		assertNull(x.$get("bar"));
		assertEquals(2, x.$get("denver").intValue());

		for (String i : x) {
			assertFalse(i.equals("-4") || i.equals("bar"));
		}
	}

	@Test
	public void testDelete04() {
		// test delete in a sparse store
		Array<Integer> x = $array(0, 1, 2, 3);
		x.$set(100, 100);
		x.$set(1000, 1000);
		x.$set(10000, 10000);
		x.$set(100000, 100000);
		x.$set(1000000, 1000000);

		x.$delete(100);
		x.$delete(10000);
		x.$delete(1000000);

		assertEquals(1000001, x.$length());
		assertEquals(null, x.$get(100));
		assertEquals(1000, x.$get(1000).intValue());
		assertEquals(null, x.$get(10000));
		assertEquals(100000, x.$get(100000).intValue());
		assertEquals(null, x.$get(1000000));

		for (String i : x) {
			assertFalse(i.equals("100") || i.equals("10000") || i.equals("1000000"));
		}
	}

	/**
	 * Deleting the last element of the array doesn't change the length.
	 */
	@Test
	public void testDelete05(){
		Array<Integer> x = $array(0, 1, 2, 3);

		x.$delete(3);

		assertNull(x.$get(3));
		assertEquals(4, x.$length());
	}
}
