package org.stjs.generator.writer.bridge;

import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.writer.bridge.pack.Bridge2;

public class BridgeTest {
	@Test(
			expected = IndexOutOfBoundsException.class)
	public void testBridge() {
		// the class is a bridge so no code is actually generated
		generate(Bridge1.class);
	}

	@Test(
			expected = IndexOutOfBoundsException.class)
	public void testBridgePackageAnnotation() {
		// the class is a bridge so no code is actually generated
		generate(Bridge2.class);
	}
}
