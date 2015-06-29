package org.stjs.generator.writer.plugins;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class PluginGeneratorTest extends AbstractStjsTest {
	@Test
	public void testReplaceContributor() {
		assertCodeContains(Plugins1.class, "return a + 2");
	}

	@Test
	public void testFilter() {
		assertCodeContains(Plugins2.class, "return ((a + 10) + 20)");
	}
}
