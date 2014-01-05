package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArrayLengthTest {

	@Test
	public void testLength01() {
		// shorten length in a packed store
		Array<Integer> x = new Array<Integer>(0, 1, 2, 3, 4, 5);

		x.$length(2);

		assertEquals(2, x.$length());
		assertEquals(0, x.$get(0).intValue());
		assertEquals(1, x.$get(1).intValue());
		assertNull(x.$get(2));
		assertNull(x.$get(5));
	}

	@Test
	public void testLength02() {
		// Shorten length in a sparse store
		Array<Integer> x = new Array<Integer>(0, 1, 2, 3, 4, 5);
		x.$set(1000, 1000);
		x.$set(1001, 1001);
		x.$set(1002, 1002);

		x.$length(500);

		assertEquals(500, x.$length());
		assertEquals(0, x.$get(0).intValue());
		assertEquals(5, x.$get(5).intValue());
		assertNull(x.$get(1000));
		assertNull(x.$get(1001));
		assertNull(x.$get(1002));
	}

}
