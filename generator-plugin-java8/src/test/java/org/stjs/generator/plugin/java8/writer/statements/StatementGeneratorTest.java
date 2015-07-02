package org.stjs.generator.plugin.java8.writer.statements;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class StatementGeneratorTest extends AbstractStjsTest {
	@Test
	public void testMultipleCatch(){
		assertCodeContains(Statement1.class, "catch(e){throw new RuntimeException(e);}");
	}
}
