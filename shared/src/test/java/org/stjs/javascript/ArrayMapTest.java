package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.stjs.javascript.JSCollections.$array;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.stjs.javascript.functions.Callback3;
import org.stjs.javascript.functions.Function3;

public class ArrayMapTest {

	/**
	 * es5id: 15.4.4.19-2-2
	 * description: Array.prototype.map - when 'length' is own data property on an Array
	 */
	@Test
	public void testMap01() {
		Array<Boolean> testResult = $array(12, 11).map(new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				return val > 10;
			}
		});
		assertEquals(2, testResult.$length());
	}

	/**
	 * es5id: 15.4.4.19-4-3
	 * description: Array.prototype.map throws TypeError if callbackfn is null
	 */
	@Test(expected = Error.class)
	public void testMap02() {
		new Array<>(10).map(null);
	}

	/**
	 * es5id: 15.4.4.19-8-1
	 * description: Array.prototype.map doesn't consider new elements added to array after it is called
	 */
	@Test
	public void testMap03() {
		final Array<Integer> srcArr = $array(1, 2, null, 4, 5);

		Function3<Integer, Long, Array<Integer>, Integer> callbackfn = new Function3<Integer, Long, Array<Integer>, Integer>() {
			@Override
			public Integer $invoke(Integer val, Long idx, Array<Integer> obj) {
				srcArr.$set(2, 3);
				srcArr.$set(5, 6);
				return 1;
			}
		};

		Array<Integer> resArr = srcArr.map(callbackfn);
		assertEquals(5, resArr.$length());
	}

	/**
	 * es5id: 15.4.4.19-8-2
	 * description: Array.prototype.map considers new value of elements in array after it is called
	 */
	@Test
	public void testMap04() {
		final Array<Integer> srcArr = $array(1, 2, 3, 4, 5);

		Function3<Integer, Long, Array<Integer>, Integer> callbackfn = new Function3<Integer, Long, Array<Integer>, Integer>() {
			@Override
			public Integer $invoke(Integer val, Long idx, Array<Integer> obj) {
				srcArr.$set(4, -1);
				if (val > 0) {
					return 1;
				} else {
					return 0;
				}
			}
		};

		Array<Integer> resArr = srcArr.map(callbackfn);
		assertEquals(5, resArr.$length());
		assertEquals(0, resArr.$get(4).intValue());
	}

	/**
	 * es5id: 15.4.4.19-8-3
	 * description: Array.prototype.map doesn't visit deleted elements in array after the call
	 */
	@Test
	public void testMap05() {
		final Array<Integer> srcArr = $array(1, 2, 3, 4, 5);

		Function3<Integer, Long, Array<Integer>, Integer> callbackfn = new Function3<Integer, Long, Array<Integer>, Integer>() {
			@Override
			public Integer $invoke(Integer val, Long idx, Array<Integer> obj) {
				srcArr.$delete(4);
				if (val > 0) {
					return 1;
				} else {
					return 0;
				}
			}
		};
		Array<Integer> resArr = srcArr.map(callbackfn);
		assertEquals(5, resArr.$length());
		assertNull(resArr.$get(4));
	}

	/**
	 * es5id: 15.4.4.19-8-4
	 * description: Array.prototype.map doesn't visit deleted elements when Array.length is decreased
	 */
	@Test
	public void testMap06() {
		final Array<Integer> srcArr = $array(1,2,3,4,5);
		final AtomicInteger callCnt = new AtomicInteger(0);
		Function3<Integer, Long, Array<Integer>, Integer> callbackfn = new Function3<Integer, Long, Array<Integer>, Integer>() {
			@Override
			public Integer $invoke(Integer val, Long idx, Array<Integer> obj) {
				srcArr.$length(2);
				callCnt.incrementAndGet();
				return 1;
			}
		};

		Array<Integer> resArr = srcArr.map(callbackfn);
		assertEquals(5, resArr.$length());
		assertEquals(2, callCnt.intValue());
		assertNull(resArr.$get(2));
	}

	/**
	 * es5id: 15.4.4.19-8-5
	 * description: Array.prototype.map doesn't consider newly added elements in sparse array
	 */
	@Test
	public void testMap07() {
		final Array<Integer> srcArr = new Array<>(10);
		final AtomicInteger callCnt = new AtomicInteger(0);
		Function3<Integer, Long, Array<Integer>, Integer> callbackfn = new Function3<Integer, Long, Array<Integer>, Integer>() {
			@Override
			public Integer $invoke(Integer val, Long idx, Array<Integer> obj) {
				srcArr.$set(1000, 3);
				callCnt.incrementAndGet();
				return val;
			}
		};

		srcArr.$set(1, 1);
		srcArr.$set(2, 2);
		Array<Integer> resArr = srcArr.map(callbackfn);
		assertEquals(10, resArr.$length());
		assertEquals(2, callCnt.intValue());
	}

	/**
	 * es5id: 15.4.4.19-8-b-1
	 * description: Array.prototype.map - callbackfn not called for indexes never been assigned values
	 */
	@Test
	public void testMap08() {
		final Array<Integer> srcArr = new Array<>(10);
		final AtomicInteger callCnt = new AtomicInteger(0);
		Function3<Integer, Long, Array<Integer>, Integer> callbackfn = new Function3<Integer, Long, Array<Integer>, Integer>() {
			@Override
			public Integer $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
				return 1;
			}
		};

		srcArr.$set(1, null); //explicitly assigning a value
		Array<Integer> resArr = srcArr.map(callbackfn);

		assertEquals(10, resArr.$length());
		assertEquals(1, callCnt.intValue());
	}

	/**
	 * es5id: 15.4.4.19-8-c-ii-1
	 * description: Array.prototype.map - callbackfn called with correct parameters
	 */
	@Test
	public void testMap09() {

		final AtomicBoolean par = new AtomicBoolean(true);
		final AtomicBoolean called = new AtomicBoolean(false);
		Function3<Object, Long, Array<Object>, Integer> callbackfn = new Function3<Object, Long, Array<Object>, Integer>() {
			@Override
			public Integer $invoke(Object val, Long idx, Array<Object> obj) {
				called.set(true);
				if(obj.$get(idx) != val) {
					par.set(false);
				}
				return null;
			}
		};

		Array<Object> srcArr = $array(0, 1, true, null, new Object(), "five");
		srcArr.$set(999999, -6.6);

		srcArr.map(callbackfn);

		assertTrue(called.get());
		assertTrue(par.get());
	}

	/**
	 * es5id: 15.4.4.19-8-c-ii-4
	 * description: Array.prototype.map - k values are passed in acending numeric order
	 */
	@Test
	public void testMap10() {
		Array<Integer> arr = $array(0, 1, 2, 3, 4, 5);
		final AtomicInteger lastIdx = new AtomicInteger(0);
		final AtomicInteger called = new AtomicInteger(0);
		final AtomicBoolean result = new AtomicBoolean(true);
		Function3<Integer, Long, Array<Integer>, Integer> callbackfn = new Function3<Integer, Long, Array<Integer>, Integer>() {
			@Override
			public Integer $invoke(Integer val, Long idx, Array<Integer> obj) {
				called.incrementAndGet();
				if (lastIdx.get() != idx) {
					result.set(false);
				} else {
					lastIdx.incrementAndGet();
				}
				return null;
			}
		};

		arr.map(callbackfn);
		assertTrue(result.get());
		assertEquals(arr.$length(), called.get());
	}

	/**
	 * es5id: 15.4.4.19-8-c-ii-5
	 * description: Array.prototype.map - k values are accessed during each iteration and not prior to starting the loop.
	 */
	@Test
	public void testMap11() {

		final Integer[] kIndex = new Integer[4];

		//By below way, we could verify that k would be setted as 0, 1, ..., length - 1 in order, and each value will be setted one time.
		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				//Each position should be visited one time, which means k is accessed one time during iterations.
				if (kIndex[idx.intValue()] == null) {
					//when current position is visited, its previous index should has been visited.
					if (idx != 0 && kIndex[idx.intValue() - 1] == null) {
						return true;
					}
					kIndex[idx.intValue()] = 1;
					return false;

				} else {
					return true;
				}
			}
		};

		Array<Boolean> testResult = $array(11, 12, 13, 14).map(callbackfn);

		assertEquals(4, testResult.$length());
		assertFalse(testResult.$get(0));
		assertFalse(testResult.$get(1));
		assertFalse(testResult.$get(2));
		assertFalse(testResult.$get(3));
	}

	/**
	 * es5id: 15.4.4.19-8-c-ii-7
	 * description: Array.prototype.map - unhandled exceptions happened in callbackfn terminate iteration
	 */
	@Test
	public void testMap12() {
		final AtomicBoolean accessed = new AtomicBoolean(false);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				if (idx > 0) {
					accessed.set(true);
				}
				if (idx == 0) {
					throw new Error("Exception occurred in callbackfn");
				}
				return true;
			}
		};

		Array<Integer> obj = new Array<>(20);
		obj.$set(0, 11);
		obj.$set(4, 10);
		obj.$set(10, 8);

		try {
			obj.map(callbackfn);
			throw new AssertionError("This point should not be reached");

		} catch (Error e) {
			// we should arrive here
			assertFalse(accessed.get());
		}
	}

	/**
	 * es5id: 15.4.4.19-8-c-ii-8
	 * description: Array.prototype.map - element changed by callbackfn on previous iterations is observed
	 */
	@Test
	public void testMap13() {
		final Array<Integer> obj = $array(9, 12);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				if (idx == 0) {
					obj.$set(idx + 1, 8);
				}
				return val > 10;
			}
		};

		Array<Boolean> testResult = obj.map(callbackfn);
		assertFalse(testResult.$get(1));
	}

	/**
	 * es5id: 15.4.4.19-8-c-ii-19
	 * description: Array.prototype.map - non-indexed properties are not called.
	 */
	@Test
	public void testMap14() {

		final AtomicInteger called = new AtomicInteger(0);
		final AtomicBoolean result = new AtomicBoolean(false);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				called.incrementAndGet();
				if (val == 11) {
					result.set(true);
				}
				return true;
			}
		};

		Array<Integer> obj = new Array<>(20);
		obj.$set(0, 9);
		obj.$set("non_index_property", 11);

		Array<Boolean> testResult = obj.map(callbackfn);

		assertFalse(result.get());
		assertTrue(testResult.$get(0));
		assertEquals(1, called.get());
	}

	/**
	 * es5id: 15.4.4.19-8-c-iii-2
	 * description: Array.prototype.map - value of returned array element equals to 'mappedValue'
	 */
	@Test
	public void testMap15() {
		Function3<Integer, Long, Array<Integer>, Integer> callbackfn = new Function3<Integer, Long, Array<Integer>, Integer>() {
			@Override
			public Integer $invoke(Integer val, Long idx, Array<Integer> obj) {
				return val;
			}
		};

		Array<Integer> obj = $array(11, 9);
		Array<Integer> newArr = obj.map(callbackfn);

		assertSame(obj.$get(0), newArr.$get(0));
		assertSame(obj.$get(1), newArr.$get(1));
	}

	/**
	 * es5id: 15.4.4.19-9-1
	 * description: Array.prototype.map doesn't mutate the Array on which it is called on
	 */
	@Test
	public void testMap16() {
		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				return true;
			}
		};

		Array<Integer> srcArr = $array(1,2,3,4,5);
		srcArr.map(callbackfn);

		assertEquals(1, srcArr.$get(0).intValue());
		assertEquals(2, srcArr.$get(1).intValue());
		assertEquals(3, srcArr.$get(2).intValue());
		assertEquals(4, srcArr.$get(3).intValue());
		assertEquals(5, srcArr.$get(4).intValue());
	}
}
