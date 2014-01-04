package org.stjs.generator.plugin.java8.writer.switches;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

public class SwitchGeneratorTest {

	// this one if for Java 7 actually
	@Test
	public void testStringLabelSwitch() {
		assertCodeContains(Switch1.class, "switch(s) {case \"abc\": break; case \"xyz\":break;}");
	}
}
