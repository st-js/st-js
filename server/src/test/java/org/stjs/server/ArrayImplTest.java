package org.stjs.server;

import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.stjs.javascript.JSCollections.$array;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.javascript.Array;

/**
 * The tests in this class are ported from the test262 test suite: http://hg.ecmascript.org/tests/test262
 * @author npiguet
 */
public class ArrayImplTest {
	
	// ========================================================
	// Tests for the section 15.4 of the ECMA-262 specification 
	// ========================================================

	// Note: We know we are out of spec since we only accept array indices between -2^31 and 2^31-1
	// (the spec specifies 2^32), but this is the maximum range int in java will allow. We believe this
	// is not likely to cause problems in real world programs.
	
	// Another point where we know we are out of spec, is that javascript allows any kind of Object as 
	// index (and will use it's toString() method to figure out where it should land in the array)
	// this however is quite confusing and we have made the conscious decision not to support it
	
	@Test
	public void testGetterSetter01(){
		// A property name P (in the form of a string value) is an array index
		// if and only if ToString(ToUint32(P)) is equal to P and ToUint32(P) is not equal to 2^32 - 1
		Array<Integer> x = $array();
		x.$set("true", 1); // "true" should not be converted to 1, but should still be accessible
		assertEquals(null, x.$get(1));
		assertEquals(1, x.$get("true").intValue());
	}

	@Test
	public void testGetterSetter02(){
		Array<Integer> x = $array();
		x.$set("false", 0);
		x = $array();
		assertEquals(null, x.$get(0)); // same goes for "false"
		assertEquals(0, x.$get("false").intValue());
	}

	@Test
	public void testGetterSetter03(){
		Array<Integer> x = $array();
		x.$set("NaN", 1);
		assertEquals(null, x.$get(0));
		assertEquals(1, x.$get("NaN").intValue());
	}
	
	@Test
	public void testGetterSetter04(){
		Array<Integer> x = $array();
		x.$set("Infinity", 1);
		assertEquals(null, x.$get(0));
		assertEquals(1, x.$get("Infinity").intValue());
	}
	
	@Test
	public void testGetterSetter05(){
		Array<Integer> x = $array();
		x.$set("-Infinity", 1);
		assertEquals(null, x.$get(0));
		assertEquals(1, x.$get("-Infinity").intValue());
	}
		
	@Test
	public void testGetterSetter06(){
		Array<Integer> x = $array();
		x.$set("1.1", 1);
		assertEquals(null, x.$get(1));
		assertEquals(1, x.$get("1.1").intValue());
	}

		
	@Test
	public void testGetterSetter07(){
		Array<Integer> x = $array();
		x.$set("0", 0);
		assertEquals(0, x.$get(0).intValue());
	}

	@Test
	public void testGetterSetter08(){
		Array<Integer> x = $array();
		x.$set("1", 1);
		assertEquals(1, x.$get(1).intValue());
	}
		
	@Test
	public void testGetterSetter09(){
		Array<Integer> x = $array();
		x.$set(null, 0);
		assertEquals(null, x.$get(0));
		assertEquals(0, x.$get("null").intValue());
	}
		
	@Test
	public void testGetterSetter10(){
		// Array index is power of two
		Array<Integer> x = $array();
		int k = 1;
		for (int i = 0; i < 31; i++) { // /!\ original test says 32, but our ints only go to 31
		  k = k * 2;
		  x.$set(k - 2, k);  
		}

		k = 1;
		for (int i = 0; i < 32; i++) {
		  k = k * 2;
		  assertEquals(k, x.$get(k - 2).intValue());
		}
	}

	// ======================================================
	// Tests for section 15.4.1 of the ECMA-262 specification 
	// ======================================================
	
	
	@Test
	public void testConstructor01(){
		// When the constructor of Array is passed exactly 1 parameter, then the first parameter
		// is treated as the length of the array
		Array<Integer> x = $array(2);
		assertFalse(x.$length() == 1);
		assertFalse(x.$get(0) == 2);
	}
	
	@Test
	public void testConstructor02(){
		// The length property of the newly constructed object;
		// is set to the number of arguments
		assertEquals(0, $array().$length());
		assertEquals(4, $array(0, 1, 0, 1).$length());
		assertEquals(2, $array(null, null).$length());
	}
	
	@Test
	public void testConstructor03(){
		//The 0 property of the newly constructed object is set to item0
		// (if supplied); the 1 property of the newly constructed object is set to item1
		// (if supplied); and, in general, for as many arguments as there are, the k property
		// of the newly constructed object is set to argument k, where the first argument is
		// considered to be argument number 
		Array<Integer> x = $array( //
				0,1,2,3,4,5,6,7,8,9, //
				10,11,12,13,14,15,16,17,18,19, //
				20,21,22,23,24,25,26,27,28,29, //
				30,31,32,33,34,35,36,37,38,39, //
				40,41,42,43,44,45,46,47,48,49, //
				50,51,52,53,54,55,56,57,58,59, //
				60,61,62,63,64,65,66,67,68,69, //
				70,71,72,73,74,75,76,77,78,79, //
				80,81,82,83,84,85,86,87,88,89, //
				90,91,92,93,94,95,96,97,98,99 //
				);
		for(int i = 0; i < 100; i ++){
			assertEquals(i, (int)x.$get(i));
		}
	}
	
	// ====================================================
	// Tests section 15.4.4.2 if the ECMA-262 specification
	// ====================================================
	
	@Test
	public void testToString01(){
		// The result of calling this function is the same as if
		// the built-in join method were invoked for this object with no argument
		
		// If the array is empty, return the empty string
		Array<Integer> x = $array();
		assertToStringJoinEquals("", x);
	}

	@Test
	public void testToString02(){
		Array<Integer> x = $array();
		x.$set(0,  1);
		x.$length(0);
		assertToStringJoinEquals("", x);
	}
		
	@Test
	public void testToString03(){
		// The elements of the array are converted to strings, and these strings are
		// then concatenated, separated by occurrences of the separator. If no separator is provided,
		// a single comma is used as the separator
		Array<Integer> x = $array(0,1,2,3);
		assertToStringJoinEquals("0,1,2,3", x);
	}

	@Test
	public void testToString04(){
		Array<Integer> x = $array();
		x.$set(0, 0);
		x.$set(3, 3);
		assertToStringJoinEquals("0,,,3", x);
	}
	
	@Test
	public void testToString05(){
		Array<Integer> x = $array(null, 1, null, 3);
		assertToStringJoinEquals(",1,,3", x);
	}

	@Test
	public void testToString06(){
		Array<Integer> x = $array();
		x.$set(0, 0);
		assertToStringJoinEquals("0", x);
	}
	
	@Test
	public void testToString07(){
		// Operator use ToString from array arguments
		Array<String> s = $array("","","");
		assertToStringJoinEquals(",,", s);
	}

	@Test
	public void testToString08(){
		Array<String> s = $array("\\","\\","\\");
		assertToStringJoinEquals("\\,\\,\\", s);
	}
		
	@Test
	public void testToString09(){
		Array<String> s = $array("&", "&", "&");
		assertToStringJoinEquals("&,&,&", s);
	}
		
	@Test
	public void testToString10(){
		Array<Boolean> b = $array(true,true,true);
		assertToStringJoinEquals("true,true,true", b);
	}
	
	@Test
	public void testToString11(){
		Array<String> x = $array(null,null,null);
		assertToStringJoinEquals(",,", x);
	}

	@Test
	public void testToString12(){
		Array<Double> d = $array(POSITIVE_INFINITY, POSITIVE_INFINITY,POSITIVE_INFINITY);
		assertToStringJoinEquals("Infinity,Infinity,Infinity", d);
	}
		
	@Test
	public void testToString13(){
		Array<Double> d = $array(NaN,NaN,NaN);
		assertToStringJoinEquals("NaN,NaN,NaN", d); // BATMAAAN! 
	}
	
	@Test
	public void testToString14(){
		// If Type(value) is Object, evaluate ToPrimitive(value, String)
		Object object = new Object(){
			public String valueOf(){
				return "+";
			}
		};
		Array<Object> o = $array(object);
		assertToStringJoinEquals("[object Object]", o);
	}
	
	@Test
	public void testToString15(){
		Object object = new Object(){
			public String valueOf(){
				return "+";
			}
			public String toString(){
				return "*";
			}
		};
		Array<Object> o = $array(object);
		assertToStringJoinEquals("*", o); 
	}
	
	@Test
	public void testToString16(){
		Object object = new Object(){
			public String valueOf(){
				throw new RuntimeException("error");
			}
			
			public String toString(){
				return "*";
			}
		};
		Array<Object> o = $array(object);
		assertToStringJoinEquals("*", o); // not supposed to throw an exception
	}
	
	@Test
	public void testToString17(){
		Object object = new Object(){
			public String toString(){
				return "*";
			}
		};
		Array<Object> o = $array(object);
		assertToStringJoinEquals("*", o); 
	}

	@Test
	public void testToString18(){
		Object object = new Object(){
			public Object valueOf(){
				return new Object();
			}
			public String toString(){
				return "*";
			}
		};
		Array<Object> o = $array(object);
		assertToStringJoinEquals("*", o);
	}

	@Test
	public void testToString19(){
		Object object = new Object(){
			public String valueOf(){
				return "+";
			}
			public String toString(){
				throw new RuntimeException("error");
			}
		};
		Array<Object> o = $array(object);
		try{
			o.toString();
			assertTrue(false);
		}catch(RuntimeException e){
			assertEquals("error", e.getMessage());
		}
	}
	
	private static void assertToStringJoinEquals(String expected, Array<?> array){
		assertEquals(array.join(), array.toString());
		assertEquals(expected, array.toString());
	}
	
	// ===================================================
	// Tests for the section 14.4.4.3 of the ECMA-262 spec
	// ===================================================
	// The elements of the array are converted to strings using their
	// toLocaleString methods, and these strings are then concatenated, separated
	// by occurrences of a separator string that has been derived in an
	// implementation-defined locale-specific way
	
	@Test
	public void testToLocaleString01(){
		// verify that we correctly call toLocaleString on the elements
		final AtomicInteger n = new AtomicInteger(0);
		Object obj = new Object(){
			public String toLocaleString() {
				n.incrementAndGet();
				return "";
			};
		};
		Array<Object> arr = $array(null, obj, null, obj, obj);
		arr.toLocaleString();
		
		assertEquals(3, n.get());
	}
	
	// ===================================================
	// Tests for the section 14.4.4.4 of the ECMA-262 spec
	// ===================================================
	// When the concat method is called with zero or more arguments item1, item2,
	// etc., it returns an array containing the array elements of the object followed by
	// the array elements of each argument in order
	
	@SuppressWarnings("unchecked")
	@Test
	public void testConcat01(){ 
		Array<Integer> x = $array();
		Array<Integer> y = $array(0,1);
		Array<Integer> z = $array(2,3,4);
		Array<Integer> arr = x.concat(y,z);

		assertNotSame(x, arr);
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(1, arr.$get(1).intValue());
		assertEquals(2, arr.$get(2).intValue());
		assertEquals(3, arr.$get(3).intValue());
		assertEquals(4, arr.$get(4).intValue());
		assertEquals(5, arr.$length());
	}

	@Test
	public void testConcat02(){
		Array<Object> x = $array();
		x.$set(0, 0);
		Object y = new Object();
		Array<Object> arr = x.concat(y, -1, true, "NaN");

		assertNotSame(x, arr);
		assertEquals(Integer.valueOf(0), arr.$get(0));
		assertSame(y, arr.$get(1));
		assertEquals(Integer.valueOf(-1), arr.$get(2));
		assertEquals(Boolean.TRUE, arr.$get(3));
		assertEquals("NaN", arr.$get(3));
		assertEquals(5, arr.$length());
	}
	
	// ===========================================
	// Tests section 15.4.4.5 of the ECMA-262 Spec
	// ===========================================
	// Note: Since the toString() tests always verify that the result of toString() and join()
	// are the same, some of the tests in the test262 suite have been omitted, since they are
	// basically a duplicate of what is done via toString(). 
	//
	// All the remaining tests focus on verifying that toString() is called on the "separator"
	// argument. However, this makes no sense for us, because the ST-JS version of join() takes
	// defines the "separator" argument to be a String.
	// 
	// So basically, no tests were ported for that section

	
	// ===========================================
	// Tests section 15.4.4.6 of the ECMA-262 Spec
	// ===========================================
	// If length equal zero, call the [[Put]] method of this object
	// with arguments "length" and 0 and return undefined
	//
	//	The last element of the array is removed from the array
	// and returned
	
	@Test
	public void testPop01(){
		Array<Object> x = $array();
		Object pop = x.pop();
		
		assertNull(pop);
		assertEquals(x.$length(), 0);
	}
	
	@Test
	public void testPop02(){
		Array<Integer> x = $array(1,2,3);
		x.$length(0);
		Integer pop = x.pop();
		
		assertNull(pop);
		assertEquals(0, x.$length());
	}
	
	@Test
	public void testPop03(){
		Array<Integer> x = $array(0,1,2,3);
		int pop = x.pop();
		
		assertEquals(3, pop);
		assertEquals(3, x.$length());
		assertEquals(null, x.$get(3));
		assertEquals(2, x.$get(2).intValue());
	}
	
	@Test
	public void testPop04(){
		Array<Integer> x = $array();
		x.$set(0, 0);
		x.$set(3, 3);
		int pop = x.pop();
		
		assertEquals(3, pop);
		assertEquals(3, x.$length());
		assertEquals(null, x.$get(3));
		assertEquals(null, x.$get(2));


		x.$length(1);
		pop = x.pop();
		assertEquals(0, pop);
		assertEquals(0, x.$length());
	}
	
	// ===========================================
	// Tests section 15.4.4.7 of the ECMA-262 Spec
	// ===========================================
	// The arguments are appended to the end of the array, in
	// the order in which they appear. The new length of the array is returned
	// as the result of the call
	
	@Test
	public void testPush01(){
		Array<Integer> x = $array();
		int push = x.push(1);
		
		assertEquals(1, push);
		assertEquals(1, x.$get(0).intValue());
		
		push = x.push();
		
		assertEquals(1, push);
		assertEquals(null, x.$get(1));
		
		push = x.push(-1);
		
		assertEquals(2, push);
		assertEquals(-1, x.$get(1).intValue());
		assertEquals(2, x.$length());
	}
	
	@Test
	public void testPush02(){
		Array<Object> x = $array();
		
		x.$set(0,  0);
		int push = x.push(true, Double.POSITIVE_INFINITY, "NaN", "1", -1);
		
		assertEquals(6, push);
		assertEquals(6, x.$length());
		assertEquals(0, x.$get(0));
		assertSame(Boolean.TRUE, x.$get(1));
		assertEquals(Double.POSITIVE_INFINITY, x.$get(2));
		assertEquals("NaN", x.$get(3));
		assertEquals("1", x.$get(4));
		assertEquals(-1, x.$get(5));
	}
	
	// ===========================================
	// Tests section 15.4.4.8 of the ECMA-262 Spec
	// ===========================================
	// The elements of the array are rearranged so as to reverse their order.
	// The object is returned as the result of the call
	
	@Test
	public void testReverse01(){
		Array<Integer> x = $array();
		Array<Integer> reverse = x.reverse();
		
		assertSame(x, reverse);
		assertEquals(0, x.$length());
	}
	
	@Test
	public void testReverse02(){
		Array<Integer> x = $array();
		x.$set(0, 1);
		Array<Integer> reverse = x.reverse();
		
		assertSame(x, reverse);
		assertEquals(1, x.$length());
		assertEquals(1, x.$get(0).intValue());
	}
		
	@Test
	public void testReverse03(){
		Array<Integer> x = $array(1,2);
		Array<Integer> reverse = x.reverse();
		
		assertSame(x, reverse);
		assertEquals(2, x.$get(0).intValue());
		assertEquals(1, x.$get(1).intValue());
		assertEquals(2, x.$length());
	}
	
	@Test
	public void testReverse04(){
		Array<Object> x = $array();
		x.$set(0, true);
		x.$set(2, POSITIVE_INFINITY);
		x.$set(4, null);
		x.$set(5, null);
		x.$set(8, "NaN");
		x.$set(9, "-1");
		Array<Object> reverse = x.reverse();
		
		assertSame(x, reverse);
		assertEquals("-1", x.$get(0));
		assertEquals("NaN", x.$get(1));
		assertEquals(null, x.$get(2));
		assertEquals(null, x.$get(3));
		assertEquals(null, x.$get(4));
		assertEquals(null, x.$get(5));
		assertEquals(null, x.$get(6));
		assertEquals(POSITIVE_INFINITY, x.$get(7));
		assertEquals(null, x.$get(8));
		assertEquals(true, x.$get(9));

		x.$length(9);
		reverse = x.reverse();

		assertSame(x, reverse);
		assertEquals(null, x.$get(0));
		assertEquals(POSITIVE_INFINITY, x.$get(1));
		assertEquals(null, x.$get(2));
		assertEquals(null, x.$get(3));
		assertEquals(null, x.$get(4));
		assertEquals(null, x.$get(5));
		assertEquals(null, x.$get(6));
		assertEquals("NaN", x.$get(7));
		assertEquals("-1", x.$get(8));
	}
	
	// ===========================================
	// Tests section 15.4.4.9 of the ECMA-262 Spec
	// ===========================================
	//  The first element of the array is removed from the array and
	// returned
	// If length equal zero, call the [[Put]] method of this object
	// with arguments "length" and 0 and return undefined
	
	@Test
	public void testShift01(){
		Array<Object> x = $array();
		Object shift = x.shift();
		
		assertNull(shift);
		assertEquals(0, x.$length());
	}
	
	@Test
	public void testShift02(){
		Array<Integer> x = $array(1,2,3);
		x.$length(0);
		Integer shift = x.shift();
		
		assertNull(shift);
		assertEquals(0, x.$length());  
	}
	
	@Test
	public void testShift03(){
		Array<Integer> x = $array(0,1,2,3);
		Integer shift = x.shift();
		
		assertEquals(0, shift.intValue());
		assertEquals(3, x.$length());
		assertEquals(1, x.$get(0).intValue());
		assertEquals(2, x.$get(1).intValue());
	}
	
	@Test
	public void testShift04(){
		Array<Integer> x = $array();
		x.$set(0, 0);
		x.$set(3, 3);
		Integer shift = x.shift();
		
		assertEquals(0, shift.intValue());
		assertEquals(3, x.$length());
		assertEquals(null, x.$get(0));
		assertEquals(null, x.$get(12));
		
		x.$length(1);
		shift = x.shift();
		
		assertNull(shift);
		assertEquals(0, x.$length());
	}
	
	// ============================================
	// Tests section 15.4.4.10 of the ECMA-262 Spec
	// ============================================
	// 
	// If start is positive, use min(start, length).
	// If start is negative, use max(start + length, 0).
	// If end is positive, use min(end, length)
	// If end is negative, use max(end + length, 0)
	//
	// Note: we are not including tests that check for the correct behavior to slice() 
	// when the end or start parameters are not numbers, because our interface declaration
	// does not allow that (and it seems like a bad practice anyway)
	
	@Test
	public void testSlice01(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(0,3);

		assertEquals(3, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(1, arr.$get(1).intValue());
		assertEquals(2, arr.$get(2).intValue());
		assertEquals(null, arr.$get(3));
	}
	
	@Test
	public void testSlice02(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(3,3);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}
	
	@Test
	public void testslice03(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(4,3);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}
	
	@Test
	public void testslice04(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(5,5);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}

	@Test
	public void testSlice05(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(3,5);

		assertEquals(2, arr.$length());
		assertEquals(3, arr.$get(0).intValue());
		assertEquals(4, arr.$get(1).intValue());
		assertEquals(null, arr.$get(3));
	}

	@Test
	public void testSlice06(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(2,4);

		assertEquals(2, arr.$length());
		assertEquals(2, arr.$get(0).intValue());
		assertEquals(3, arr.$get(1).intValue());
		assertEquals(null, arr.$get(3));
	}
	
	@Test
	public void testSlice07(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(3,6);

		assertEquals(2, arr.$length());
		assertEquals(3, arr.$get(0).intValue());
		assertEquals(4, arr.$get(1).intValue());
		assertEquals(null, arr.$get(3));
	}
	
	@Test
	public void testSlice08(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(-3,3);

		assertEquals(1, arr.$length());
		assertEquals(2, arr.$get(0).intValue());
		assertEquals(null, arr.$get(1));
	}
	
	@Test
	public void testSlice09(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(-1,5);

		assertEquals(1, arr.$length());
		assertEquals(4, arr.$get(0).intValue());
		assertEquals(null, arr.$get(1));
	}
	
	@Test
	public void testSlice10(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(-5,1);

		assertEquals(1, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(null, arr.$get(1));
	}
	
	@Test
	public void testSlice11(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(-9,5);

		assertEquals(5, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(0, arr.$get(1).intValue());
		assertEquals(0, arr.$get(2).intValue());
		assertEquals(0, arr.$get(3).intValue());
		assertEquals(0, arr.$get(4).intValue());
		assertEquals(null, arr.$get(5));
	}
	
	@Test
	public void testSlice12(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(0,-2);

		assertEquals(3, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(0, arr.$get(1).intValue());
		assertEquals(0, arr.$get(2).intValue());
		assertEquals(null, arr.$get(3));
	}
	
	@Test
	public void testSlice13(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(1,-4);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}
	
	@Test
	public void testSlice14(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(0,-5);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}
	
	@Test
	public void testSlice15(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(4,-9);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}
	
	@Test
	public void testSlice16(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(-5,-2);

		assertEquals(3, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(0, arr.$get(1).intValue());
		assertEquals(0, arr.$get(2).intValue());
		assertEquals(null, arr.$get(3));
	}
	
	@Test
	public void testSlice17(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(-3,-1);

		assertEquals(2, arr.$length());
		assertEquals(2, arr.$get(0).intValue());
		assertEquals(3, arr.$get(1).intValue());
		assertEquals(null, arr.$get(2));
	}
	
	@Test
	public void testSlice18(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(-9,-1);

		assertEquals(4, arr.$length());
		assertEquals(0, arr.$get(0).intValue());
		assertEquals(1, arr.$get(1).intValue());
		assertEquals(2, arr.$get(2).intValue());
		assertEquals(3, arr.$get(3).intValue());
		assertEquals(null, arr.$get(4));
	}
	
	@Test
	public void testSlice19(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(-6,-6);

		assertEquals(0, arr.$length());
		assertEquals(null, arr.$get(0));
	}
	
	@Test
	public void testSlice20(){
		Array<Integer> x = $array(0,1,2,3,4);
		Array<Integer> arr = x.slice(-2);

		assertEquals(2, arr.$length());
		assertEquals(3, arr.$get(0).intValue());
		assertEquals(4, arr.$get(1).intValue());
		assertEquals(null, arr.$get(2)); 
	}
	
	// ============================================
	// Tests section 15.4.4.11 of the ECMA-262 Spec
	// ============================================
}
