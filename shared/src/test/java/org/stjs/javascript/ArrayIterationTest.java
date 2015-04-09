package org.stjs.javascript;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.stjs.javascript.JSCollections.$array;

import java.util.ArrayList;
import java.util.Iterator;
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

	@Test
	public void testIterator07() {
		// now a tricky one: we'll change the array store type from packed to sparse during the iteration
		Array<Integer> x = $array();
		for(int i = 0; i < 1000; i++){
			x.push(i);
		}

		Iterator<String> keyIter = x.iterator();
		assertEquals("0", keyIter.next());
		for(int i = 1; i < 998; i ++){
			x.$delete(i);
		}
		assertEquals("998", keyIter.next());
		assertEquals("999", keyIter.next());
	}

	@Test
	public void testIterator08() {
		// equally tricky, but the other way: we'll change the array store type from sparse to packed during the
		// iteration
		Array<Integer> x = $array();
		x.$set(10000, 10000);
		x.$set(0, 0);
		x.$set(1, 1);

		Iterator<String> keyIter = x.iterator();
		assertEquals("0", keyIter.next());
		x.$length(2);
		assertEquals("1", keyIter.next());
		assertFalse(keyIter.hasNext());
	}

	@Test
	public void testIterator09() {
		// Delete some key in a sparse store during the iteration
		Array<Integer> x = $array();
		x.$set(10000, 10000);
		x.$set(0, 0);
		x.$set(1, 1);

		Iterator<String> keyIter = x.iterator();
		assertEquals("0", keyIter.next());
		x.$delete(1);
		assertEquals("10000", keyIter.next());
		assertFalse(keyIter.hasNext());
	}

	@Test
	public void testIterator10() {
		// Add some key in a sparse store during the iteration
		Array<Integer> x = $array();
		x.$set(10000, 10000);
		x.$set(0, 0);
		x.$set(1, 1);

		Iterator<String> keyIter = x.iterator();
		assertEquals("0", keyIter.next());
		assertEquals("1", keyIter.next());
		x.$set(2, 2);
		assertEquals("2", keyIter.next());
		assertEquals("10000", keyIter.next());
		assertFalse(keyIter.hasNext());
	}

	@Test
	public void testIterator11() {
		// Delete some key in a packed store during the iteration
		Array<Integer> x = $array(0, 1, 2);

		Iterator<String> keyIter = x.iterator();
		assertEquals("0", keyIter.next());
		x.$delete(1);
		assertEquals("2", keyIter.next());
		assertFalse(keyIter.hasNext());
	}

	@Test
	public void testIterator12() {
		// Add some key in a packed store during the iteration
		Array<Integer> x = $array();
		x.$set(0, 0);
		x.$set(2, 2);

		Iterator<String> keyIter = x.iterator();
		assertEquals("0", keyIter.next());
		x.$set(1, 1);
		assertEquals("1", keyIter.next());
		assertEquals("2", keyIter.next());
		assertFalse(keyIter.hasNext());
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
