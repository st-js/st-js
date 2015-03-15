package org.stjs.javascript;

import org.junit.Test;
import org.stjs.javascript.functions.Callback3;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.stjs.javascript.JSCollections.$array;

//============================================
//Tests section 15.4.4.18 of the ECMA-262 Spec
//============================================

public class ArrayForEachTest {

	/**
	 * Array.prototype.forEach throws TypeError if callbackfn is null
	 */
	@Test(expected = Error.class)
	public void testForEach00() {
		Array<Object> arr = new Array<>(10);
		arr.forEach((ForEachCB<Object>) null);
	}

	/**
	 * Array.prototype.forEach - 'callbackfn' is a function
	 */
	@Test
	public void testForEach01() {

		final AtomicBoolean accessed = new AtomicBoolean(false);
		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>(){
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				accessed.set(true);
			}
		};

		$array(11, 9).forEach(callbackfn);

		assertTrue(accessed.get());
	}

	/**
	 * Array.prototype.forEach doesn't consider new elements added to array after the call
	 */
	@Test
	public void testForEach02() {

		final Array<Integer> arr = $array(1,2);
		// arr[2] intentionally left unset
		arr.$set(3, 4);
		arr.$set(4, 5);

		final AtomicInteger callCnt = new AtomicInteger(0);
		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
				arr.$set(2, 3);
				arr.$set(5, 6);
			}
		};

		arr.forEach(callbackfn);

		assertEquals(5, callCnt.get());
	}

	/**
	 * Array.prototype.forEach doesn't visit deleted elements in array after the call
	 */
	@Test
	public void testForEach03() {

		final Array<Integer> arr = $array(1,2,3,4,5);
		final AtomicInteger callCnt = new AtomicInteger(0);
		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				if (callCnt.get() == 0) {
					arr.$delete(3);
				}
				callCnt.incrementAndGet();
			}
		};

		arr.forEach(callbackfn);

		assertEquals(4, callCnt.get());
	}

	/**
	 * Array.prototype.forEach doesn't visit deleted elements when Array.length is decreased
	 */
	@Test
	public void testForEach04() {

		final Array<Integer> arr = $array(1,2,3,4,5);
		final AtomicInteger callCnt = new AtomicInteger(0);
		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				arr.$length(3);
				callCnt.incrementAndGet();
			}
		};

		arr.forEach(callbackfn);

		assertEquals(3, callCnt.get());
	}

	/**
	 * Array.prototype.forEach doesn't consider newly added elements in sparse array
	 */
	@Test
	public void testForEach05() {

		final Array<Integer> arr = new Array<>(10);

		final AtomicInteger callCnt = new AtomicInteger(0);
		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				arr.$set(1000, 3);
				callCnt.incrementAndGet();
			}
		};

		arr.$set(1, 1);
		arr.$set(2, 2);

		arr.forEach(callbackfn);

		assertEquals(2, callCnt.get());
	}

	/**
	 * Array.prototype.forEach - considers new value of elements in array after the call
	 */
	@Test
	public void testForEach06() {

		final AtomicBoolean result = new AtomicBoolean(false);
		final Array<Integer> arr = $array(1, 2, 3, 4, 5);

		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				arr.$set(4, 6);
				if (val >= 6) {
					result.set(true);
				}
			}
		};

		arr.forEach(callbackfn);

		assertTrue(result.get());
	}

	/**
	 * Array.prototype.forEach - callbackfn not called for indexes never been assigned values
	 */
	@Test
	public void testForEach07() {

		final AtomicInteger callCnt = new AtomicInteger(0);
		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
			}
		};

		Array<Integer> arr = new Array<>(10);
		arr.$set(1, null);

		arr.forEach(callbackfn);

		assertEquals(1, callCnt.get()); // callbackfn was never called for index 0
	}

	/**
	 * Array.prototype.forEach - element to be retrieved is own data property on an Array
	 */
	@Test
	public void testForEach08() {

		final AtomicBoolean testResult = new AtomicBoolean(false);

		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				if (idx == 0) {
					testResult.set(val == 11);
				}
			}
		};

		$array(11).forEach(callbackfn);

		assertTrue(testResult.get());
	}

	/**
	 *Array.prototype.forEach - callbackfn called with correct parameters
	 */
	@Test
	public void testForEach09() {

		final AtomicBoolean parametersCorrect = new AtomicBoolean(true);
		final AtomicBoolean called = new AtomicBoolean(false);
		ForEachCB<Object> callbackfn = new ForEachCB<Object>() {
			@Override
			public void $invoke(Object val, Long idx, Array<Object> obj) {
				called.set(true);
				if (obj.$get(idx) != val) {
					parametersCorrect.set(false);
				}
			}
		};

		Array<Object> arr = $array(0,1,true,null,new Object(),"five");
		arr.$set(999999, -6.6);

		arr.forEach(callbackfn);

		assertTrue(called.get());
		assertTrue(parametersCorrect.get());
	}

	/**
	 * Array.prototype.forEach - k values are passed in ascending numeric order
	 */
	@Test
	public void testForEach10() {

		Array<Integer> arr = $array(0, 1, 2, 3, 4, 5);
		final AtomicInteger lastIdx = new AtomicInteger(0);
		final AtomicInteger called = new AtomicInteger(0);
		final AtomicBoolean result = new AtomicBoolean(true);

		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				called.incrementAndGet();
				if (lastIdx.get() != idx) {
					result.set(false);
				} else {
					lastIdx.incrementAndGet();
				}
			}
		};

		arr.forEach(callbackfn);

		assertTrue(result.get());
		assertEquals(arr.$length(), called.get());
	}

	/**
	 * Array.prototype.forEach - k values are accessed during each iteration and not prior to starting the loop on an Array
	 */
	@Test
	public void testForEach11() {

		final AtomicBoolean result = new AtomicBoolean(true);
		final Array<Integer> kIndex = $array();

		// By below way, we could verify that k would be setted as 0, 1, ..., length - 1 in order,
		// and each value will be setted one time.
		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				//Each position should be visited one time, which means k is accessed one time during iterations.
				if (kIndex.$get(idx) == null
						){
					//when current position is visited, its previous index should has been visited.
					if (idx != 0 && kIndex.$get(idx - 1) == null){
						result.set(false);
					}
					kIndex.$set(idx, 1);

				}else{
					result.set(false);
				}
			}
		};

		$array(11, 12, 13, 14).forEach(callbackfn);

		assertTrue(result.get());
	}

	/**
	 * Array.prototype.forEach - unhandled exceptions happened in callbackfn terminate iteration
	 */
	@Test
	public void testForEach12() {

		final AtomicBoolean accessed = new AtomicBoolean(false);

		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				if (idx > 0) {
					accessed.set(true);
				}
				if (idx == 0) {
					throw new Error("Exception occurred in callbackfn");
				}
			}
		};

		Array<Integer> arr = new Array<>(20);
		arr.$set(0, 11);
		arr.$set(4, 10);
		arr.$set(10, 8);

		Error err = null;
		try {
			arr.forEach(callbackfn);
		} catch (Error e) {
			err = e;
		}

		assertFalse(accessed.get());
		assertNotNull(err);
	}

	/**
	 * Array.prototype.forEach doesn't call callbackfn if 'length' is 0 (empty array)
	 */
	@Test
	public void testForEach13() {

		final AtomicInteger callCnt = new AtomicInteger(0);
		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
			}
		};

		Array<Integer> arr = $array();
		arr.forEach(callbackfn);

		assertEquals(0, callCnt.get());
	}

	/**
	 * Array.prototype.forEach doesn't visit expandos
	 */
	@Test
	public void testForEach14() {

		final AtomicInteger callCnt = new AtomicInteger(0);
		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
			}
		};

		Array<Integer> arr = $array(1,2,3,4,5);
		arr.$set("i", 10);
		arr.$set("true", 11);

		arr.forEach(callbackfn);

		assertEquals(5, callCnt.get());
	}

	/**
	 * Change the Array store type from sparse to packed during iteration
	 */
	@Test
	public void testForEach15(){
		final Array<Integer> arr = new Array<>(1000);
		arr.$set(0, 0);
		arr.$set(999, 999);

		final AtomicInteger callCnt = new AtomicInteger(0);
		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
				if(idx == 0){
					// add lots of elements to force the change of array store
					for(int i = 1; i < 999; i ++){
						arr.$set(i, i);
					}
				}
			}
		};

		arr.forEach(callbackfn);

		assertEquals(1000, callCnt.get());
	}

	/**
	 * Change the Array store type from packed to sparse during iteration
	 */
	@Test
	public void testForEach16(){
		final Array<Integer> arr = $array();
		for(int i = 0; i < 1000; i ++){
			arr.$set(i, i);
		}

		final AtomicInteger callCnt = new AtomicInteger(0);
		ForEachCB<Integer> callbackfn = new ForEachCB<Integer>() {
			@Override
			public void $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
				if(idx == 0){
					// remove lots of elements to force the change of array store
					for(int i = 1; i < 999; i ++){
						arr.$delete(i);
					}
				}
			}
		};

		arr.forEach(callbackfn);

		assertEquals(2, callCnt.get()); // only 0 and 999 remain
	}


	public interface ForEachCB<V> extends Callback3<V, Long, Array<V>> {}

}
