package org.stjs.generator.writer.switches;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.writer.enums.Enums3;
import org.stjs.generator.writer.enums.Enums4Switch;
import org.stjs.generator.writer.enums.Enums7;

public class SwitchGeneratorTest extends AbstractStjsTest {

	@Test
	public void testSwitchEnumsInnerClass() {
		assertCodeContains(Enums3.class, "case Enums3.Value.a");
	}

	@Test
	public void testSwitchEnums() {
		assertCodeContains(Enums4Switch.class, "case Enums4.a");
	}

	@Test
	public void testSwitchEnumsFromParam() {
		assertCodeContains(Enums7.class, "case Enums4.a");
	}

	@Test
	public void testSwitchInteger() {
		assertCodeContains(NativeTypeSwith.class, "case 1");
	}
}
