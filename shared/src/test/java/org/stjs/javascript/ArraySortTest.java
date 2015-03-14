package org.stjs.javascript;

import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSGlobal.Array;
import static org.stjs.javascript.JSGlobal.String;

import org.junit.Test;

import java.util.Objects;

// ============================================
// Tests section 15.4.4.11 of the ECMA-262 Spec
// ============================================
public class ArraySortTest {

	@Test
	public void testSort01() {
		Array<Integer> x = Array(2); // remember, this makes an empty array with length=2
		x.sort();

		assertEquals(2, x.$length());
		assertNull(x.$get(0));
		assertNull(x.$get(1));
	}

	@Test
	public void testSort02() {
		Array<Integer> x = Array(2); // remember, this makes an empty array with length=2
		x.$set(1, 1);
		x.sort();

		assertEquals(2, x.$length());
		assertEquals(1, x.$get(0).intValue());
		assertEquals(null, x.$get(1));

		x = Array(2); // remember, this makes an empty array with length=2
		x.$set(0, 1);
		x.sort();

		assertEquals(2, x.$length());
		assertEquals(1, x.$get(0).intValue());
		assertEquals(null, x.$get(1));
	}

	@Test
	public void testSort03() {
		SortFunction<Integer> myComparefn = new SortFunction<Integer>() {
			@Override
			public int $invoke(Integer x, Integer y) {
				if (x == null) {
					return -1;
				}
				if (y == null) {
					return 1;
				}
				return 0;
			}
		};

		Array<Integer> x = Array(2); // remember, this makes an empty array with length=2
		x.$set(1, 1);
		x.sort(myComparefn);

		assertEquals(2, x.$length());
		assertEquals(1, x.$get(0).intValue());
		assertEquals(null, x.$get(1));

		x = Array(2); // remember, this makes an empty array with length=2
		x.$set(0, 1);
		x.sort(myComparefn);

		assertEquals(2, x.$length());
		assertEquals(1, x.$get(0).intValue());
		assertEquals(null, x.$get(1));
	}

	@Test
	public void testSort04() {
		Array<Object> x = $array(null, null);
		x.sort();

		assertEquals(2, x.$length());
		assertNull(x.$get(0));
		assertNull(x.$get(1));
	}

	@Test
	public void testSort05() {
		Array<Integer> x = $array(null, 1);
		x.sort();

		assertEquals(2, x.$length());
		assertEquals(1, x.$get(0).intValue());
		assertEquals(null, x.$get(1));

		x = $array(1, null);
		x.sort();

		assertEquals(2, x.$length());
		assertEquals(1, x.$get(0).intValue());
		assertEquals(null, x.$get(1));
	}

	@Test
	public void testSort06() {
		// this function sorts nulls last. By default nulls are sorted before numbers because
		// the string "null" is alphabetically smaller than all numeric strings (except maybe Infinity).
		SortFunction<Integer> myComparefn = new SortFunction<Integer>() {
			@Override
			public int $invoke(Integer x, Integer y) {
				if (x == null) {
					return 1;
				}
				if (y == null) {
					return -1;
				}
				return 0;
			}
		};

		Array<Integer> x = $array(null, 1);
		x.sort(myComparefn);

		assertEquals(2, x.$length());
		assertEquals(1, x.$get(0).intValue());
		assertEquals(null, x.$get(1));

		x = $array(1, null);
		x.sort(myComparefn);

		assertEquals(2, x.$length());
		assertEquals(1, x.$get(0).intValue());
		assertEquals(null, x.$get(1));
	}

	@Test
	public void testSort07() {
		Array<Integer> x = $array(1, 0);
		x.sort();

		assertEquals(2, x.$length());
		assertEquals(0, x.$get(0).intValue());
		assertEquals(1, x.$get(1).intValue());

		x = $array(1, 0);
		x.sort(null);

		assertEquals(2, x.$length());
		assertEquals(0, x.$get(0).intValue());
		assertEquals(1, x.$get(1).intValue());
	}

	@Test
	public void testSort08() {
		Array<String> alphabetR = $array("z", "y", "x", "w", "v", "u", "t", "s", "r", "q", "p", "o", "n", "M", "L",
				"K", "J", "I", "H", "G", "F", "E", "D", "C", "B", "A");
		Array<String> alphabet = $array("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "n", "o", "p",
				"q", "r", "s", "t", "u", "v", "w", "x", "y", "z");

		alphabetR.sort();
		boolean result = true;
		for (int i = 0; i < 26; i++) {
			if (!alphabetR.$get(i).equals(alphabet.$get(i))) {
				result = false;
				break;
			}
		}

		assertTrue("Enlish alphabet not correctly sorted", result);
	}

	@Test
	public void testSort09() {
		Array<String> alphabetR = $array("z", "y", "x", "w", "v", "u", "t", "s", "r", "q", "p", "o", "n", "M", "L",
				"K", "J", "I", "H", "G", "F", "E", "D", "C", "B", "A");
		Array<String> alphabet = $array("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "n", "o", "p",
				"q", "r", "s", "t", "u", "v", "w", "x", "y", "z");

		SortFunction<String> myComparefn = new SortFunction<String>() {
			@Override
			public int $invoke(String a, String b) {
				// inverse function from the normal comparison, should sort in descending order
				return -a.compareToIgnoreCase(b);
			}
		};

		alphabet.sort(myComparefn);
		boolean result = true;
		for (int i = 0; i < 26; i++) {
			if (!alphabetR.$get(i).equals(alphabet.$get(i))) {
				result = false;
				break;
			}
		}

		assertTrue("Enlish alphabet not correctly reverse sorted", result);
	}

	@Test
	public void testSort10() {
		SortFunction<Integer> myComparefn = new SortFunction<Integer>() {
			@Override
			public int $invoke(Integer a, Integer b) {
				throw new RuntimeException("error");
			}
		};

		Array<Integer> x = $array(1, 0);

		try {
			x.sort(myComparefn);
			assertTrue("Array.sort should not eat exceptions", false);
		}
		catch (RuntimeException e) {
			assertEquals("Array.sort should not eat exceptions", "error", e.getMessage());
		}
	}

	@Test
	public void testSort11() {
		Array<String> alphabetR = $array("ё", "я", "ю", "э", "ь", "ы", "ъ", "щ", "ш", "ч", "ц", "х", "ф", "у", "т",
				"с", "р", "П", "О", "Н", "М", "Л", "К", "Й", "И", "З", "Ж", "Е", "Д", "Г", "В", "Б", "А");
		Array<String> alphabet = $array("А", "Б", "В", "Г", "Д", "Е", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П",
				"р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я", "ё");

		alphabetR.sort();

		boolean result = true;
		for (int i = 0; i < 26; i++) {
			if (!alphabetR.$get(i).equals(alphabet.$get(i))) {
				result = false;
				break;
			}
		}

		assertTrue("Russian alphabet not correctly sorted", result);
	}

	@Test
	public void testSort12() {
		Array<String> alphabetR = $array("ё", "я", "ю", "э", "ь", "ы", "ъ", "щ", "ш", "ч", "ц", "х", "ф", "у", "т",
				"с", "р", "П", "О", "Н", "М", "Л", "К", "Й", "И", "З", "Ж", "Е", "Д", "Г", "В", "Б", "А");
		Array<String> alphabet = $array("А", "Б", "В", "Г", "Д", "Е", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П",
				"р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я", "ё");

		SortFunction<String> myComparefn = new SortFunction<String>() {
			@Override
			public int $invoke(String a, String b) {
				// inverse function from the normal comparison, should sort in descending order
				return -a.compareToIgnoreCase(b);
			}
		};

		alphabet.sort(myComparefn);
		boolean result = true;
		for (int i = 0; i < 26; i++) {
			if (!alphabetR.$get(i).equals(alphabet.$get(i))) {
				result = false;
				break;
			}
		}

		assertTrue("Russian alphabet not correctly reverse sorted", result);
	}

	@Test
	public void testSort13() {
		Object obj = new Object() {
			public int valueOf() {
				return 1;
			}

			@Override
			public String toString() {
				return "-2";
			}
		};
		Array<Object> alphabetR = $array(null, 2, 1, "X", -1, "a", true, obj, NaN, POSITIVE_INFINITY);
		Array<Object> alphabet = $array(-1, obj, 1, 2, POSITIVE_INFINITY, NaN, "X", "a", null, true);

		alphabetR.sort();

		boolean result = true;
		for (int i = 0; i < 10; i++) {
			Object a = alphabetR.$get(i);
			Object b = alphabet.$get(i);

			if (!(a instanceof Double && b instanceof Double && Double.isNaN((Double) a) && Double.isNaN((Double) b))) {
				if (!Objects.equals(a, b)) {
					result = false;
					break;
				}
			}
		}

		assertTrue("Check toString operator", result);
	}

	@Test
	public void testSort14() {
		Object obj = new Object() {
			public int valueOf() {
				return 1;
			}

			@Override
			public String toString() {
				return "-2";
			}
		};

		SortFunction<Object> myComparefn = new SortFunction<Object>() {
			@Override
			public int $invoke(Object x, Object y) {
				// inverse function from the normal comparison, should sort in descending order
				String xS = String(x);
				String yS = String(y);
				return -xS.compareTo(yS);
			}
		};

		Array<Object> alphabetR = $array(null, 2, 1, "X", -1, "a", true, obj, NaN, POSITIVE_INFINITY);
		Array<Object> alphabet = $array(true, null, "a", "X", NaN, POSITIVE_INFINITY, 2, 1, obj, -1);

		alphabetR.sort(myComparefn);

		boolean result = true;
		for (int i = 0; i < 10; i++) {
			Object a = alphabetR.$get(i);
			Object b = alphabet.$get(i);

			if (!(a instanceof Double && b instanceof Double && Double.isNaN((Double) a) && Double.isNaN((Double) b))) {
				if (!Objects.equals(a, b)) {
					result = false;
					break;
				}
			}
		}

		assertTrue("Check toString operator", result);
	}

	@Test
	public void testSort15(){
		// test sort in a sparse store
		Array<Object> a = Array(10001);
		a.$set(0, "e");
		a.$set(10, "d");
		a.$set(100, "c");
		a.$set(1000, "b");
		a.$set(10000, "a");

		a.sort();

		// unset/undefined values sort last
		assertEquals("a", a.$get(0));
		assertEquals("b", a.$get(1));
		assertEquals("c", a.$get(2));
		assertEquals("d", a.$get(3));
		assertEquals("e", a.$get(4));
		assertNull(a.$get(5));
		assertNull(a.$get(10));
		assertNull(a.$get(100));
		assertNull(a.$get(1000));
		assertNull(a.$get(10000));
		assertEquals(10001, a.$length());
	}
}
