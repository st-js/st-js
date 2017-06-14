package org.stjs.javascript;

import org.junit.Test;
import org.stjs.javascript.functions.Function3;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.stjs.javascript.JSCollections.$array;

//============================================
//Tests section 15.4.4.17 of the ECMA-262 Spec
//============================================

public class ArraySomeTest {

	@Test(expected = Error.class)
	public void testSome00() {
		Array<Object> arr = $array();
		arr.some((Function3)null);
	}

	/**
	 * Array.prototype.some considers new elements added to array after it is called
	 */
	@Test
	public void testSome01() {

		final Array<Integer> arr = $array(1, 2);
		// explicitly leaving index 2 (val 3) unset
		arr.$set(3, 4);
		arr.$set(4, 5);

		final AtomicBoolean calledForThree = new AtomicBoolean(false);
		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> obj) {
				arr.$set(2, 3);
				if (value == 3) {
					calledForThree.set(true);
				}

				return false;
			}
		};

		arr.some(callbackfn);

		assertTrue(calledForThree.get());
	}

	/**
	 * Array.prototype.some considers new value of elements in array after it is called
	 */
	@Test
	public void testSome02() {

		final Array<Integer> arr = $array(1, 2, 3, 4, 5);

		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> arr) {
				arr.$set(4, 6);
				return value < 6;
			}
		};

		assertTrue(arr.some(callbackfn));
	}

	/**
	 * Array.prototype.some doesn't visit deleted elements in array after it is called
	 */
	@Test
	public void testSome03() {

		final Array<Integer> arr = $array(1, 2, 3, 4, 5);

		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> arr) {
				arr.$delete(2);
				return value == 3;
			}
		};

		assertFalse(arr.some(callbackfn));
	}

	/**
	 * Array.prototype.some doesn't visit deleted elements when Array.length is decreased
	 */
	@Test
	public void testSome04() {

		final Array<Integer> arr = $array(1, 2, 3, 4, 6);

		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> arr) {
				arr.$length(3);
				return value >= 4;
			}
		};

		assertFalse(arr.some(callbackfn));
	}

	/**
	 * Array.prototype.some doesn't consider newly added elements in sparse array
	 */
	@Test
	public void testSome05() {
		final Array<Integer> arr = new Array<>(10);

		arr.$set(1, 1);
		arr.$set(2, 2);

		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> arr) {
				arr.$set(1000, 5);
				return value >= 5;
			}
		};

		assertFalse(arr.some(callbackfn));
	}

	/**
	 * Array.prototype.some - callbackfn not called for indexes never been assigned values
	 */
	@Test
	public void testSome06() {

		final AtomicInteger callCnt = new AtomicInteger(0);
		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> arr) {
				callCnt.incrementAndGet();
				return false;
			}
		};

		Array<Integer> arr = new Array<>(10);
		arr.$set(1, null);

		arr.some(callbackfn);

		assertEquals(1, callCnt.get());
	}

	/**
	 * Array.prototype.some - element to be retrieved is own data property on an Array
	 */
	@Test
	public void testSome07() {

		final Object kValue = new Object();

		SomeCB<Object> callbackfn = new SomeCB<Object>() {
			@Override
			public Boolean $invoke(Object value, Long index, Array<Object> arr) {
				if (index == 0) {
					return kValue == value;
				}
				return false;
			}
		};

		Array<Object> arr = $array(kValue);

		assertTrue(arr.some(callbackfn));
	}

	/**
	 * Array.prototype.some - callbackfn called with correct parameters
	 */
	@Test
	public void testSome08() {

		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> obj) {
				return !obj.$get(index).equals(value);
			}
		};

		Array<Integer> arr = $array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

		assertFalse(arr.some(callbackfn));
	}

	/**
	 * Array.prototype.some immediately returns true if callbackfn returns true
	 */
	@Test
	public void testSome09() {

		final AtomicInteger callCnt = new AtomicInteger(0);
		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> obj) {
				callCnt.incrementAndGet();
				return index > 5;
			}
		};

		Array<Integer> arr = $array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

		assertTrue(arr.some(callbackfn));
		assertEquals(7, callCnt.get());
	}

	/**
	 * Array.prototype.some - k values are passed in ascending numeric order
	 */
	@Test
	public void testSome10() {

		Array<Integer> arr = $array(0, 1, 2, 3, 4, 5);
		final AtomicInteger lastIdx = new AtomicInteger(0);
		final AtomicInteger called = new AtomicInteger(0);

		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> obj) {
				called.incrementAndGet();
				if (lastIdx.get() != index) {
					return true;

				} else {
					lastIdx.incrementAndGet();
					return false;
				}
			}
		};

		assertFalse(arr.some(callbackfn));
		assertEquals(arr.$length(), called.get());
	}

	/**
	 * Array.prototype.some - k values are accessed during each iteration and not prior to starting the loop
	 */
	@Test
	public void testSome11() {

		final Array<Integer> kIndex = $array();

		// By below way, we could verify that k would be setted as 0, 1, ..., length - 1 in order
		// and each value will be setted one time.
		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> obj) {
				//Each position should be visited one time, which means k is accessed one time during iterations.
				if (kIndex.$get(index) == null) {

					//when current position is visited, its previous index should has been visited.
					if (index != 0 && kIndex.$get(index - 1) == null) {
						return true;
					}
					kIndex.$set(index, 1);
					return false;

				} else {
					return true;
				}
			}
		};

		Array<Integer> arr = $array(11, 12, 13, 14);
		assertFalse(arr.some(callbackfn));
	}

	/**
	 * Array.prototype.some - return value of callbackfn is null
	 */
	@Test
	public void testSome12() {

		final AtomicBoolean accessed = new AtomicBoolean(false);

		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> obj) {
				accessed.set(true);
				return null;
			}
		};

		Array<Integer> arr = new Array<>(2);
		arr.$set(0, 11);

		assertFalse(arr.some(callbackfn));
		assertTrue(accessed.get());
	}

	/**
	 * Array.prototype.some returns false if 'length' is 0 (empty array)
	 */
	@Test
	public void testSome14() {
		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> obj) {
				return false;
			}
		};

		Array<Integer> arr = $array();

		assertFalse(arr.some(callbackfn));
	}

	/**
	 * Array.prototype.some returns false when all calls to callbackfn return false
	 */
	@Test
	public void testSome15() {

		final AtomicInteger callCnt = new AtomicInteger(0);
		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> obj) {
				callCnt.incrementAndGet();
				return false;
			}
		};

		Array<Integer> arr = $array(0,1,2,3,4,5,6,7,8,9);

		assertFalse(arr.some(callbackfn));
		assertEquals(10, callCnt.get());
	}

	/**
	 * Array.prototype.some doesn't visit expandos
	 */
	@Test
	public void testSome16() {

		final AtomicInteger callCnt = new AtomicInteger(0);
		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer value, Long index, Array<Integer> obj) {
				callCnt.incrementAndGet();
				return false;
			}
		};

		Array<Integer> arr = $array(0,1,2,3,4,5,6,7,8,9);
		arr.$set("i", 10);
		arr.$set("true", 11);

		assertFalse(arr.some(callbackfn));
		assertEquals(10, callCnt.get());
	}

	/**
	 * Change the Array store type from sparse to packed during iteration
	 */
	@Test
	public void testSome17(){
		final Array<Integer> arr = new Array<>(1000);
		arr.$set(0, 0);
		arr.$set(999, 999);

		final AtomicInteger callCnt = new AtomicInteger(0);
		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
				if(idx == 0){
					// add lots of elements to force the change of array store
					for(int i = 1; i < 999; i ++){
						arr.$set(i, i);
					}
				}
				return false;
			}
		};

		arr.some(callbackfn);

		assertEquals(1000, callCnt.get());
	}

	/**
	 * Change the Array store type from packed to sparse during iteration
	 */
	@Test
	public void testSome18(){
		final Array<Integer> arr = $array();
		for(int i = 0; i < 1000; i ++){
			arr.$set(i, i);
		}

		final AtomicInteger callCnt = new AtomicInteger(0);
		SomeCB<Integer> callbackfn = new SomeCB<Integer>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
				if(idx == 0){
					// remove lots of elements to force the change of array store
					for(int i = 1; i < 999; i ++){
						arr.$delete(i);
					}
				}
				return false;
			}
		};

		arr.some(callbackfn);

		assertEquals(2, callCnt.get()); // only 0 and 999 remain
	}

	private static interface SomeCB<V> extends Function3<V, Long, Array<V>, Boolean> {}
}
