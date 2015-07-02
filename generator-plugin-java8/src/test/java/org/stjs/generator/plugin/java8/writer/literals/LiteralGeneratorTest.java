package org.stjs.generator.plugin.java8.writer.literals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class LiteralGeneratorTest extends AbstractStjsTest {
	@Test
	public void testUnderscore(){
		assertCodeContains(Literal1.class, "n=100100");
	}
	
	@Test
	public void testBinary(){
		assertCodeContains(Literal2.class, "n=37");
	}
}
