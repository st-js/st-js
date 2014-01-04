package org.stjs.generator.plugin.java8.writer.literals;

import org.junit.Test;
import org.stjs.generator.utils.GeneratorTestHelper;

public class LiteralGeneratorTest {
	@Test
	public void testUnderscore(){
		GeneratorTestHelper.assertCodeContains(Literal1.class, "n=100100");
	}
	
	@Test
	public void testBinary(){
		GeneratorTestHelper.assertCodeContains(Literal2.class, "n=37");
	}
}
