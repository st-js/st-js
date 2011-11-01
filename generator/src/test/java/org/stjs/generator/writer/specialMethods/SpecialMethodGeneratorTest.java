package org.stjs.generator.writer.specialMethods;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

public class SpecialMethodGeneratorTest {

	@Test
	public void testSpecialGet() {
		// x.$get -> x[]
		assertCodeContains(SpecialMethod1.class, "this[\"3\"]");
	}

	@Test
	public void testSpecialSet() {
		// x.$set -> x[x]
		assertCodeContains(SpecialMethod2.class, "this[\"3\"]=4");
	}

	@Test
	public void testSpecialPut() {
		// x.$put -> x[x]
		assertCodeContains(SpecialMethod3.class, "this[\"3\"]=4");
	}

	@Test
	public void testSpecialInvoke() {
		// x.$invoke(a,b) -> x(a,b)
		assertCodeContains(SpecialMethod4.class, "this(\"3\", 4)");
	}

	@Test
	public void testSpecialMap() {
		// $map(k,v) -> {k:v}
		assertCodeContains(SpecialMethod5.class, "{\"key\":1}");
	}

	@Test
	public void testSpecialArray() {
		// $array(a,b) -> [a,b]
		assertCodeContains(SpecialMethod6.class, "[1,2]");
	}

	@Test
	public void testSpecialMethodAsProp1() {
		// x.$length() -> x.length
		assertCodeContains(SpecialMethod7.class, "this.length;");
	}

	@Test
	public void testSpecialMethodAsProp2() {
		// x.$length(y) -> x.length = y
		assertCodeContains(SpecialMethod8.class, "this.length = 1");
	}

	@Test
	public void testSpecialLengthAppliedToString() {
		// $length(x, y) -> x.length = y
		assertCodeContains(SpecialMethod13.class, "(this).length = 1");
	}

	@Test
	public void testSpecialOr() {
		// $or(a,b) -> a || b
		assertCodeContains(SpecialMethod9.class, "3 || 4");
	}

	@Test
	public void testSpecialEquals() {
		// x.equals(y) -> x == y
		assertCodeContains(SpecialMethod10.class, "(x == 2)");
	}

	@Test
	public void testSpecialNotEquals() {
		// !x.equals(y) -> !(x == y)
		assertCodeContains(SpecialMethod11.class, "!(x == 2)");
	}

	@Test
	public void testSpecialGetObject() {
		// $get(x,y) -> x[y]
		assertCodeContains(SpecialMethod12.class, "(obj)[\"a\"]");
	}

	@Test
	public void testAssertMethods() {
		// the special parameter THIS should not be added
		assertCodeContains(SpecialMethod14.class,
				"assertArgEquals(\"SpecialMethod14.java:8\",\"assertArgEquals(\\\"123\\\", x)\", \"123\", x);");
	}

	@Test
	public void testSpecialDelete() {
		// x.$delete(key) -> delete x[key]
		assertCodeContains(SpecialMethod15.class, "delete this[\"key\"]");
	}

	@Test
	public void testStringLength() {
		// string.length() -> string.length
		assertCodeContains(SpecialMethod16.class, "n = (\"a\" + \"b\").length;");
	}

}
