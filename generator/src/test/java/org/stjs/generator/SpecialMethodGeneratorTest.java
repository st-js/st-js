package org.stjs.generator;

import static org.stjs.generator.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.specialMethods.SpecialMethod1;
import test.generator.specialMethods.SpecialMethod10;
import test.generator.specialMethods.SpecialMethod2;
import test.generator.specialMethods.SpecialMethod3;
import test.generator.specialMethods.SpecialMethod4;
import test.generator.specialMethods.SpecialMethod5;
import test.generator.specialMethods.SpecialMethod6;
import test.generator.specialMethods.SpecialMethod7;
import test.generator.specialMethods.SpecialMethod8;
import test.generator.specialMethods.SpecialMethod9;

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
	public void testSpecialOr() {
		// $or(a,b) -> a || b
		assertCodeContains(SpecialMethod9.class, "3 || 4");
	}

	@Test
	public void testSpecialEquals() {
		// x.equals(y) -> x == y
		assertCodeContains(SpecialMethod10.class, "x == 2");
	}
}
