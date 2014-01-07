package org.stjs.javascript;

import static junit.framework.Assert.assertEquals;
import static org.stjs.javascript.JSCollections.$array;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ArrayIterationTest {

	@Test
	public void testIteration01() {
		// without holes in the array
		Array<Integer> x = $array(0, 1, 2, 3, 4);
		assertIteratedKeys(x, "0", "1", "2", "3", "4");
	}

	@Test
	public void testIteration02() {
		// packed store with holes in the array, but not on the first or last element
		Array<Integer> x = new Array<Integer>();
		x.$set(0, 0);
		x.$set(2, 2);
		x.$set(5, 5);

		assertIteratedKeys(x, "0", "2", "5");
	}

	@Test
	public void testIteration03() {
		// packed store with holes in the array, including on the first or last element
		Array<Integer> x = new Array<Integer>();
		x.$set(1, 1);
		x.$set(2, 2);
		x.$set(5, 5);
		x.$length(10);

		assertIteratedKeys(x, "1", "2", "5");
	}

	@Test
	public void testIteration04() {
		// sparse store with holes in the array, but not on the first or last element
		Array<Integer> x = new Array<Integer>();
		x.$set(0, 0);
		x.$set(10000, 10000);
		x.$set(1234567, 1234567);
		x.$set("7000000", 0xDEADBEEF); // i > Integer.MAX_VALUE, but smaller than 2^32, which is a valid array index

		assertIteratedKeys(x, "0", "10000", "1234567", "7000000");
	}

	@Test
	public void testIteration05() {
		// sparse store with holes in the array, including on the first or last element
		Array<Integer> x = new Array<Integer>();
		x.$set(1, 1);
		x.$set(10, 10);
		x.$set(100, 100);
		x.$length(1000);

		assertIteratedKeys(x, "1", "10", "100");
	}

	@Test
	public void testIteration06() {
		// non-array elements should also be iterated over
		Array<Integer> x = $array(0, 1, 2);
		x.$set("foo", 3);
		x.$set("jambon", 4);

		assertIteratedKeys(x, "0", "1", "2", "foo", "jambon");
	}

	private void assertIteratedKeys(Array<Integer> x, String... expectedKeys) {
		List<String> keys = new ArrayList<String>();
		for (String key : x) {
			keys.add(key);
		}

		assertEquals("wrong number of keys returned", expectedKeys.length, keys.size());
		for (int i = 0; i < expectedKeys.length; i++) {
			assertEquals("Wrong key at index " + i, expectedKeys[i], keys.get(i));
		}
	}
}