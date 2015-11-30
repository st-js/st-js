package org.stjs.generator.writer.overload;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class OverloadTest extends AbstractStjsTest {
	@Test
	public void testSkipNativeGeneration() {
		assertCodeDoesNotContain(Overload1.class, "method");
	}

	@Test
	public void testOverloadDifferentParamNumber() {
		// check that no other method is generated
		assertCodeContains(Overload2.class, "{prototype.method=function(param1, param2){};}");
	}

	@Test
	public void testVarArgs() {
		// check that no other method is generated
		assertCodeContains(Overload6.class, "{prototype.method=function(_arguments){};}");
	}
}
