package org.stjs.generator.writer.bridge;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

public class BridgeGeneratorTest {
	@Test
	public void testBridgeWithNamespace() {
		assertCodeContains(Bridge1.class, "b=new test.BridgeClass()");
	}

	@Test
	public void testBridgeSyntheticWithNamespace() {
		assertCodeContains(Bridge2.class, "b={}");
	}
}
