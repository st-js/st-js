package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.stjs.javascript.JSCollections.$array;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.stjs.javascript.functions.Function4;

public class ArrayReduceTest {

	/**
	 * es5id: 15.4.4.21-4-3
	 * description: Array.prototype.reduce throws TypeError if callbackfn is null
	 */
	@Test(expected = Error.class)
	public void testReduce01() {
		new Array<Integer>(10).reduce((Function4)null);
	}

	/**
	 * es5id: 15.4.4.21-4-12
	 * description: Array.prototype.reduce - 'callbackfn' is a function
	 */
	@Test
	public void testReduce02() {
		final AtomicBoolean accessed = new AtomicBoolean(false);
		Function4<Boolean, Integer, Long, Array<Integer>, Boolean> callbackfn =
				new Function4<Boolean, Integer, Long, Array<Integer>, Boolean>() {
					@Override
					public Boolean $invoke(Boolean prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						accessed.set(true);
						return curVal > 10;
					}
				};

		assertFalse($array(11, 9).reduce(callbackfn, true));
		assertTrue(accessed.get());
	}

	/**
	 * es5id: 15.4.4.21-5-1
	 * description: Array.prototype.reduce throws TypeError if 'length' is 0 (empty array), no initVal
	 */
	@Test(expected = Error.class)
	public void testReduce03() {
		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						return 0;
					}
				};

		new Array<Integer>().reduce(callbackfn);
	}

	/**
	 * es5id: 15.4.4.21-5-9
	 * description: Array.prototype.reduce - 'initialValue' is returned if 'len' is 0 and 'initialValue' is present
	 */
	@Test
	public void testReduce04() {

		final AtomicBoolean accessed = new AtomicBoolean(false);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						accessed.set(true);
						return 0;
					}
				};

		assertEquals(3, new Array<Integer>().reduce(callbackfn, 3).intValue());
		assertFalse(accessed.get());
	}

	/**
	 * es5id: 15.4.4.21-8-c-2
	 * description: Array.prototype.reduce throws TypeError when elements assigned values are deleted by reducing array length
	 * and initialValue is not present
	 */
	@Test(expected = Error.class)
	public void testReduce05() {

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						return 0;
					}
				};

		Array<Integer> arr = new Array<>(10);
		arr.$set(9, 1);
		arr.$length(5);

		arr.reduce(callbackfn);
	}

	/**
	 * es5id: 15.4.4.21-8-c-3
	 * description: Array.prototype.reduce throws TypeError when elements assigned
	 * values are deleted and initialValue is not present
	 */
	@Test(expected = Error.class)
	public void testReduce06() {

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						return 0;
					}
				};

		Array<Integer> arr = $array(1, 2, 3, 4, 5);
		arr.$delete(0);
		arr.$delete(1);
		arr.$delete(2);
		arr.$delete(3);
		arr.$delete(4);

		arr.reduce(callbackfn);
	}

	/**
	 * es5id: 15.4.4.21-9-1
	 * description: Array.prototype.reduce doesn't consider new elements added to array after it is called
	 */
	@Test
	public void testReduce07() {
		final Array<Integer> arr = $array(1, 2, null, 4, 5);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						arr.$set(5, 6);
						arr.$set(2, 3);
						return prevVal + curVal;
					}
				};

		assertEquals(15, arr.reduce(callbackfn).intValue());
	}

	/**
	 * es5id: 15.4.4.21-9-2
	 * description: Array.prototype.reduce considers new value of elements in array after it is called
	 */
	@Test
	public void testReduce08() {
		final Array<Integer> arr = $array(1, 2, 3, 4, 5);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						arr.$set(3, -2);
						arr.$set(4, -1);
						return prevVal + curVal;
					}
				};

		assertEquals(3, arr.reduce(callbackfn).intValue());
	}

	/**
	 * es5id: 15.4.4.21-9-3
	 * description: Array.prototype.reduce doesn't visit deleted elements in array after the call
	 */
	@Test
	public void testReduce09() {
		final Array<Integer> arr = $array(1, 2, 3, 4, 5);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						arr.$delete(3);
						arr.$delete(4);
						return prevVal + curVal;
					}
				};

		assertEquals(6, arr.reduce(callbackfn).intValue());
	}

	/**
	 * es5id: 15.4.4.21-9-4
	 * description: Array.prototype.reduce doesn't visit deleted elements when Array.length is decreased
	 */
	@Test
	public void testReduce10() {
		final Array<Integer> arr = $array(1, 2, 3, 4, 5);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						arr.$length(2);
						return prevVal + curVal;
					}
				};

		assertEquals(3, arr.reduce(callbackfn).intValue());
	}

	/**
	 * es5id: 15.4.4.21-9-5
	 * description: Array.prototype.reduce - callbackfn not called for array with one element
	 */
	@Test
	public void testReduce11() {
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
		assertEquals(1, arr.reduce(callbackfn).intValue());
		assertEquals(0, callCnt.get());
	}

	/**
	 * es5id: 15.4.4.21-9-c-1
	 * description: Array.prototype.reduce - callbackfn not called for indexes never been assigned values
	 */
	@Test
	public void testReduce12() {
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
		arr.$set(0, null);
		arr.$set(1, null);

		assertEquals(null, arr.reduce(callbackfn));
		assertEquals(1, callCnt.get());
	}

	/**
	 * es5id: 15.4.4.21-9-c-ii-1
	 * description: Array.prototype.reduce - callbackfn called with correct parameters (initialvalue not passed)
	 */
	@Test
	public void testReduce13() {
		Function4<Object, Object, Long, Array<Object>, Object> callbackfn = new Function4<Object, Object, Long, Array<Object>, Object>() {
			@Override
			public Object $invoke(Object prevVal, Object curVal, Long idx, Array<Object> obj) {
				if (idx > 0 && obj.$get(idx) == curVal && obj.$get(idx - 1) == prevVal) {
					return curVal;
				} else {
					return false;
				}
			}
		};

		Array<Object> arr = $array(0, 1, true, null, new Object(), "five");
		assertEquals("five", arr.reduce(callbackfn));
	}

	/**
	 * es5id: 15.4.4.21-9-c-ii-2
	 * description: Array.prototype.reduce - callbackfn called with correct parameters (initialvalue passed)
	 */
	@Test
	public void testReduce14() {
		final Object initialValue = 5.5;
		Function4<Object, Object, Long, Array<Object>, Object> callbackfn = new Function4<Object, Object, Long, Array<Object>, Object>() {
			@Override
			public Object $invoke(Object prevVal, Object curVal, Long idx, Array<Object> obj) {
				if (idx == 0 && obj.$get(idx) == curVal && prevVal == initialValue) {
					return curVal;
				} else if (idx > 0 && obj.$get(idx) == curVal && obj.$get(idx - 1) == prevVal) {
					return curVal;
				} else {
					return false;
				}
			}
		};

		Array<Object> arr = $array(0, 1, true, null, new Object(), "five");
		assertEquals("five", arr.reduce(callbackfn, initialValue));
	}

	/**
	 * es5id: 15.4.4.21-9-c-ii-4
	 * description: Array.prototype.reduce - k values are passed in acending numeric order on an Array
	 */
	@Test
	public void testReduce15() {

		final Array<Integer> arr = $array(0, 1, 2);
		final AtomicInteger lastIdx = new AtomicInteger(0);
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
							lastIdx.incrementAndGet();
						}
						return null;
					}
				};

		arr.reduce(callbackfn, 11);
		assertTrue(result.get());
		assertTrue(accessed.get());
	}

	/**
	 * es5id: 15.4.4.21-9-c-ii-7
	 * description: Array.prototype.reduce - unhandled exceptions happened in callbackfn terminate iteration
	 */
	@Test
	public void testReduce16() {
		final AtomicBoolean accessed = new AtomicBoolean(false);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						if (idx > 0) {
							accessed.set(true);
						}
						if (idx == 0) {
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
			obj.reduce(callbackfn, 1);
			throw new AssertionError("This point should not be reached");

		}
		catch (Error e) {
			// we should arrive here
			assertFalse(accessed.get());
		}
	}

	/**
	 * es5id: 15.4.4.21-9-c-ii-5
	 * description: Array.prototype.reduce - k values are accessed during each
	 * iteration and not prior to starting the loop on an Array
	 */
	@Test
	public void testReduce17() {
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
							if (idx != 0 && kIndex[idx.intValue() - 1] == null) {
								result.set(false);
							}
							kIndex[idx.intValue()] = 1;
						} else {
							result.set(false);
						}
						return null;
					}
				};

		$array(11, 12, 13, 14).reduce(callbackfn, 1);

		assertTrue(result.get());
		assertEquals(4, called.get());
	}

	/**
	 * es5id: 15.4.4.21-9-c-ii-16
	 * description: Array.prototype.reduce - non-indexed properties are not called
	 */
	@Test
	public void testReduce18() {

		final AtomicBoolean accessed = new AtomicBoolean(false);
		final AtomicBoolean result1 = new AtomicBoolean(true);
		final AtomicBoolean result2 = new AtomicBoolean(true);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						accessed.set(true);
						if (curVal != null && curVal == 8) {
							result1.set(false);
						}

						if (prevVal != null && prevVal == 8) {
							result2.set(false);
						}
						return null;
					}
				};

		Array<Integer> obj = new Array<>(20);
		obj.$set(0, 11);
		obj.$set(10, 12);
		obj.$set("non_index_property", 8);

		obj.reduce(callbackfn, 1);

		assertTrue(result1.get());
		assertTrue(result2.get());
		assertTrue(accessed.get());
	}

	/**
	 * es5id: 15.4.4.21-9-c-ii-17
	 * description: Array.prototype.reduce - 'accumulator' used for current iteration
	 * is the result of previous iteration on an Array
	 */
	@Test
	public void testReduce19() {

		final AtomicBoolean result = new AtomicBoolean(true);
		final AtomicBoolean accessed = new AtomicBoolean(false);
		final AtomicInteger preIteration = new AtomicInteger(1);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						accessed.set(true);
						if (preIteration.get() != prevVal) {
							result.set(false);
						}
						preIteration.set(curVal);
						return curVal;
					}
				};

		$array(11, 12, 13).reduce(callbackfn, 1);

		assertTrue(result.get());
		assertTrue(accessed.get());
	}

	/**
	 * es5id: 15.4.4.21-9-c-ii-19
	 * description: Array.prototype.reduce - value of 'accumulator' used for first
	 * iteration is the value of least index property which is not
	 * undefined when 'initialValue' is not present on an Array
	 */
	@Test
	public void testReduce20() {

		final AtomicInteger called = new AtomicInteger(0);
		final AtomicBoolean result = new AtomicBoolean(false);

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						called.incrementAndGet();
						if (idx == 1) {
							result.set(prevVal == 11 && curVal == 9);
						}
						return null;
					}
				};

		$array(11, 9).reduce(callbackfn);

		assertTrue(result.get());
		assertEquals(1, called.get());
	}

	/**
	 * es5id: 15.4.4.21-10-1
	 * description: Array.prototype.reduce doesn't mutate the Array on which it is called on
	 */
	@Test
	public void testReduce21() {

		Function4<Integer, Integer, Long, Array<Integer>, Integer> callbackfn =
				new Function4<Integer, Integer, Long, Array<Integer>, Integer>() {
					@Override
					public Integer $invoke(Integer prevVal, Integer curVal, Long idx, Array<Integer> obj) {
						return 1;
					}
				};

		Array<Integer> srcArr = $array(1, 2, 3, 4, 5);
		srcArr.reduce(callbackfn);

		assertEquals(1, srcArr.$get(0).intValue());
		assertEquals(2, srcArr.$get(1).intValue());
		assertEquals(3, srcArr.$get(2).intValue());
		assertEquals(4, srcArr.$get(3).intValue());
		assertEquals(5, srcArr.$get(4).intValue());
	}

	/**
	 * es5id: 15.4.4.21-10-2
	 * description: Array.prototype.reduce reduces the array in ascending order of indices
	 */
	@Test
	public void testReduce22() {
		Function4<String, String, Long, Array<String>, String> callbackfn = new Function4<String, String, Long, Array<String>, String>() {
			@Override
			public String $invoke(String prevVal, String curVal, Long idx, Array<String> obj) {
				return prevVal + curVal;
			}
		};

		Array<String> srcArr = $array("1", "2", "3", "4", "5");

		assertEquals("12345", srcArr.reduce(callbackfn));
	}

	/**
	 * es5id: 15.4.4.21-10-5
	 * description: Array.prototype.reduce reduces the array in ascending order of indices(initialvalue present)
	 */
	@Test
	public void testReduce23() {
		Function4<String, String, Long, Array<String>, String> callbackfn = new Function4<String, String, Long, Array<String>, String>() {
			@Override
			public String $invoke(String prevVal, String curVal, Long idx, Array<String> obj) {
				return prevVal + curVal;
			}
		};

		Array<String> srcArr = $array("1", "2", "3", "4", "5");

		assertEquals("012345", srcArr.reduce(callbackfn, "0"));
	}
}
