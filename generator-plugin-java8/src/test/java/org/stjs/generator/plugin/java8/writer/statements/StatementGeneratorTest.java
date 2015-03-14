package org.stjs.generator.plugin.java8.writer.statements;

import org.junit.Test;
import org.stjs.generator.utils.GeneratorTestHelper;

public class StatementGeneratorTest {
	@Test
	public void testMultipleCatch(){
		GeneratorTestHelper.assertCodeContains(Statement1.class, "catch(e){throw new RuntimeException(e);}");
	}
}
