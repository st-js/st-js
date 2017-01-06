package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.stjs.javascript.JSCollections.$array;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.stjs.javascript.functions.Function3;

//============================================
//Tests section 15.4.4.16 of the ECMA-262 Spec
//============================================

public class ArrayEveryTest {

	@Test(expected = Error.class)
	public void testEvery00(){
		Array<Object> arr = $array();
		arr.every((Function3)null);
	}

	@Test
	public void testEvery01() {
		// every doesn't visit non-array indices
		final AtomicInteger callCnt = new AtomicInteger(0);
		Array<Integer> arr = $array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		arr.$set("i", 10);
		arr.$set("true", 11);

		boolean result = arr.every(new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer p1, Long p2, Array<Integer> p3) {
				callCnt.incrementAndGet();
				return true;
			}
		});

		assertTrue(result);
		assertEquals(10, callCnt.get());
	}

	@Test
	public void testEvery02() {
		// every returns true if 'length' is 0 (empty array)
		Array<Integer> a = $array();
		assertTrue(a.every(callback(a, false)));
	}

	@Test
	public void testEvery03() {
		// callbackfn is called with 3 formal parameter
		final AtomicInteger called = new AtomicInteger(0);
		EveryCB<Integer> cb = new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> array) {
				called.incrementAndGet();
				return val > 10 && array.$get(idx.intValue()).equals(val);
			}
		};

		assertTrue($array(11, 12, 13).every(cb));
		assertEquals(3, called.get());
	}

	@Test
	public void testEvery04() {
		// k values are passed in ascending numeric order
		Array<Integer> arr = $array(0, 1, 2, 3, 4, 5);
		final AtomicInteger lastIdx = new AtomicInteger(0);
		final AtomicInteger called = new AtomicInteger(0);
		EveryCB<Integer> cb = new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> array) {
				called.incrementAndGet();
				if (lastIdx.get() != idx) {
					return false;
				} else {
					lastIdx.incrementAndGet();
					return true;
				}
			}
		};

		assertTrue(arr.every(cb));
		assertEquals(arr.$length(), called.get());
	}

	@Test
	public void testEvery05() {
		// every immediately returns false if callbackfn returns false
		final AtomicInteger callCnt = new AtomicInteger(0);
		EveryCB<Integer> callbackfn = new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> array) {
				callCnt.incrementAndGet();
				if (idx > 5) {
					return false;
				} else {
					return true;
				}
			}

		};

		Array<Integer> arr = $array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		assertFalse(arr.every(callbackfn));
		assertEquals(7, callCnt.get());
	}

	@Test
	public void testEvery06() {
		// callbackfn not called for indexes never been assigned values

		final AtomicInteger callCnt = new AtomicInteger(0);
		EveryCB<Integer> callbackfn = new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> array) {
				callCnt.incrementAndGet();
				return true;
			}
		};
		Array<Integer> arr = new Array<Integer>(10);
		arr.$set(1, null);

		arr.every(callbackfn);

		assertEquals(1, callCnt.get());
	}

	@Test
	public void testEvery07() {
		// every doesn't consider newly added elements in sparse array
		final Array<Integer> arr = new Array<Integer>(10);
		arr.$set(1, 1);
		arr.$set(2, 2);

		EveryCB<Integer> callbackfn = new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> array) {
				arr.$set(1000, 3);
				if (val < 3) {
					return true;
				} else {
					return false;
				}
			}
		};

		assertTrue(arr.every(callbackfn));
	}

	@Test
	public void testEvery08() {
		// every doesn't visit deleted elements when Array.length is decreased
		final Array<Integer> arr = $array(1, 2, 3, 4, 6);

		EveryCB<Integer> callbackfn = new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> array) {
				arr.$length(3);
				if (val < 4) {
					return true;
				} else {
					return false;
				}
			}
		};

		assertTrue(arr.every(callbackfn));
	}

	@Test
	public void testEvery09() {
		// every doesn't visit deleted elements in array after the call
		final Array<Integer> arr = $array(1, 2, 3, 4, 5);
		EveryCB<Integer> callbackfn = new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> array) {
				arr.$delete(2);
				if (val == 3) {
					return false;
				} else {
					return true;
				}
			}
		};

		assertTrue(arr.every(callbackfn));
	}

	@Test
	public void testEvery10() {
		// every considers new value of elements in array after the call
		final Array<Integer> arr = $array(1, 2, 3, 4, 5);
		EveryCB<Integer> callbackfn = new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> array) {
				arr.$set(4, 6);
				if (val < 6) {
					return true;
				} else {
					return false;
				}
			}
		};

		assertFalse(arr.every(callbackfn));
	}

	@Test
	public void testEvery11() {
		// every considers new elements added to array after the call
		final AtomicBoolean calledForThree = new AtomicBoolean(false);
		final Array<Integer> arr = $array(1, 2);
		arr.$set(3, 4);
		arr.$set(4, 5);
		EveryCB<Integer> callbackfn = new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> array) {
				arr.$set(2, 3);
				if (val == 3) {
					calledForThree.set(true);
				}
				return true;
			}
		};

		// note that arr[2] is not set at this point
		assertTrue(arr.every(callbackfn));
		assertTrue(calledForThree.get());
	}

	/**
	 * Change the Array store type from sparse to packed during iteration
	 */
	@Test
	public void testEvery12(){
		final Array<Integer> arr = new Array<>(1000);
		arr.$set(0, 0);
		arr.$set(999, 999);

		final AtomicInteger callCnt = new AtomicInteger(0);
		EveryCB<Integer> callbackfn = new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
				if(idx == 0){
					// add lots of elements to force the change of array store
					for(int i = 1; i < 999; i ++){
						arr.$set(i, i);
					}
				}
				return true;
			}
		};

		arr.every(callbackfn);

		assertEquals(1000, callCnt.get());
	}

	/**
	 * Change the Array store type from packed to sparse during iteration
	 */
	@Test
	public void testEvery13(){
		final Array<Integer> arr = $array();
		for(int i = 0; i < 1000; i ++){
			arr.$set(i, i);
		}

		final AtomicInteger callCnt = new AtomicInteger(0);
		EveryCB<Integer> callbackfn = new EveryCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
				if(idx == 0){
					// remove lots of elements to force the change of array store
					for(int i = 1; i < 999; i ++){
						arr.$delete(i);
					}
				}
				return true;
			}
		};

		arr.every(callbackfn);

		assertEquals(2, callCnt.get()); // only 0 and 999 remain
	}

	private static <V> Function3<V, Long, Array<V>, Boolean> callback(Array<V> arr, final Boolean retVal) {
		return new Function3<V, Long, Array<V>, Boolean>() {
			@Override
			public Boolean $invoke(V p1, Long p2, Array<V> p3) {
				return retVal;
			}
		};
	}

	private static interface EveryCB<V> extends Function3<V, Long, Array<V>, Boolean> {}
}
