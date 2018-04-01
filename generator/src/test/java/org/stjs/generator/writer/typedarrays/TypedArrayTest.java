package org.stjs.generator.writer.typedarrays;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class TypedArrayTest extends AbstractStjsTest {

	@Test
	public void testEnhancedForLoopArray() throws Exception {
		assertCodeContains(EnhancedForLoopIntArray.class, "var arr = new Int32Array([42, 43]);");
		// int expected = EnhancedForLoopIntArray.method();
		// assertEquals(expected, executeAndReturnNumber(EnhancedForLoopIntArray.class), 0.01);
	}

	@Test
	public void testFloatArray() throws Exception {
		assertCodeContains(Float32ArrayInit.class, "new Float32Array(1)");
	}

	@Test
	public void testMF32AInit() throws Exception {
		assertCodeContains(MultiF32AInit.class,
			"Array.apply(null, Array(1))" //
				+ ".map(function(){return Array.apply(null, Array(2))" //
				+ ".map(function(){return new Float32Array(3);});});");
	}

	@Test
	public void testFloatArrayInit0() throws Exception {
		assertCodeContains(Float32ArrayInit0.class, "new Float32Array()");
	}

	@Test
	public void testFloatArrayInit00() throws Exception {
		assertCodeContains(Float32ArrayInit0Empty.class, "var arr = new Float32Array()");
	}

	@Test
	public void testMultiF32AInit0Empty() throws Exception {
		assertCodeContains(MultiF32AInit0Empty.class, "var arr = [];");
	}

	@Test
	public void testMultiF32AInit0Empty2() throws Exception {
		assertCodeContains(MultiF32AInit0Empty2.class, "var arr2 = [[]];");
	}

	@Test
	public void testMultiF32AInit0Empty3() throws Exception {
		assertCodeContains(MultiF32AInit0Empty3.class, "var arr3 = [[new Float32Array()]];");
	}

	@Test
	public void testFloatArrayInit1() throws Exception {
		assertCodeContains(Float32ArrayInit1.class, "new Float32Array([1.2, 2.0, 3, 0.4, this.a()])");
	}

	@Test
	public void testMultiFloatArrayInit1() throws Exception {
		/* @formatter:off */
		assertCodeContains(MultiF32AInit1.class,
			"["
				+ "["
				+ "new Float32Array([1.2, 2.0, 3, 0.4, this.a()]),"
				+ "new Float32Array(),"
				+ "new Float32Array(1),"
				+ "this.myarray()"
				+ "],"
				+ "[]"
				+ "]");
		/* @formatter:on */
	}

	@Test
	public void testFloatArrayInit2() throws Exception {
		assertCodeContains(Float32ArrayInit2.class, "new Float32Array([1.2, 2.0, 3, 0.4, this.a()])");
	}

	@Test
	public void testTypes() throws Exception {
		String generated = generate(Types.class);
		assertCodeContains(generated, "var bool = new Int8Array()");
		assertCodeContains(generated, "var b = new Int8Array()");
		assertCodeContains(generated, "var s = new Int16Array()");
		assertCodeContains(generated, "var c = new Uint16Array()");
		assertCodeContains(generated, "var i = new Int32Array()");
		assertCodeContains(generated, "var f = new Float32Array()");
		assertCodeContains(generated, "var d = new Float64Array()");
	}

	@Test
	public void testPrimitiveArrayFields() throws Exception {
		String generated = generate(PrimitiveArrayFields.class);
		assertCodeContains(
			generated,
			"bool: \"Int8Array\", b: \"Int8Array\", s: \"Int16Array\", c: \"Uint16Array\", i: \"Int32Array\", f: \"Float32Array\", d: \"Float64Array\", bool2d: \"Array\"");
	}

	@Test
	public void testMultiInit() throws Exception {
		String expected = "Array.apply(null, Array(3)).map(function(){return Array(2);});"; //
		assertCodeContains(MultiInit.class, expected);
	}

	@Test
	public void testMultiInit2() throws Exception {
		/* @formatter:off */
		String expected =
			"var ac = [Array.apply(null, Array(2)).map(function() {" + //
				"    return new Int32Array(3);" + //
				"}), Array.apply(null, Array(4)).map(function() {" + //
				"    return new Int32Array(5);" + //
				"}), Array.apply(null, Array(6)).map(function() {" + //
				"    return new Int32Array(7);" + //
				"})]";
		/* @formatter:on */
		assertCodeContains(MultiInit2.class, expected);
	}

	@Test
	public void testArrayMath() throws Exception {
		assertCodeContains(ArrayMath.class, "c[i]++;");
		// int expected = ArrayMath.method();
		// assertEquals(expected, executeAndReturnNumber(ArrayMath.class), 0);
	}

	@Test
	public void testBooleanArray() throws Exception {
		// int expected = BooleanArray.method();
		// assertEquals(expected, executeAndReturnNumber(BooleanArray.class), 0);

		assertCodeContains(BooleanArray.class, "var a = new Int8Array(3);");
	}

	@Test
	public void testInstanceOf() {
		String generated = generate(ArrayInstanceOf.class);
		assertCodeContains(generated, "isInstanceOf(o.constructor, Int8Array)");
		// assertEquals(44., executeAndReturnNumber(ArrayInstanceOf.class), 0.01);
	}
}
