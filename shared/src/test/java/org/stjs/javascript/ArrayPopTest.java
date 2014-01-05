package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.stjs.javascript.JSCollections.$array;

import org.junit.Test;

// ===========================================
// Tests section 15.4.4.6 of the ECMA-262 Spec
// ===========================================
// If length equal zero, call the [[Put]] method of this object
// with arguments "length" and 0 and return undefined
//
// The last element of the array is removed from the array
// and returned

public class ArrayPopTest {

	@Test
	public void testPop01() {
		Array<Object> x = $array();
		Object pop = x.pop();

		assertNull(pop);
		assertEquals(x.$length(), 0);
	}

	@Test
	public void testPop02() {
		Array<Integer> x = $array(1, 2, 3);
		x.$length(0);
		Integer pop = x.pop();

		assertNull(pop);
		assertEquals(0, x.$length());
	}

	@Test
	public void testPop03() {
		Array<Integer> x = $array(0, 1, 2, 3);
		int pop = x.pop();

		assertEquals(3, pop);
		assertEquals(3, x.$length());
		assertEquals(null, x.$get(3));
		assertEquals(2, x.$get(2).intValue());
	}

	@Test
	public void testPop04() {
		Array<Integer> x = $array();
		x.$set(0, 0);
		x.$set(3, 3);

		Integer pop = x.pop();
		assertEquals(Integer.valueOf(3), pop);
		assertEquals(3, x.$length());
		assertEquals(null, x.$get(3));
		assertEquals(null, x.$get(2));

		pop = x.pop();
		assertEquals(null, pop);
		assertEquals(2, x.$length());
		assertEquals(null, x.$get(2));

		pop = x.pop();
		assertEquals(null, pop);
		assertEquals(1, x.$length());

		pop = x.pop();
		assertEquals(Integer.valueOf(0), pop);
		assertEquals(0, x.$length());
	}

	@Test
	public void testPop05() {
		Array<Integer> x = $array();
		x.$set(0, 0);
		x.$length(4);

		Integer pop3 = x.pop();
		Integer pop2 = x.pop();
		Integer pop1 = x.pop();
		Integer pop0 = x.pop();

		assertEquals(null, pop3);
		assertEquals(null, pop2);
		assertEquals(null, pop1);
		assertEquals(Integer.valueOf(0), pop0);
	}
}
