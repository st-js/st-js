package org.stjs.generator.writer.typedarrays;

import static org.junit.Assert.*;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class ArrayTest extends AbstractStjsTest {
	@Test
	public void testSimple() throws Exception {
		assertCodeContains(StringArrayInit.class, "var arr = Array(1); var simple = [];");
	}

	@Test
	public void testOneDimInit() throws Exception {
		String expected = "var arr = [ \"\", null, \"hello\", \"world\", this.a() ]";
		assertCodeContains(StringArrayInit1.class, expected);
	}

	@Test
	public void testMultiDim() throws Exception {
		String expected = "Array.apply(null, Array(1))" //
				+ ".map(function(){return Array.apply(null, Array(2))" //
				+ ".map(function(){return Array(3);});});";
		assertCodeContains(MultiStringArrayInit.class, expected);
	}

	@Test
	public void testCrazy() throws Exception {
		String expected = "[[[\"hello\"], [\"world\"], []], [[1, 2], [3, 4]], Array.apply(null, Array(4)).map(function() {return Array(5);})]";
		assertCodeContains(ObjectMultiInit2.class, expected);
	}

}
