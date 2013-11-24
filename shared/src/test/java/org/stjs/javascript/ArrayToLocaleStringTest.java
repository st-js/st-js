package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.stjs.javascript.JSGlobal.Array;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

//===================================================
// Tests for the section 14.4.4.3 of the ECMA-262 spec
// ===================================================
// The elements of the array are converted to strings using their
// toLocaleString methods, and these strings are then concatenated, separated
// by occurrences of a separator string that has been derived in an
// implementation-defined locale-specific way

public class ArrayToLocaleStringTest {

	@Test
	public void testToLocaleString01() {
		// verify that we correctly call toLocaleString on the elements
		final AtomicInteger n = new AtomicInteger(0);
		Object obj = new Object() {
			@SuppressWarnings("unused")
			public String toLocaleString() {
				n.incrementAndGet();
				return "";
			};
		};
		Array<Object> arr = Array(null, obj, null, obj, obj);
		arr.toLocaleString();

		assertEquals(3, n.get());
	}
}
