package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.stjs.javascript.JSCollections.$array;

import org.junit.Test;

/**
 * Tests for the section 15.4 of the ECMA-262 specification
 * <p>
 * Note: We know we are out of spec since we only accept array indices between -2^31 and 2^31-1 (the spec specifies
 * 2^32), but this is the maximum range int in java will allow. We believe this is not likely to cause problems in real
 * world programs.
 * <p>
 * Another point where we know we are out of spec, is that javascript allows any kind of Object as index (and will use
 * it's toString() method to figure out where it should land in the array) this however is quite confusing and we have
 * made the conscious decision not to support it
 */
public class ArrayGetterSetterTest {

	@Test
	public void testGetterSetter01() {
		// A property name P (in the form of a string value) is an array index
		// if and only if ToString(ToUint32(P)) is equal to P and ToUint32(P) is not equal to 2^32 - 1
		Array<Integer> x = $array();
		x.$set("true", 1); // "true" should not be converted to 1, but should still be accessible
		assertEquals(null, x.$get(1));
		assertEquals(1, x.$get("true").intValue());
	}

	@Test
	public void testGetterSetter02() {
		Array<Integer> x = $array();
		x.$set("false", 0);
		assertEquals(null, x.$get(0)); // same goes for "false"
		assertEquals(0, x.$get("false").intValue());
	}

	@Test
	public void testGetterSetter03() {
		Array<Integer> x = $array();
		x.$set("NaN", 1);
		assertEquals(null, x.$get(0));
		assertEquals(1, x.$get("NaN").intValue());
	}

	@Test
	public void testGetterSetter04() {
		Array<Integer> x = $array();
		x.$set("Infinity", 1);
		assertEquals(null, x.$get(0));
		assertEquals(1, x.$get("Infinity").intValue());
	}

	@Test
	public void testGetterSetter05() {
		Array<Integer> x = $array();
		x.$set("-Infinity", 1);
		assertEquals(null, x.$get(0));
		assertEquals(1, x.$get("-Infinity").intValue());
	}

	@Test
	public void testGetterSetter06() {
		Array<Integer> x = $array();
		x.$set("1.1", 1);
		assertEquals(null, x.$get(1));
		assertEquals(1, x.$get("1.1").intValue());
	}

	@Test
	public void testGetterSetter07() {
		Array<Integer> x = $array();
		x.$set("0", 0);
		assertEquals(0, x.$get(0).intValue());
	}

	@Test
	public void testGetterSetter08() {
		Array<Integer> x = $array();
		x.$set("1", 1);
		assertEquals(1, x.$get(1).intValue());
	}

	@Test
	public void testGetterSetter09() {
		Array<Integer> x = $array();
		x.$set(null, 0);
		assertEquals(null, x.$get(0));
		assertEquals(0, x.$get("null").intValue());
	}

	@Test
	public void testGetterSetter10() {
		// Array index is power of two
		Array<Integer> x = $array();
		int k = 1;
		// FIXME: int here should be replaced by a string coming from a long, so we can test
		// indices all the way to 2^32
		for (int i = 0; i < 32; i++) { // /!\ original test says 32, but our ints only go to 31
			k = k * 2;
			x.$set(k - 2, k);
		}

		k = 1;
		for (int i = 0; i < 32; i++) {
			k = k * 2;
			assertEquals(k, x.$get(k - 2).intValue());
		}
	}
}
