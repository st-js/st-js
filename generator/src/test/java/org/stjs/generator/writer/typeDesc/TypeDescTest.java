package org.stjs.generator.writer.typeDesc;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class TypeDescTest extends AbstractStjsTest {
	@Test
	public void testBasicField() {
		assertCodeContains(TypeDesc1.class, "x: number");
	}

	@Test
	public void testArrayOfBasicField() {
		assertCodeContains(TypeDesc2.class, "x: Array<number>");
	}

	@Test
	public void testNonBasicField() {
		assertCodeContains(TypeDesc3.class, "x: Date");
	}

	@Test
	public void testArrayOfNonBasicField() {
		assertCodeContains(TypeDesc4.class, "x: Array<Date>");
	}

	@Test
	public void testMapOfNonBasicField() {
		assertCodeContains(TypeDesc5.class, "x: {[key: string]: Date}");
	}

	@Test
	public void testEnum() {
		assertCodeContains(TypeDesc6.class, "x: TypeDesc6_Type");
	}

	@Test
	public void testWildcards() {
		// TODO :: should be `x: MyType1<? extends MyType2>`
		assertCodeContains(TypeDesc7.class, "field: MyType1<any>");
	}
}
