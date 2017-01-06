package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.stjs.javascript.JSCollections.$array;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.stjs.javascript.functions.Function3;

public class ArrayFilterTest {

	/**
	 * es5id: 15.4.4.20-2-2
	 * description: Array.prototype.filter - 'length' is own data property on an Array
	 */
	@Test
	public void testFilter01() {
		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				return obj.$length() == 2;
			}
		};

		Array<Integer> newArr = $array(12, 11).filter(callbackfn);
		assertEquals(2, newArr.$length());
	}

	/**
	 * es5id: 15.4.4.20-4-3
	 * description: Array.prototype.filter throws TypeError if callbackfn is null
	 */
	@Test(expected = Error.class)
	public void testFilter02() {
		new Array<Integer>(10).filter((Function3)null);
	}

	/**
	 * es5id: 15.4.4.20-4-12
	 * description: Array.prototype.filter - 'callbackfn' is a function
	 */
	@Test()
	public void testFilter03() {
		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				return idx == 1 && val == 9;
			}
		};

		Array<Integer> newArr = $array(11, 9).filter(callbackfn);
		assertEquals(1, newArr.$length());
		assertEquals(9, newArr.$get(0).intValue());
	}

	/**
	 * es5id: 15.4.4.20-5-29
	 * description: Array.prototype.filter - returns an array whose length is 0
	 */
	@Test()
	public void testFilter04() {
		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				return false;
			}
		};

		Array<Integer> newArr = $array(11).filter(callbackfn);
		assertEquals(0, newArr.$length());
	}

	/**
	 * es5id: 15.4.4.20-6-1
	 * description: Array.prototype.filter returns an empty array if 'length' is 0 (empty array)
	 */
	@Test()
	public void testFilter05() {
		final AtomicBoolean called = new AtomicBoolean(false);
		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				called.set(true);
				return false;
			}
		};

		Array<Integer> a = new Array<Integer>().filter(callbackfn);
		assertEquals(0, a.$length());
		assertFalse(called.get());
	}

	/**
	 * es5id: 15.4.4.20-9-1
	 * description: Array.prototype.filter doesn't consider new elements added to array after it is called
	 */
	@Test()
	public void testFilter06() {
		final Array<Integer> srcArr = $array(1, 2, null, 4, 5);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				srcArr.$set(2, 3);
				srcArr.$set(5, 6);
				return true;
			}
		};

		Array<Integer> resArr = srcArr.filter(callbackfn);
		assertEquals(5, resArr.$length());
	}

	/**
	 * es5id: 15.4.4.20-9-2
	 * description: Array.prototype.filter considers new value of elements in array after it is called
	 */
	@Test()
	public void testFilter07() {

		final Array<Integer> srcArr = $array(1,2,3,4,5);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				srcArr.$set(2, -1);
				srcArr.$set(4, -1);
				return val > 0;
			}
		};

		Array<Integer> resArr = srcArr.filter(callbackfn);
		assertEquals(3, resArr.$length());
		assertEquals(1, resArr.$get(0).intValue());
		assertEquals(4, resArr.$get(2).intValue());
	}

	/**
	 * es5id: 15.4.4.20-9-3
	 * description: Array.prototype.filter doesn't visit deleted elements in array after the call
	 */
	@Test()
	public void testFilter08() {

		final Array<Integer> srcArr = $array(1,2,3,4,5);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				srcArr.$delete(2);
				srcArr.$delete(4);
				return val > 0;
			}
		};

		Array<Integer> resArr = srcArr.filter(callbackfn);
		assertEquals(3, resArr.$length());
		assertEquals(1, resArr.$get(0).intValue());
		assertEquals(4, resArr.$get(2).intValue());
	}

	/**
	 * es5id: 15.4.4.20-9-4
	 * description: Array.prototype.filter doesn't visit deleted elements when Array.length is decreased
	 */
	@Test()
	public void testFilter09() {
		final Array<Integer> srcArr = $array(1,2,3,4,6);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				srcArr.$length(2);
				return true;
			}
		};

		Array<Integer> resArr = srcArr.filter(callbackfn);
		assertEquals(2, resArr.$length());
	}

	/**
	 * es5id: 15.4.4.20-9-5
	 * description: Array.prototype.filter doesn't consider newly added elements in sparse array
	 */
	@Test()
	public void testFilter10() {
		final Array<Integer> srcArr = new Array<>(10);
		srcArr.$set(1, 1);
		srcArr.$set(2, 2);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				srcArr.$set(1000, 3);
				return true;
			}
		};

		Array<Integer> resArr = srcArr.filter(callbackfn);
		assertEquals(2, resArr.$length());
	}

	/**
	 * es5id: 15.4.4.20-9-b-1
	 * description: Array.prototype.filter - callbackfn not called for indexes never been assigned values
	 */
	@Test()
	public void testFilter11() {

		final AtomicInteger callCnt = new AtomicInteger(0);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				callCnt.incrementAndGet();
				return false;
			}
		};

		Array<Integer> srcArr = new Array<>(10);
		srcArr.$set(1, null); //explicitly assigning a value
		Array<Integer> resArr = srcArr.filter(callbackfn);

		assertEquals(0, resArr.$length());
		assertEquals(1, callCnt.get());
	}

	/**
	 * es5id: 15.4.4.20-9-c-ii-1
	 * description: Array.prototype.filter - callbackfn called with correct parameters
	 */
	@Test()
	public void testFilter12() {
		final AtomicBoolean par = new AtomicBoolean(true);
		final AtomicBoolean called = new AtomicBoolean(false);

		Function3<Object, Long, Array<Object>, Boolean> callbackfn = new Function3<Object, Long, Array<Object>, Boolean>() {
			@Override
			public Boolean $invoke(Object val, Long idx, Array<Object> obj) {
				called.set(true);
				if(obj.$get(idx) != val) {
					par.set(false);
				}
				return false;
			}
		};

		Array<Object> srcArr = $array(0,1,true,null,new Object(),"five");
		srcArr.$set(999999, -6.6);
		srcArr.filter(callbackfn);

		assertTrue(called.get());
		assertTrue(par.get());
	}

	/**
	 * es5id: 15.4.4.20-9-c-ii-4
	 * description: Array.prototype.filter - k values are passed in ascending numeric order
	 */
	@Test
	public void testFilter13() {
		Array<Integer> arr = $array(0, 1, 2, 3, 4, 5);
		final AtomicInteger lastIdx = new AtomicInteger(0);
		final AtomicInteger called = new AtomicInteger(0);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				called.incrementAndGet();
				if (lastIdx.get() != idx) {
					return false;
				} else {
					lastIdx.incrementAndGet();
					return true;
				}
			}
		};

		Array<Integer> newArr = arr.filter(callbackfn);
		assertEquals(newArr.$length(), called.get());
	}

	/**
	 * es5id: 15.4.4.20-9-c-ii-5
	 * description: Array.prototype.filter - k values are accessed during each iteration and not prior to starting the loop on an Array
	 */
	@Test
	public void testFilter14() {

		final Integer[] kIndex = new Integer[4];
		final AtomicInteger called = new AtomicInteger(0);

		//By below way, we could verify that k would be setted as 0, 1, ..., length - 1 in order, and each value will be setted one time.
		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				called.incrementAndGet();
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

		Array<Integer> newArr = $array(11, 12, 13, 14).filter(callbackfn);
		assertEquals(0, newArr.$length());
		assertEquals(4, called.get());
	}

	/**
	 * es5id: 15.4.4.20-9-c-ii-7
	 * description: Array.prototype.filter - unhandled exceptions happened in callbackfn terminate iteration
	 */
	@Test
	public void testFilter15() {
		final AtomicInteger called = new AtomicInteger(0);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				called.incrementAndGet();
				if (called.get() == 1) {
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
			obj.filter(callbackfn);
			throw new AssertionError("This point should not be reached");

		} catch (Error e) {
			// we should arrive here
			assertEquals(1, called.get());
		}
	}

	/**
	 * es5id: 15.4.4.20-9-c-ii-8
	 * description:  Array.prototype.filter - element changed by callbackfn on previous iterations is observed
	 */
	@Test
	public void testFilter16() {
		Array<Integer> obj = $array(11, 12);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				if (idx == 0) {
					obj.$set(idx.intValue() + 1, 8);
				}
				return val > 10;
			}
		};

		Array<Integer> newArr = obj.filter(callbackfn);

		assertEquals(1, newArr.$length());
		assertEquals(11, newArr.$get(0).intValue());
	}

	/**
	 * es5id: 15.4.4.20-9-c-ii-19
	 * description: Array.prototype.filter - non-indexed properties are not called
	 */
	@Test
	public void testFilter17() {

		final AtomicBoolean accessed = new AtomicBoolean(false);

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				accessed.set(true);
				return val == 8;
			}
		};

		Array<Integer> obj = new Array<>(20);
		obj.$set(0, 11);
		obj.$set("non_index_property", 8);
		obj.$set(2, 5);
		Array<Integer> newArr = obj.filter(callbackfn);

		assertEquals(0, newArr.$length());
		assertTrue(accessed.get());
	}

	/**
	 * es5id: 15.4.4.20-10-1
	 * description: Array.prototype.filter doesn't mutate the Array on which it is called on
	 */
	@Test
	public void testFilter18() {

		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				return true;
			}
		};

		Array<Integer> srcArr = $array(1,2,3,4,5);
		srcArr.filter(callbackfn);

		assertEquals(1, srcArr.$get(0).intValue());
		assertEquals(2, srcArr.$get(1).intValue());
		assertEquals(3, srcArr.$get(2).intValue());
		assertEquals(4, srcArr.$get(3).intValue());
		assertEquals(5, srcArr.$get(4).intValue());
	}

	/**
	 * es5id: 15.4.4.20-10-2
	 * description: Array.prototype.filter returns new Array with length equal to number of true returned by callbackfn
	 */
	@Test
	public void testFilter19() {
		Function3<Integer, Long, Array<Integer>, Boolean> callbackfn = new Function3<Integer, Long, Array<Integer>, Boolean>() {
			@Override
			public Boolean $invoke(Integer val, Long idx, Array<Integer> obj) {
				return val % 2 == 1;
			}
		};

		Array<Integer> resArr = $array(1,2,3,4,5).filter(callbackfn);

		assertEquals(3, resArr.$length());
		assertEquals(1, resArr.$get(0).intValue());
		assertEquals(3, resArr.$get(1).intValue());
		assertEquals(5, resArr.$get(2).intValue());
	}

}
