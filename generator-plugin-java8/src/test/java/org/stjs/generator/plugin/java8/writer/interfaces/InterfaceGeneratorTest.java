package org.stjs.generator.plugin.java8.writer.interfaces;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stjs.generator.MultipleFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

public class InterfaceGeneratorTest extends AbstractStjsTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void testStaticMethod() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("Methods in interfaces should not have a body.");
		assertCodeContains(Interface1.class, "constructor.staticMethod=function(){return 1;}");
	}

	@Test
	public void testDefaultMethod() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("Methods in interfaces should not have a body.");
		assertEquals(1, executeAndReturnNumber(Class2.class), 0);
	}
}
