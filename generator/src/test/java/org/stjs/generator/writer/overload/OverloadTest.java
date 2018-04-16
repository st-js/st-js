package org.stjs.generator.writer.overload;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class OverloadTest extends AbstractStjsTest {
	@Test
	public void testSkipNativeGeneration() {
		assertCodeDoesNotContain(Overload1.class, "method");
	}

	@Test
	public void testOverloadDifferentParamNumber() {
		// check that no other method is generated
		assertCodeContains(Overload2.class, "{method(param1: number, param2: number): void {}}");
	}

	@Test
	public void testMoreGenericType() {
		// check that no other method is generated
		assertCodeContains(Overload3.class, "{method(param1: any): void {}}");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testLessGenericType() {
		generate(Overload4.class);
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testTwoWithBody() {
		generate(Overload5.class);
	}

	@Test
	public void testVarArgs() {
		// check that no other method is generated
		assertCodeContains(Overload6.class, "{method(..._arguments: Array<any>): void {}}");
	}
}
