package org.stjs.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

public class JSCollectionsTest {

	@Test
	public void testArrayImpl() {
		Array<Integer> array = JSCollections.$array(1, 2, 3);
		assertNotNull(array);
		assertEquals(3, array.$length());
		assertEquals(2, (int) array.$get(1));
		assertNull(array.$get(3));// null when out of bound
		array.$set(1, 4);
		// 1,4,3
		assertEquals(4, (int) array.$get(1));
		array.push(5);
		// 1,4,3,5
		assertEquals(4, array.$length());
		Array<Integer> removed = array.splice(0, 2, 10);
		// 10, 3,5
		assertEquals(2, removed.$length());
		assertEquals(4, (int) removed.$get(1));
		assertEquals(3, array.$length());
		assertEquals(10, (int) array.$get(0));
		assertEquals(3, (int) array.$get(1));

		// add after bounds
		array.$set(4, 20);
		assertEquals(5, array.$length());
		assertNull(array.$get(3));
		assertEquals(20, (int) array.$get(4));

		// change length
		array.$length(3);
		assertEquals(3, array.$length());
		assertNull(array.$get(4));
	}

	@Test
	public void testMapImpl() {
		Map<String, Integer> map = JSCollections.$map("a", 1);
		assertNotNull(map);
		assertEquals(1, (int) map.$get("a"));
		assertNull(map.$get("b"));

		map.$put("a", 2);
		assertEquals(2, (int) map.$get("a"));

		map.$put("b", 3);
		assertEquals(3, (int) map.$get("b"));

		map.$delete("b");
		assertNull(map.$get("b"));

	}
}
