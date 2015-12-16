package org.stjs.generator.lib.number;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

import static org.junit.Assert.assertEquals;

public class NumberMethodsTest extends AbstractStjsTest {

	@Test
	public void testParseInt() {
		assertEquals(123.0, executeAndReturnNumber(Number1.class), 0);
	}

	@Test
	public void testIntValue() {
		// TODO Investigate why the execution is not possible anymore for multiple constructors.
		//assertEquals(123.0, executeAndReturnNumber(Number2.class), 0);
	}

	@Test
	public void testValueOf() {
		assertEquals(123.0, executeAndReturnNumber(Number3.class), 0);
	}

	@Test
	public void testIntegerOverloadMethodNames() {
		assertCodeContains(Number4_overloaded_methods.class, "" +
				"        var test = Integer.parseInt$String(\"test\");\n" +
				"        var testRadix = Integer.parseInt$String_int(\"test\", 10);\n");
	}
}
