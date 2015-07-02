package org.stjs.generator.plugin.java8.writer.switches;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class SwitchGeneratorTest extends AbstractStjsTest {

	// this one if for Java 7 actually
	@Test
	public void testStringLabelSwitch() {
		assertCodeContains(Switch1.class, "switch(s) {case \"abc\": break; case \"xyz\":break;}");
	}
}
