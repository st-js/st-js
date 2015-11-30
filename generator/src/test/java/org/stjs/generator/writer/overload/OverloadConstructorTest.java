package org.stjs.generator.writer.overload;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class OverloadConstructorTest extends AbstractStjsTest {
	@Test
	public void testSkipNativeGeneration() {
		assertCodeDoesNotContain(Overload1c.class, "n = 10");
	}

	@Test
	public void testOverloadDifferentParamNumber() {
		// check that no other method is generated
		assertCodeContains(Overload2c.class, "Overload2c=function(param1, param2){}");
	}

	@Test
	public void testMoreGenericType() {
		// check that no other method is generated
		assertCodeContains(Overload3c.class, "Overload3c=function(param1){}");
	}

	@Test
	public void testVarArgs() {
		// check that no other method is generated
		assertCodeContains(Overload6c.class, "Overload6c=function(_arguments){}");
	}
}
