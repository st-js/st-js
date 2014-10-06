package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.stjs.javascript.JSCollections.$array;

import org.junit.Test;

/**
 * Tests for the section 15.4 of the ECMA-262 specification
 * <p>
 * Note: We know we are out of spec since we only accept array indices between -2^31 and 2^31-1 (the spec specifies 2^32), but this is the
 * maximum range int in java will allow. We believe this is not likely to cause problems in real world programs.
 * <p>
 * Another point where we know we are out of spec, is that javascript allows any kind of Object as index (and will use it's toString() method to
 * figure out where it should land in the array) this however is quite confusing and we have made the conscious decision not to support it
 */
public class ArrayGetterSetterTest {

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

}
