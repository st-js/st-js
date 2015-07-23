package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.stjs.javascript.JSCollections.$array;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.stjs.javascript.functions.Function4;

public class ArrayReduceRightTest {

	/**
	 * es5id: 15.4.4.22-4-3
	 * description: Array.prototype.reduceRight throws TypeError if callbackfn is null
	 */
	@Test(expected = Error.class)
	public void testReduceRight01() {
		new Array<Integer>(10).reduceRight(null);
	}

	/**
	 * es5id: 15.4.4.22-5-1
	 * description: Array.prototype.reduceRight throws TypeError if 'length' is 0 (empty array), no initVal
	 */
	@Test(expected = Error.class)
	public void testReduceRight02() {
		new Array<Integer>().reduceRight(nullCallback(Integer.class));
	}

	/**
	 * es5id: 15.4.4.22-7-1
	 * description: Array.prototype.reduceRight returns initialValue if 'length' is 0
	 * and initialValue is present (empty array)
	 */
	@Test
	public void testReduceRight03() {
		assertEquals(1, new Array<Integer>().reduceRight(nullCallback(Integer.class), 1).intValue());
	}

	/**
	 * es5id: 15.4.4.22-7-11
	 * description: Array.prototype.reduceRight - 'initialValue' is not present
	 */
	@Test
	public void testReduceRight04() {
		String str = "initialValue is not present";

		assertEquals(str, new Array<>(str).reduceRight(nullCallback(String.class)));
	}

	/**
	 * es5id: 15.4.4.22-8-c-1
	 * description: Array.prototype.reduceRight throws TypeError when Array is empty
	 * and initialValue is not present but length > 0
	 */
	@Test(expected = Error.class)
	public void testReduceRight05() {
		new Array<String>(10).reduceRight(nullCallback(String.class));
	}

	/**
	 * es5id: 15.4.4.22-8-c-2
	 * description:  Array.prototype.reduceRight throws TypeError when elements
	 * assigned values are deleted by reducign array length and
	 * initialValue is not present
	 */
	@Test(expected = Error.class)
	public void testReduceRight06() {
		Array<Integer> arr = new Array<>(10);
		arr.$set(9, 1);
		arr.$length(5);

		arr.reduceRight(nullCallback(Integer.class));
	}

	/**
	 * es5id: 15.4.4.22-8-c-3
	 * description: Array.prototype.reduceRight throws TypeError when elements
	 * assigned values are deleted and initialValue is not present
	 */
	@Test(expected = Error.class)
	public void testReduceRight07() {
		Array<Integer> arr = $array(1, 2, 3, 4, 5);
		arr.$delete(0);
		arr.$delete(1);
		arr.$delete(2);
		arr.$delete(3);
		arr.$delete(4);

		arr.reduceRight(nullCallback(Integer.class));
	}

	/**
	 * es5id: 15.4.4.22-9-1
	 * description: Array.prototype.reduceRight doesn't consider new elements which
	 * index is larger than array original length added to array after it
	 * is called, consider new elements which index is smaller than array length
	 */
	@Test
	public void testReduceRight08() {
		final Array<String> arr = $array("1", "2", null, "4", "5");

		Function4<String, String, Long, Array<String>, String> callbackfn =
				new Function4<String, String, Long, Array<String>, String>() {
					@Override
					public String $invoke(String prevVal, String curVal, Long idx, Array<String> obj) {
						arr.$set(5, "6");
						arr.$set(2, "3");
						return prevVal + curVal;
					}
				};

		assertEquals("54321", arr.reduceRight(callbackfn));
	}

	/**
	 * es5id: 15.4.4.22-9-2
	 * description: Array.prototype.reduceRight considers new value of elements in
	 * array after it is called
	 */
	@Test
	public void testReduceRight09() {
		final Array<Integer> arr = $array(1, 2, 3, 4, 5);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						arr.$set(3, -2);
						arr.$set(0, -1);
						return prevVal + curVal;
					}
				};

		assertEquals(13, arr.reduceRight(callbackfn).intValue());
	}

	/**
	 * es5id: 15.4.4.22-9-3
	 * description: Array.prototype.reduceRight doesn't consider unvisited deleted
	 * elements in array after the call
	 */
	@Test
	public void testReduceRight10() {
		final Array<String> arr = $array("1", "2", "3", "4", "5");

		Function4<String, String, Long, Array<String>, String> callbackfn =
				new Function4<String, String, Long, Array<String>, String>() {
					@Override
					public String $invoke(String prevVal, String curVal, Long idx, Array<String> obj) {
						arr.$delete(1);
						arr.$delete(4);
						return prevVal + curVal;
					}
				};

		assertEquals("5431", arr.reduceRight(callbackfn)); // two elements deleted
	}

	/**
	 * es5id: 15.4.4.22-9-4
	 * description: Array.prototype.reduceRight doesn't consider unvisited deleted
	 * elements when Array.length is decreased
	 */
	@Test
	public void testReduceRight11() {
		final Array<String> arr = $array("1", "2", "3", "4", "5");

		Function4<String, String, Long, Array<String>, String> callbackfn =
				new Function4<String, String, Long, Array<String>, String>() {
					@Override
					public String $invoke(String prevVal, String curVal, Long idx, Array<String> obj) {
						arr.$length(2);
						return prevVal + curVal;
					}
				};

		assertEquals("5421", arr.reduceRight(callbackfn)); // two elements deleted
	}

	/**
	 * es5id: 15.4.4.22-9-5
	 * description: Array.prototype.reduceRight - callbackfn not called for array with
	 * one element
	 */
	@Test
	public void testReduceRight12() {
		final AtomicInteger callCnt = new AtomicInteger(0);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						callCnt.incrementAndGet();
						return 2;
					}
				};

		Array<Integer> arr = $array(1);
		assertEquals(1, arr.reduceRight(callbackfn).intValue());
		assertEquals(0, callCnt.get());
	}

	/**
	 * es5id: 15.4.4.22-9-c-1
	 * description: Array.prototype.reduceRight - callbackfn not called for indexes
	 * never been assigned values
	 */
	@Test
	public void testReduceRight13() {

		final AtomicInteger callCnt = new AtomicInteger(0);
		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						callCnt.incrementAndGet();
						return curVal;
					}
				};

		Array<Integer> arr = new Array<>(10);
		//explicitly assigning a value
		arr.$set(0, null);
		arr.$set(1, null);

		assertEquals(null, arr.reduceRight(callbackfn));
		assertEquals(1, callCnt.get());
	}

	/**
	 * es5id: 15.4.4.22-9-c-ii-1
	 * description: Array.prototype.reduceRight - callbackfn called with correct
	 * parameters (initialvalue not passed)
	 */
	@Test
	public void testReduceRight14() {

		Function4<Object, Object, Long, Array<Object>, Object> callbackfn =
				new Function4<Object, Object, Long, Array<Object>, Object>() {
					@Override
					public Object $invoke(Object prevVal, Object curVal, Long idx, Array<Object> obj) {
						if (idx + 1 < obj.$length() && obj.$get(idx) == curVal && obj.$get(idx + 1) == prevVal) {
							return curVal;
						} else {
							return false;
						}
					}
				};

		Array<Object> arr = $array(0, 1, true, null, new Object(), "five");

		assertEquals(0, arr.reduceRight(callbackfn));
	}

	/**
	 * es5id: 15.4.4.22-9-c-ii-2
	 * description: Array.prototype.reduceRight - callbackfn called with correct
	 * parameters (initialvalue passed)
	 */
	@Test
	public void testReduceRight15() {

		final AtomicBoolean parCorrect = new AtomicBoolean(false);
		Array<Object> arr = $array(0, 1, true, null, new Object(), "five");
		final Double initialValue = 5.5;

		Function4<Object, Object, Long, Array<Object>, Object> callbackfn =
				new Function4<Object, Object, Long, Array<Object>, Object>() {
					@Override
					public Object $invoke(Object prevVal, Object curVal, Long idx, Array<Object> obj) {
						if (idx == obj.$length() - 1 && obj.$get(idx) == curVal && prevVal == initialValue) {
							return curVal;

						} else if (idx + 1 < obj.$length() && obj.$get(idx) == curVal && obj.$get(idx + 1) == prevVal) {
							return curVal;

						} else {
							return false;
						}
					}
				};

		assertEquals(0, arr.reduceRight(callbackfn, initialValue));
	}

	/**
	 * es5id: 15.4.4.22-9-c-ii-4
	 * description: Array.prototype.reduceRight - k values are passed in descending numeric order on an Array
	 */
	@Test
	public void testReduceRight16() {

		final Array<Integer> arr = $array(0, 1, 2, 3, 4, 5);
		final AtomicInteger lastIdx = new AtomicInteger(arr.$length() - 1);
		final AtomicBoolean result = new AtomicBoolean(true);
		final AtomicBoolean accessed = new AtomicBoolean(false);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						accessed.set(true);
						if (lastIdx.get() != idx) {
							result.set(false);
						} else {
							lastIdx.decrementAndGet();
						}
						return null;
					}
				};

		arr.reduceRight(callbackfn, 1);
		assertTrue(result.get());
		assertTrue(accessed.get());
	}

	/**
	 * es5id: 15.4.4.22-9-c-ii-5
	 * description: Array.prototype.reduceRight - k values are accessed during each
	 * iteration and not prior to starting the loop on an Array
	 */
	@Test
	public void testReduceRight17() {
		final Array<Integer> arr = $array(11, 12, 13, 14);
		final AtomicBoolean result = new AtomicBoolean(true);
		final Integer[] kIndex = new Integer[4];
		final AtomicInteger called = new AtomicInteger(0);

		//By below way, we could verify that k would be setted as 0, 1, ..., length - 1 in order, and each value will be setted one time.
		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						//Each position should be visited one time, which means k is accessed one time during iterations.
						called.incrementAndGet();
						if (kIndex[idx.intValue()] == null) {
							//when current position is visited, its previous index should has been visited.
							if (idx != arr.$length() - 1 && kIndex[idx.intValue() + 1] == null) {
								result.set(false);
							}
							kIndex[idx.intValue()] = 1;
						} else {
							result.set(false);
						}
						return null;
					}
				};

		arr.reduceRight(callbackfn, 1);
		assertTrue(result.get());
		assertEquals(4, called.get());
	}

	/**
	 * es5id: 15.4.4.22-9-c-ii-7
	 * description: Array.prototype.reduceRight - unhandled exceptions happened in callbackfn terminate iteration
	 */
	@Test
	public void testReduceRight18() {
		final AtomicBoolean accessed = new AtomicBoolean(false);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						if (idx < 10) {
							accessed.set(true);
						}
						if (idx == 10) {
							throw new Error("Exception occurred in callbackfn");
						}
						return null;
					}
				};

		Array<Integer> obj = new Array<>(20);
		obj.$set(0, 11);
		obj.$set(4, 10);
		obj.$set(10, 8);

		try {
			obj.reduceRight(callbackfn, 1);
			throw new AssertionError("This point should not be reached");

		}
		catch (Error e) {
			// we should arrive here
			assertFalse(accessed.get());
		}
	}

	/**
	 * es5id: 15.4.4.22-9-c-ii-16
	 * description: Array.prototype.reduceRight - non-indexed properties are not called
	 */
	@Test
	public void testReduceRight19() {

		final AtomicBoolean result = new AtomicBoolean(false);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						if ((prevVal != null && prevVal == 8) ||
								(curVal != null && curVal == 8)) {
							result.set(true);
						}
						return null;
					}
				};

		Array<Integer> obj = new Array<>(20);
		obj.$set(0, 11);
		obj.$set(10, 12);
		obj.$set("non_index_property", 8);

		obj.reduceRight(callbackfn, 1);

		assertFalse(result.get());
	}

	/**
	 * es5id: 15.4.4.22-9-c-ii-17
	 * description: Array.prototype.reduceRight - 'accumulator' used for current iteration
	 * is the result of previous iteration on an Array
	 */
	@Test
	public void testReduceRight20() {

		final Array<Integer> arr = $array(11, 12, 13);
		final AtomicBoolean result = new AtomicBoolean(true);
		final Integer initVal = 6;
		final AtomicInteger preResult = new AtomicInteger(initVal);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						if (prevVal != preResult.get()) {
							result.set(false);
						}
						preResult.set(curVal);
						return curVal;
					}
				};

		arr.reduceRight(callbackfn, initVal);

		assertTrue(result.get());
	}

	/**
	 * es5id: 15.4.4.22-9-c-ii-19
	 * description: Array.prototype.reduceRight - value of 'accumulator' used for
	 * first iteration is the value of max index property which is not
	 * undefined when 'initialValue' is not present on an Array
	 */
	@Test
	public void testReduceRight21() {

		Array<Integer> arr = $array(11, 12, 13);
		final AtomicBoolean testResult = new AtomicBoolean(false);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						if (idx == 1) {
							testResult.set(prevVal == 13);
						}
						return curVal;
					}
				};

		arr.reduceRight(callbackfn);

		assertTrue(testResult.get());
	}

	/**
	 * es5id: 15.4.4.22-10-1
	 * description: Array.prototype.reduceRight doesn't mutate the Array on which it
	 * is called on
	 */
	@Test
	public void testReduceRight22() {
		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						return 1;
					}
				};

		Array<Integer> srcArr = $array(1, 2, 3, 4, 5);
		srcArr.reduceRight(callbackfn);

		assertEquals(1, srcArr.$get(0).intValue());
		assertEquals(2, srcArr.$get(1).intValue());
		assertEquals(3, srcArr.$get(2).intValue());
		assertEquals(4, srcArr.$get(3).intValue());
		assertEquals(5, srcArr.$get(4).intValue());
	}

	/**
	 * es5id: 15.4.4.22-10-5
	 * description: Array.prototype.reduceRight reduces array in descending order of
	 * indices(initialvalue present)
	 */
	@Test
	public void testReduceRight23() {

		Function4<String, String, Long, Array<String>, String> callbackfn =
				new Function4<String, String, Long, Array<String>, String>() {
					@Override
					public String $invoke(String prevVal, String curVal, Long idx, Array<String> obj) {
						return prevVal + curVal;
					}
				};

		Array<String> srcArr = $array("1", "2", "3", "4", "5");

		assertEquals("654321", srcArr.reduceRight(callbackfn, "6"));
	}

	private static <T> Function4<T, T, Long, Array<T>, T> nullCallback(Class<T> type) {
		return new Function4<T, T, Long, Array<T>, T>() {
			@Override
			public T $invoke(T prevVal, T curVal, Long idx, Array<T> obj) {
				return null;
			}
		};
	}
}
