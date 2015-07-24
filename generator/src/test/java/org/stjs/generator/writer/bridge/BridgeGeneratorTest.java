package org.stjs.generator.writer.bridge;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.writer.bridge.pack.Bridge4;

public class BridgeGeneratorTest extends AbstractStjsTest {
	@Test
	public void testBridgeWithNamespace() {
		assertCodeContains(Bridge1.class, "b=new test.BridgeClass()");
	}

	@Test
	public void testBridgeSyntheticWithNamespace() {
		assertCodeContains(Bridge2.class, "b={}");
	}

	@Test(
			expected = IndexOutOfBoundsException.class)
	public void testBridge() {
		// the class is a bridge so no code is actually generated
		generate(Bridge3.class);
	}

	@Test(
			expected = IndexOutOfBoundsException.class)
	public void testBridgePackageAnnotation() {
		// the class is a bridge so no code is actually generated
		generate(Bridge4.class);
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testTemplateOutsideBridge() {
		generate(Bridge5.class);
	}

	@Test
	public void testBridgeAsInnerClass(){
		assertCodeDoesNotContain(Bridge6.class, "constructor.ActualBridge = function()");
	}
}
