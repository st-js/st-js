package org.stjs.generator.writer.specialMethods;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class SpecialMethodGeneratorTest extends AbstractStjsTest {

	@Test
	public void testSpecialGet() {
		// x.$get -> x[]
		assertCodeContains(SpecialMethod1.class, "{}[\"3\"]");
	}

	@Test
	public void testSpecialSet() {
		// x.$set -> x[x]
		assertCodeContains(SpecialMethod2.class, "[][\"3\"]=4");
	}

	@Test
	public void testSpecialPut() {
		// x.$put -> x[x]
		assertCodeContains(SpecialMethod3.class, "map[\"3\"]=4");
	}

	@Test
	public void testSpecialInvoke() {
		// x.$invoke(a) -> x(a)
		assertCodeContains(SpecialMethod4.class, "f(4)");
	}

	@Test
	public void testSpecialInvokeWiithJavascriptFunctionAnnotation() {
		// x.$invoke(a) -> x(a)
		assertCodeContains(SpecialMethod4a.class, "f(4)");
	}

	@Test
	public void testSpecialMap() {
		// $map(k,v) -> {k:v}
		assertCodeContains(SpecialMethod5.class, "{\"key\":1}");
	}

	// @Test
	// public void testSpecialNumber() {
	// // $map(k,v) -> {k:v}
	// assertCodeContains(SpecialMethod5a.class, "{2:1}");
	// }

	@Test(expected = JavascriptFileGenerationException.class)
	public void testWrongMapKey() {
		// $map(k,v) -> {k:v}
		generate(SpecialMethod5b.class);
	}

	@Test
	public void testSpecialArray() {
		// $array(a,b) -> [a,b]
		assertCodeContains(SpecialMethod6.class, "[1,2]");
	}

	@Test
	public void testSpecialMethodAsProp1() {
		// x.$length() -> x.length
		assertCodeContains(SpecialMethod7.class, "new TestBridge().length;");
	}

	// @Test
	// public void testSpecialMethodAsProp2() {
	// // x.$length(y) -> x.length = y
	// assertCodeContains(SpecialMethod8.class, "this.length = 1");
	// }

	@Test
	public void testSpecialLengthAppliedToString() {
		// x.$length(y) -> x.length = y
		assertCodeContains(SpecialMethod13.class, "new TestBridge().length = 1");
	}

	@Test
	public void testSpecialOr() {
		// $or(a,b) -> a || b
		assertCodeContains(SpecialMethod9.class, "3 || 4");
	}

	@Test
	public void testSpecialOrFromSuperclass() {
		// $or(a,b) -> a || b
		assertCodeContains(SpecialMethod9b.class, "3 || 4");
	}

	@Test
	public void testSpecialEquals() {
		// no longer [x.equals(y) -> x == y], but keep equals as is
		assertCodeContains(SpecialMethod10.class, "x.equals(2)");
	}

	@Test
	public void testSpecialNotEquals() {
		// no longer [!x.equals(y) -> !(x == y)], but keep equals as is
		assertCodeContains(SpecialMethod11.class, "!x.equals(2)");
	}

	@Test
	public void testSpecialEquals2() {
		// x == y -> x == y
		assertCodeContains(SpecialMethod10a.class, "x == 2");
	}

	@Test
	public void testSpecialNotEquals2() {
		// x != y -> x != 2
		assertCodeContains(SpecialMethod11a.class, "x != 2");
	}

	@Test
	public void testSpecialGetObject() {
		// $get(x,y) -> x[y]
		assertCodeContains(SpecialMethod12.class, "obj[\"a\"]");
	}

	@Test
	public void testAssertMethods() {
		assertCodeContains(SpecialMethod14.class, "assertArgEquals(\"SpecialMethod14.java:8\",\"assertArgEquals(\\\"123\\\", x)\", \"123\", x);");
	}

	@Test
	public void testSpecialDelete() {
		// x.$delete(key) -> delete x[key]
		assertCodeContains(SpecialMethod15.class, "delete {}[\"key\"]");
	}

	@Test
	public void testStringLength() {
		// string.length() -> string.length
		assertCodeContains(SpecialMethod16.class, "n = (\"a\" + b).length;");
	}

	@Test
	public void testProperties() {
		// Map<K,V> x = $properties(obj) -> var x = obj;
		assertCodeContains(SpecialMethod17.class, "var map = (123);");
	}

	@Test
	public void testObject() {
		// x = $object(map) -> var x = map;
		assertCodeContains(SpecialMethod18.class, "var p = (map);");
	}

	@Test
	public void testCastArray() {
		// Array<V> x = $castArray(obj[]) -> var x = obj;
		assertCodeContains(SpecialMethod19.class, "var a = (\"abc\".split(\",\"));");
	}

	@Test
	public void testKeepJQuery$() {
		assertCodeContains(SpecialMethod20.class, "var div = $(\"div\");");
	}

	@Test
	public void testJs() {
		assertCodeContains(SpecialMethod21.class, "x = s.a");
	}

	@Test
	public void testJs2() {
		assertCodeContains(SpecialMethod21a.class, "if (this.a == null) {return s.a;} return this.a;");
	}

	@Test
	public void testTemplateNone() {
		assertCodeContains(SpecialMethod22.class, "n = new TestBridge().$get(0)");
	}

	@Test
	public void testTypeOf() {
		assertCodeContains(SpecialMethod23.class, "n = (typeof \"abc\")");
	}

	@Test
	public void testTypeOf2() {
		assertCodeContains(SpecialMethod23a.class, "n = (typeof t)");
	}

	@Test
	public void testPrefix() {
		assertCodeContains(SpecialMethod24.class, "n = new TestBridge().prefix()");
	}

	@Test
	public void testPrefixWithParameter() {
		assertCodeContains(SpecialMethod25.class, "n = new TestBridge().say()");
	}

	@Test
	public void testSuffixWithParameter() {
		assertCodeContains(SpecialMethod26.class, "new TestBridge().say()");
	}
}
