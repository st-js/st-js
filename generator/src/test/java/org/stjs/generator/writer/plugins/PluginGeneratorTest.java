package org.stjs.generator.writer.plugins;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

public class PluginGeneratorTest {
	@Test
	public void testReplaceContributor() {
		assertCodeContains(Plugins1.class, "return a + 2");
	}

	@Test
	public void testFilter() {
		assertCodeContains(Plugins2.class, "return ((a + 10) + 20)");
	}
}
