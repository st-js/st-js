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
