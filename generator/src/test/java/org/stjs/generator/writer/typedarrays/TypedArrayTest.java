package org.stjs.generator.writer.typedarrays;

import static org.junit.Assert.*;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class TypedArrayTest extends AbstractStjsTest {

	int ga() {
		return 42;
	}

	@Test
	public void testDummy() throws Exception {
		float arr[][] = new float[ga()][];
		System.out.println(arr[0]);
		float[][] arr2 = { {}, {} };
		System.out.println(arr2);

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
		System.out.println(generated);
		assertCodeContains(generated, "new Int8Array()");
		assertCodeContains(generated, "new Int16Array()");
		assertCodeContains(generated, "new Uint16Array()");
		assertCodeContains(generated, "new Int32Array()");
		assertCodeContains(generated, "new Float32Array()");
		assertCodeContains(generated, "new Float64Array()");

	}

}
